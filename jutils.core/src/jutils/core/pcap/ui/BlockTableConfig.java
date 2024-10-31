package jutils.core.pcap.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jutils.core.data.DataItemPair;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;
import jutils.core.pcap.blocks.EnhancedPacket;
import jutils.core.pcap.ethernet.EtherType;
import jutils.core.pcap.ethernet.ITcpIpLayer;
import jutils.core.pcap.ethernet.Ipv4Header;
import jutils.core.pcap.ethernet.MacHeader;
import jutils.core.pcap.ethernet.Protocol;
import jutils.core.pcap.ethernet.TcpIpPacket;
import jutils.core.time.TimeUtils;
import jutils.core.ui.model.DefaultTableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockTableConfig
    extends DefaultTableItemsConfig<DataItemPair<IBlock>>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public BlockTableConfig()
    {
        super.addCol( "Type", String.class,
            ( d ) -> BlockType.fromValue( d.item.id ).getDescription() );
        super.addCol( "Size", Integer.class, ( d ) -> d.data.length );
        super.addCol( "Length", Integer.class, ( d ) -> d.item.length );
        super.addCol( "Time", String.class, ( d ) -> getTime( d.item ) );
        super.addCol( "App Length", Integer.class,
            ( d ) -> getAppLen( d.item ) );
        super.addCol( "Ether Type", EtherType.class,
            ( d ) -> getEtherType( d.item ) );
        super.addCol( "Protocol", Protocol.class,
            ( d ) -> getProtocol( d.item ) );
    }

    /***************************************************************************
     * @param item
     * @return
     **************************************************************************/
    private Integer getAppLen( IBlock item )
    {
        return getEnhancedField( item, ( e ) -> getAppLen( e ) );
    }

    /***************************************************************************
     * @param item
     * @return
     **************************************************************************/
    private Integer getAppLen( EnhancedPacket item )
    {
        TcpIpPacket tcpip = item.readData();

        if( tcpip != null )
        {
            return tcpip.applicationLayer.length;
        }

        return null;
    }

    /***************************************************************************
     * @param block
     * @return
     **************************************************************************/
    private static String getTime( IBlock block )
    {
        return getEnhancedField( block, ( e ) -> getTime( e.timestamp ) );
    }

    /***************************************************************************
     * @param block
     * @return
     **************************************************************************/
    private static EtherType getEtherType( IBlock block )
    {
        IFieldGetter<EnhancedPacket, EtherType> getter;

        getter = ( e ) -> getLayerField( e, MacHeader.class,
            ( lr ) -> lr.getEtherType() );

        return getEnhancedField( block, getter );
    }

    /***************************************************************************
     * @param block
     * @return
     **************************************************************************/
    private static Protocol getProtocol( IBlock block )
    {
        IFieldGetter<EnhancedPacket, Protocol> getter;

        getter = ( e ) -> getLayerField( e, Ipv4Header.class,
            ( lr ) -> lr.getProtocol() );

        Protocol p = getEnhancedField( block, getter );

        return p;
    }

    /***************************************************************************
     * @param <L>
     * @param <T>
     * @param e
     * @param layerClass
     * @param getter
     * @return
     **************************************************************************/
    private static <L extends ITcpIpLayer, T> T getLayerField( EnhancedPacket e,
        Class<L> layerClass, IFieldGetter<L, T> getter )
    {
        TcpIpPacket tcpip = e.readData();

        if( tcpip != null )
        {
            for( ITcpIpLayer layer : tcpip.layers )
            {
                if( layerClass.isAssignableFrom( layer.getClass() ) )
                {
                    @SuppressWarnings( "unchecked")
                    L lr = ( L )layer;
                    return getter.get( lr );
                }
            }
        }

        return null;
    }

    /***************************************************************************
     * @param timestamp
     * @return
     **************************************************************************/
    private static String getTime( long timestamp )
    {
        long millis = timestamp / 1000;
        long micros = timestamp % 1000;

        LocalDateTime time = TimeUtils.fromLinuxEpoch( millis );

        time = time.plus( micros, ChronoUnit.MICROS );

        return time.format( TimeUtils.buildDateTimeDisplayFormat() );
    }

    /***************************************************************************
     * @param <T>
     * @param block
     * @param getter
     * @return
     **************************************************************************/
    private static <T> T getEnhancedField( IBlock block,
        IFieldGetter<EnhancedPacket, T> getter )
    {
        if( block instanceof EnhancedPacket )
        {
            return getter.get( ( EnhancedPacket )block );
        }

        return null;
    }
}
