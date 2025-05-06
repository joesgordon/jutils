package jutils.telemetry.ch10.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.border.EmptyBorder;

import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.IItemStream;
import jutils.core.ui.PaginatedTableView;
import jutils.core.ui.model.DefaultTableItemsConfig;
import jutils.core.ui.model.IDataView;
import jutils.telemetry.ch10.Ch10File;
import jutils.telemetry.ch10.Ch10Utils;
import jutils.telemetry.ch10.PacketInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketInfosView implements IDataView<Ch10File>
{
    /**  */
    private final PaginatedTableView<PacketInfo> table;
    /**  */
    private final PacketTableConfig config;
    /**  */
    private final PacketStream packetsStream;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketInfosView()
    {
        this.packetsStream = new PacketStream();
        this.config = new PacketTableConfig();
        this.table = new PaginatedTableView<>( config, packetsStream,
            new PacketInfoView() );

        table.setItemTypeName( "Packet" );

        table.setScrollPaneBorder( new EmptyBorder( 0, 0, 0, 0 ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Component getView()
    {
        return table.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Ch10File getData()
    {
        return this.packetsStream.c10;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Ch10File c10 )
    {
        this.packetsStream.setValue( c10 );
        table.setItems( packetsStream );
        config.setInitTime( c10.initRelTime );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PacketTableConfig
        extends DefaultTableItemsConfig<PacketInfo>
    {
        /**  */
        private long initTime;

        /**
         * 
         */
        public PacketTableConfig()
        {
            super.addCol( "Channel ID", Short.class,
                ( d ) -> d.packet.header.channelId );
            super.addCol( "Data Type", String.class,
                ( d ) -> d.packet.header.dataType.name );
            super.addCol( "Seq #", Integer.class,
                ( d ) -> d.packet.header.sequenceNumber & 0xFF );
            super.addCol( "Time", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString(
                    d.packet.header.relativeTimeCounter ) );
            super.addCol( "RelTime", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString(
                    d.packet.header.relativeTimeCounter - initTime ) );
            super.addCol( "Data Length", Integer.class,
                ( d ) -> d.packet.header.dataLength );
        }

        /**
         * @param time
         */
        public void setInitTime( long time )
        {
            this.initTime = time;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class PacketStream implements IItemStream<PacketInfo>
    {
        /**  */
        private Ch10File c10;

        /**
         * 
         */
        public PacketStream()
        {
            @SuppressWarnings( "resource")
            DataStream stream = new DataStream( new ByteArrayStream( 0 ) );

            this.c10 = new Ch10File( stream );
        }

        /**
         * @param file
         */
        public void setValue( Ch10File file )
        {
            this.c10 = file;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getCount()
        {
            return c10.packets.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<PacketInfo> get( long index, int count )
        {
            List<PacketInfo> packets = new ArrayList<>( count );
            int p = ( int )index;

            for( int i = 0; i < count; i++, p++ )
            {
                PacketInfo info = c10.readPacket( p );
                packets.add( info );
            }

            return packets;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void add( PacketInfo item )
        {
            throw new IllegalStateException( "Unimplemented" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeAll()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<PacketInfo> iterator()
        {
            return new Iterator<PacketInfo>()
            {
                int i = 0;

                @Override
                public PacketInfo next()
                {
                    PacketInfo info = c10.readPacket( i++ );
                    return info;
                }

                @Override
                public boolean hasNext()
                {
                    return i < c10.packets.size();
                }
            };
        }
    }
}
