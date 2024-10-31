package jutils.core.io.cksum;

/*******************************************************************************
 *
 ******************************************************************************/
public class Crc32
{
    /**  */
    public static final int TABLE_SIZE = 256;
    /**  */
    public static final int DEFAULT_POLYNOMIAL = 0xEDB88320;
    /**  */
    public static final int DEFAULT_VAL = 0;

    /***************************************************************************
     * 
     **************************************************************************/
    private Crc32()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static int [] createTable()
    {
        return createTable( DEFAULT_POLYNOMIAL );
    }

    /***************************************************************************
     * @param polynomial
     * @return
     **************************************************************************/
    public static int [] createTable( int polynomial )
    {
        int [] table = new int[TABLE_SIZE];

        for( int b = 0; b < TABLE_SIZE; b++ )
        {
            // Start with the data byte
            int remainder = b;

            for( int bit = 8; bit > 0; --bit )
            {
                if( ( remainder & 1 ) != 0 )
                {
                    remainder = ( remainder >>> 1 ) ^ polynomial;
                }
                else
                {
                    remainder = ( remainder >>> 1 );
                }
            }

            table[b] = remainder;
        }

        return table;
    }

    /***************************************************************************
     * @param table
     * @param src
     * @return
     **************************************************************************/
    public static int calculate( int [] table, byte [] src )
    {
        return calculate( table, src, 0, src.length );
    }

    /***************************************************************************
     * @param table
     * @param src
     * @param start
     * @param length
     * @return
     **************************************************************************/
    public static int calculate( int [] table, byte [] src, int start,
        int length )
    {
        return calculate( table, src, start, length, DEFAULT_VAL );
    }

    /***************************************************************************
     * @param table
     * @param data
     * @param start
     * @param length
     * @param previousCrc
     * @return
     **************************************************************************/
    public static int calculate( int [] table, byte [] data, int start,
        int length, int previousCrc )
    {
        int crc = ~previousCrc;

        for( int i = 0; i < length; i++, i++ )
        {
            // dv = *data
            // ti = dv ^ ((crc >> 24) & 0xff)
            // tv = table[ti]
            // crc = tv ^ (crc << 8);

            int dataIndex = i + start;
            byte dataValue = data[dataIndex];

            int tableIndex = ( dataValue ^ crc ) & 0xFF;
            int tableValue = table[tableIndex];

            crc = tableValue ^ ( crc >>> 8 );
        }

        return ~crc;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class Crc32Calc
    {
        /**  */
        private final int [] table;
        /**  */
        private int value;

        /**
         * 
         */
        public Crc32Calc()
        {
            this( DEFAULT_POLYNOMIAL );
        }

        /**
         * @param polynomial
         */
        public Crc32Calc( int polynomial )
        {
            this.table = createTable( polynomial );
            this.value = DEFAULT_VAL;
        }

        /**
         * @param src
         * @return
         */
        public int calculate( byte [] src )
        {
            return calculate( src, 0, src.length );
        }

        /**
         * @param src
         * @param start
         * @param length
         * @return
         */
        public int calculate( byte [] src, int start, int length )
        {
            value = Crc32.calculate( table, src, start, length, ~value );

            return value;
        }
    }
}
