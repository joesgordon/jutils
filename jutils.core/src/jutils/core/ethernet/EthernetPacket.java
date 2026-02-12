package jutils.core.ethernet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.ethernet.Ipv4Header.Ipv4HeaderSerializer;
import jutils.core.ethernet.Ipv6Header.Ipv6HeaderSerializer;
import jutils.core.ethernet.MacHeader.MacHeaderSerializer;
import jutils.core.ethernet.TcpHeader.TcpHeaderSerializer;
import jutils.core.ethernet.UdpHeader.UdpHeaderSerializer;
import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EthernetPacket implements IPacketData
{
    /**  */
    public final List<ITcpIpLayer> layers;
    /**  */
    public byte [] applicationLayer;

    /***************************************************************************
     * 
     **************************************************************************/
    public EthernetPacket()
    {
        this.layers = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        for( ITcpIpLayer header : layers )
        {
            printer.printTier( header.getName(), header );
        }

        printer.printFieldValues( "Application Layer", applicationLayer );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class TcpIpPacketSerializer
        implements IDataSerializer<EthernetPacket>
    {
        /**  */
        private final Map<Class<? extends ITcpIpLayer>,
            IDataSerializer<? extends ITcpIpLayer>> serializers;

        /**
         * 
         */
        public TcpIpPacketSerializer()
        {
            this.serializers = new HashMap<>();

            this.serializers.put( MacHeader.class, new MacHeaderSerializer() );
            this.serializers.put( Ipv4Header.class,
                new Ipv4HeaderSerializer() );
            this.serializers.put( Ipv6Header.class,
                new Ipv6HeaderSerializer() );
            this.serializers.put( TcpHeader.class, new TcpHeaderSerializer() );
            this.serializers.put( UdpHeader.class, new UdpHeaderSerializer() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EthernetPacket read( IDataStream stream )
            throws IOException, ValidationException
        {
            EthernetPacket packet = new EthernetPacket();

            stream.setOrder( ByteOrdering.BIG_ENDIAN );

            IDataSerializer<? extends ITcpIpLayer> serializer;
            Class<? extends ITcpIpLayer> layerClass;

            layerClass = MacHeader.class;

            while( layerClass != null )
            {
                serializer = serializers.get( layerClass );

                ITcpIpLayer layer = serializer.read( stream );

                packet.layers.add( layer );

                layerClass = layer.getNextLayerType();
            }

            // TODO determine app size better

            int appSize = ( int )stream.getAvailable();

            packet.applicationLayer = new byte[appSize];

            stream.readFully( packet.applicationLayer );

            return packet;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( EthernetPacket data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
