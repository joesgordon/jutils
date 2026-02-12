package jutils.core.swap;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteSwapper
{
    /**  */
    private static final int [] E16 = new int[] { 1 };
    /**  */
    private static final int [] E32 = new int[] { 3, 1 };
    /**  */
    private static final int [] E64 = new int[] { 7, 5, 3, 1 };
    /**  */
    private static final int [] W16 = new int[] { 2, 2 };
    /**  */
    private static final int [] W32 = new int[] { 4, 4, 4, 4 };

    /***************************************************************************
     * @param pattern
     * @param data
     **************************************************************************/
    public static void swap( SwapPattern pattern, byte [] data )
    {
        swap( pattern, data, 0, data.length );
    }

    /***************************************************************************
     * @param pattern
     * @param data
     * @param start
     * @param length
     **************************************************************************/
    public static void swap( SwapPattern pattern, byte [] data, int start,
        int length )
    {
        switch( pattern )
        {
            case NONE:
                break;

            case ENDIAN16:
                swap( E16, data, start, length );
                break;
            case ENDIAN32:
                swap( E32, data, start, length );
                break;
            case ENDIAN64:
                swap( E64, data, start, length );
                break;

            case WORD16:
                swap( W16, data, start, length );
                break;
            case WORD32:
                swap( W32, data, start, length );
                break;
        }
    }

    /***************************************************************************
     * @param pattern
     * @param data
     * @param start
     * @param length
     **************************************************************************/
    private static void swap( int [] pattern, byte [] data, int start,
        int length )
    {
        int size = 2 * pattern.length;
        int end = start + length;
        for( int i = start, j = start + size - 1; i < end &&
            j < end; i += size, j += size )
        {
            for( int b = 0; b < pattern.length; b++ )
            {
                byte d = data[i];
                data[i] = data[j];
                data[j] = d;
            }
        }
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static int swap( int value )
    {
        int swapped = 0;

        swapped |= ( value >> 24 ) & 0x000000FF;
        swapped |= ( value >> 8 ) & 0x0000FF00;
        swapped |= ( value << 8 ) & 0x00FF0000;
        swapped |= ( value << 24 ) & 0xFF000000;

        return swapped;
    }
}
