package jutils.telemetry.ui.ch10;

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
import jutils.telemetry.data.ch10.Ch10File;
import jutils.telemetry.data.ch10.Ch10Utils;
import jutils.telemetry.data.ch10.Packet;
import jutils.telemetry.data.ch10.PacketInfo;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketsView implements IDataView<Ch10File>
{
    /**  */
    private final PaginatedTableView<Packet> table;
    /**  */
    private final PacketTableConfig config;
    /**  */
    private final PacketStream packetsStream;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketsView()
    {
        this.packetsStream = new PacketStream();
        this.config = new PacketTableConfig();
        this.table = new PaginatedTableView<>( config, packetsStream,
            new PacketView() );

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
        extends DefaultTableItemsConfig<Packet>
    {
        /**  */
        private long initTime;

        /**
         * 
         */
        public PacketTableConfig()
        {
            super.addCol( "Channel ID", Short.class,
                ( d ) -> d.header.channelId );
            super.addCol( "Data Type", String.class,
                ( d ) -> d.header.dataType.name );
            super.addCol( "Seq #", Integer.class,
                ( d ) -> d.header.sequenceNumber & 0xFF );
            super.addCol( "Time", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString(
                    d.header.relativeTimeCounter ) );
            super.addCol( "RelTime", String.class,
                ( d ) -> Ch10Utils.reltimeToSecondsString(
                    d.header.relativeTimeCounter - initTime ) );
            super.addCol( "Data Length", Integer.class,
                ( d ) -> d.header.dataLength );
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
    private static final class PacketStream implements IItemStream<Packet>
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
        public List<Packet> get( long index, int count )
        {
            List<Packet> packets = new ArrayList<>( count );
            int p = ( int )index;

            for( int i = 0; i < count; i++, p++ )
            {
                PacketInfo info = c10.readPacket( p );
                Packet packet = info.packet;
                packets.add( packet );
            }

            return packets;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void add( Packet item )
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
        public Iterator<Packet> iterator()
        {
            return new Iterator<Packet>()
            {
                int i = 0;

                @Override
                public Packet next()
                {
                    PacketInfo info = c10.readPacket( i++ );
                    return info.packet;
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
