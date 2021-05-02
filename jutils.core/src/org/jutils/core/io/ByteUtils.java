package org.jutils.core.io;

/*******************************************************************************
 * Utility class for parsing data from byte arrays.
 ******************************************************************************/
public final class ByteUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private ByteUtils()
    {
    }

    /***************************************************************************
     * Reads a Big Endian integer from the beginning of data provided.
     * @param data the buffer containing the integer to be read.
     * @return the integer read.
     **************************************************************************/
    public static int getInteger( byte [] data )
    {
        return getInteger( data, 0 );
    }

    /***************************************************************************
     * Reads a Big Endian integer from the data provided starting at the
     * provided index.
     * @param data the buffer containing the integer to be read.
     * @param index the location at which the integer will be read.
     * @return the integer read.
     **************************************************************************/
    public static int getInteger( byte [] data, int index )
    {
        int i = 0;

        i |= Byte.toUnsignedInt( data[index + 0] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 1] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 2] );

        i <<= 8;
        i |= Byte.toUnsignedInt( data[index + 3] );

        return i;
    }

    /***************************************************************************
     * Reads a Big Endian short from the beginning of data provided.
     * @param data the buffer containing the short to be read.
     * @return the short read.
     **************************************************************************/
    public static short getShort( byte [] data )
    {
        return getShort( data, 0 );
    }

    /***************************************************************************
     * Reads a Big Endian short from the data provided starting at the provided
     * index.
     * @param data the buffer containing the short to be read.
     * @param index the location at which the short will be read.
     * @return the short read.
     **************************************************************************/
    public static short getShort( byte [] data, int index )
    {
        short s = 0;

        s |= Byte.toUnsignedInt( data[index + 0] );

        s <<= 8;
        s |= Byte.toUnsignedInt( data[index + 1] );

        return s;
    }

    /***************************************************************************
     * Wraps {@link Math#min(int, int)} with byte casting.
     * @param a the one byte to compare.
     * @param b the other byte to compare.
     * @return the minimum unsigned byte value.
     **************************************************************************/
    public static byte minUnsigned( byte a, byte b )
    {
        return ( byte )Math.min( Byte.toUnsignedInt( a ),
            Byte.toUnsignedInt( b ) );
    }

    /***************************************************************************
     * Wraps {@link Math#max(int, int)} with byte casting.
     * @param a the one byte to compare.
     * @param b the other byte to compare.
     * @return the maximum unsigned byte value.
     **************************************************************************/
    public static byte maxUnsigned( byte a, byte b )
    {
        return ( byte )Math.max( Byte.toUnsignedInt( a ),
            Byte.toUnsignedInt( b ) );
    }
}
