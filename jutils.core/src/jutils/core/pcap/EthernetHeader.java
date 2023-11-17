package jutils.core.pcap;

import java.io.IOException;

import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EthernetHeader
{
    /**  */
    public final byte [] destination;
    /**  */
    public final byte [] source;
    /**  */
    public short type;

    /***************************************************************************
     * 
     **************************************************************************/
    public EthernetHeader()
    {
        this.destination = new byte[6];
        this.source = new byte[6];
        this.type = 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public EtherType getEtherType()
    {
        return EtherType.fromId( type & 0xFFFF );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class EthernetHeaderSerializer
        implements IDataSerializer<EthernetHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public EthernetHeader read( IDataStream stream ) throws IOException
        {
            EthernetHeader header = new EthernetHeader();

            stream.readFully( header.destination );
            stream.readFully( header.source );
            header.type = stream.readShort();

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( EthernetHeader data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
