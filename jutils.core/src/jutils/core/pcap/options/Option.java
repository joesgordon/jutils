package jutils.core.pcap.options;

import java.io.IOException;

import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Option
{
    /**  */
    public int type;
    /**  */
    public int length;
    /**  */
    public byte [] value;

    /***************************************************************************
     * 
     **************************************************************************/
    public Option()
    {
        this.type = -1;
        this.length = 0;
        this.value = new byte[0];
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    public static final int getTotalLength( int length )
    {
        return 4 * ( ( length + 3 ) / 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class OptionSerializer implements IDataSerializer<Option>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Option read( IDataStream stream ) throws IOException
        {
            Option option = new Option();

            option.type = stream.readShort() & 0xFFFF;
            option.length = stream.readShort() & 0xFFFF;

            int len = getTotalLength( option.length );

            option.value = new byte[len];

            if( option.value.length > 0 )
            {
                stream.readFully( option.value );
            }

            return option;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Option data, IDataStream stream ) throws IOException
        {
            // TODO Auto-generated method stub
            throw new IllegalStateException( "Not implemented" );
        }
    }
}
