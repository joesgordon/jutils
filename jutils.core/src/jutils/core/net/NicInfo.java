package jutils.core.net;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.utils.EnumerationIteratorAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NicInfo implements ITierPrinter
{
    /**  */
    public final String name;
    /**  */
    public final byte [] hardwareAddress;
    /**  */
    public final int mtuSize;

    /**  */
    public final boolean isUp;
    /**  */
    public final boolean isLoopback;
    /**  */
    public final boolean isPointToPoint;
    /**  */
    public final boolean isVirtual;
    /**  */
    public final boolean isMulticastSupported;

    /**  */
    public final List<AddressInfo> ipAddresses;
    /**  */
    public final List<NicInfo> subNics;

    /***************************************************************************
     * @param nic the interface to get information from.
     * @throws RuntimeException if a {@link SocketException} is thrown.
     **************************************************************************/
    public NicInfo( NetworkInterface nic ) throws RuntimeException
    {
        try
        {
            this.name = nic != null ? nic.getDisplayName() : "Any";
            this.hardwareAddress = nic.getHardwareAddress();
            this.mtuSize = nic.getMTU();

            this.isUp = nic.isUp();
            this.isLoopback = nic.isLoopback();
            this.isPointToPoint = nic.isPointToPoint();
            this.isVirtual = nic.isVirtual();
            this.isMulticastSupported = nic.supportsMulticast();

            this.ipAddresses = new ArrayList<>();
            this.subNics = new ArrayList<>();

            for( InterfaceAddress intf : nic.getInterfaceAddresses() )
            {
                ipAddresses.add( new AddressInfo( intf ) );
            }

            for( NetworkInterface subnic : new EnumerationIteratorAdapter<>(
                nic.getSubInterfaces() ) )
            {
                subNics.add( new NicInfo( subnic ) );
            }
        }
        catch( SocketException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Name", name );
        printer.printFieldValues( "Hardware Address", hardwareAddress );
        printer.printField( "MTU Size", mtuSize );
        printer.printField( "Is Up", isUp );
        printer.printField( "Is Loopback", isLoopback );
        printer.printField( "Is Point-to-Point", isPointToPoint );
        printer.printField( "Is Virtual", isVirtual );
        printer.printTiers( "Address", ipAddresses );
        printer.printTiers( "Sub Nics", subNics );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class AddressInfo implements ITierPrinter
    {
        /**  */
        public final IpAddress ip;
        /**  */
        public final IpAddress broadcast;
        /**  */
        public short prefixLength;

        /**
         * @param intf the interface to get information from.
         */
        public AddressInfo( InterfaceAddress intf )
        {
            this.ip = new IpAddress();
            this.broadcast = new IpAddress();
            this.prefixLength = intf.getNetworkPrefixLength();

            ip.setInetAddress( intf.getAddress() );
            if( intf.getBroadcast() != null )
            {
                broadcast.setInetAddress( intf.getBroadcast() );
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "IP Address", ip );
            printer.printField( "Broadcast Address", broadcast );
            printer.printField( "Prefix Length", prefixLength );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class NicTier implements ITierPrinter
    {
        /**  */
        private NetworkInterface nic;

        /**
         * @param nic
         */
        public NicTier( NetworkInterface nic )
        {
            this.nic = nic;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "Name", nic.getName() );
            printer.printField( "Display Name", nic.getDisplayName() );
            printer.printField( "Index", nic.getIndex() );
            printer.printField( "Virtual", nic.isVirtual() );

            try
            {
                printer.printField( "Up", nic.isUp() );
            }
            catch( SocketException ex )
            {
                printer.printField( "Up", "ERROR: " + ex.getMessage() );
            }

            try
            {
                printer.printField( "Supports Multicast",
                    nic.supportsMulticast() );
            }
            catch( SocketException ex )
            {
                printer.printField( "Supports Multicast",
                    "ERROR: " + ex.getMessage() );
            }

            try
            {
                printer.printFieldValues( "Hardware Address",
                    nic.getHardwareAddress() );
            }
            catch( SocketException ex )
            {
                printer.printField( "Hardware Address",
                    "ERROR: " + ex.getMessage() );
            }

            try
            {
                printer.printField( "MTU", nic.getMTU() );
            }
            catch( SocketException ex )
            {
                printer.printField( "MTU", "ERROR: " + ex.getMessage() );
            }

            try
            {
                printer.printField( "Loopback", nic.isLoopback() );
            }
            catch( SocketException ex )
            {
                printer.printField( "Loopback", "ERROR: " + ex.getMessage() );
            }

            List<InterfaceTier> ifas = new ArrayList<>();
            for( InterfaceAddress ifa : nic.getInterfaceAddresses() )
            {
                ifas.add( new InterfaceTier( ifa ) );
            }
            printer.printTiers( "Addresses", ifas );

            List<NicTier> subs = new ArrayList<>();
            for( NetworkInterface n : Collections.list(
                nic.getSubInterfaces() ) )
            {
                subs.add( new NicTier( n ) );
            }
            printer.printTiers( "Sub-Interfaces", subs );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class InterfaceTier implements ITierPrinter
    {
        /**  */
        private InterfaceAddress ifa;

        /**
         * @param ifa
         */
        public InterfaceTier( InterfaceAddress ifa )
        {
            this.ifa = ifa;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "Address", ifa.getAddress() );
            printer.printField( "Broadcast", ifa.getBroadcast() );
            printer.printField( "Network Prefix Length",
                ifa.getNetworkPrefixLength() );
        }
    }
}
