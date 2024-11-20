package jutils.core.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NicInfo implements ITierPrinter
{
    /**  */
    public final NetworkInterface nic;
    /**  */
    public final InetAddress address;

    /**  */
    public final String name;
    /**  */
    public final String addressString;
    /**  */
    public final boolean isIpv4;

    /***************************************************************************
     * @param nic {@code null} indicates Any.
     * @param address the address of the provided NIC may not be null.
     **************************************************************************/
    public NicInfo( NetworkInterface nic, InetAddress address )
    {
        this.nic = nic;
        this.name = nic != null ? nic.getDisplayName() : "Any";
        this.address = address;
        this.addressString = address.getHostAddress();
        this.isIpv4 = address instanceof Inet4Address;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IpAddress toIpAddress()
    {
        IpAddress ip = new IpAddress();

        ip.setInetAddress( address );

        return ip;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static NicInfo createAny()
    {
        try
        {
            byte [] bytes = new byte[] { 0, 0, 0, 0 };
            InetAddress address = InetAddress.getByAddress( bytes );

            return new NicInfo( null, address );
        }
        catch( UnknownHostException ex )
        {
            throw new RuntimeException(
                "0.0.0.0 was identified as a bad address" );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return name + "|" + addressString;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Name", name );
        printer.printField( "Address", addressString );

        if( nic != null )
        {
            printer.printTier( "NIC", new NicTier( nic ) );
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
