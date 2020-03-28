package org.jutils.apps.filespy;

import org.jutils.core.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ByteCharSequence implements CharSequence
{
    /**  */
    private int offset = -1;
    /**  */
    private int length = -1;
    /**  */
    private byte [] buffer = null;

    /***************************************************************************
     * @param buf
     **************************************************************************/
    public ByteCharSequence( byte [] buf )
    {
        this( buf, 0, buf.length );
    }

    /***************************************************************************
     * @param buf
     * @param off
     * @param len
     **************************************************************************/
    public ByteCharSequence( byte [] buf, int off, int len )
    {
        buffer = buf;
        offset = off;
        length = len;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public int length()
    {
        return length;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public char charAt( int index )
    {
        return ( char )buffer[offset + index];
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public CharSequence subSequence( int start, int end )
    {
        return new ByteCharSequence( buffer, offset + start, offset + end );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return new String( buffer, offset, length, IOUtils.get8BitEncoding() );
    }
}
