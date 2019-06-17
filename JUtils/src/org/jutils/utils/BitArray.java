package org.jutils.utils;

import java.util.*;

import org.jutils.io.BitBuffer;
import org.jutils.ui.hex.HexUtils;

/*******************************************************************************
 * Defines an object to hold an array of bits.
 ******************************************************************************/
public class BitArray implements Iterable<Boolean>
{
    /** The bits to be managed. */
    private final List<Boolean> bits;

    /***************************************************************************
     * Creates a new bit array.
     **************************************************************************/
    public BitArray()
    {
        bits = new ArrayList<>( 64 );
    }

    /***************************************************************************
     * Creates a new bit array that is a copy of the provided bit array.
     * @param bits the array to be copied.
     **************************************************************************/
    public BitArray( BitArray bits )
    {
        this.bits = new ArrayList<>( bits.bits );
    }

    /***************************************************************************
     * Creates a new bit array initialized to the provided hexadecimal string.
     * @param hexString the hexadecimal digits to initialize the array to.
     **************************************************************************/
    public BitArray( String hexString ) throws NumberFormatException
    {
        this();

        set( HexUtils.fromHexStringToArray( hexString ) );
    }

    /***************************************************************************
     * Sets the bits in this array to the provided bytes.
     * @param bytes the bytes to set the array to.
     **************************************************************************/
    public void set( byte [] bytes )
    {
        bits.clear();

        BitBuffer buf = new BitBuffer( bytes );

        for( int i = 0; i < buf.bitCount(); i++ )
        {
            bits.add( buf.readBit() );
        }
    }

    /***************************************************************************
     * Sets the array to the provided 1's and 0's ignoring spaces.
     * @param binaryString the string of 1's and 0's.
     * @throws NumberFormatException if anything beside a 1,0, or space is in
     * the string.
     **************************************************************************/
    public void set( String binaryString ) throws NumberFormatException
    {
        bits.clear();

        for( int i = 0; i < binaryString.length(); i++ )
        {
            char c = binaryString.charAt( i );

            if( c == '0' )
            {
                bits.add( false );
            }
            else if( c == '1' )
            {
                bits.add( true );
            }
            else if( c == ' ' )
            {
                continue;
            }
            else
            {
                throw new NumberFormatException( "Invalid binary character '" +
                    c + "' found at index " + i );
            }
        }
    }

    /***************************************************************************
     * Returns a byte array of the bits shifted to the left byte boundary and
     * padding zeros on the right.
     * @return the left-aligned byte array.
     **************************************************************************/
    public byte [] getLeftAligned()
    {
        int byteCount = ( bits.size() + 7 ) / 8;
        byte [] bytes = new byte[byteCount];
        if( byteCount > 0 )
        {
            BitBuffer buf = new BitBuffer( bytes );

            for( Boolean b : bits )
            {
                buf.writeBit( b );
            }
        }

        return bytes;
    }

    /***************************************************************************
     * Returns a byte array of the bits shifted to the right byte boundary and
     * padding zeros on the left.
     * @return the right-aligned byte array.
     **************************************************************************/
    public byte [] getRightAligned()
    {
        int byteCount = ( bits.size() + 7 ) / 8;
        byte [] bytes = new byte[byteCount];

        if( byteCount > 0 )
        {
            BitBuffer buf = new BitBuffer( bytes );
            int bit = ( 8 - bits.size() % 8 ) % 8;

            buf.setPosition( 0, bit );

            for( Boolean b : bits )
            {
                buf.writeBit( b );
            }
        }

        return bytes;
    }

    /***************************************************************************
     * Returns the number of bits in this array.
     * @return the number of bits in this array.
     **************************************************************************/
    public int size()
    {
        return bits.size();
    }

    /***************************************************************************
     * Returns an iterator that returns each bit as a boolean.
     **************************************************************************/
    @Override
    public Iterator<Boolean> iterator()
    {
        return bits.iterator();
    }

    /***************************************************************************
     * Returns the bit at the provided index as a boolean.
     * @param idx the index of the bit.
     * @return the bit.
     **************************************************************************/
    public boolean get( int idx )
    {
        return bits.get( idx );
    }

    /***************************************************************************
     * Sets this array to the bits in the provided array.
     * @param bits the array to set this array to.
     **************************************************************************/
    public void set( BitArray bits )
    {
        this.bits.clear();
        this.bits.addAll( bits.bits );
    }

    /***************************************************************************
     * Returns a binary representation of this collection of bits.
     **************************************************************************/
    @Override
    public String toString()
    {
        char [] str = new char[bits.size()];

        for( int i = 0; i < str.length; i++ )
        {
            Boolean b = bits.get( i );
            str[i] = b ? '1' : '0';
        }

        return new String( str );
    }

    /***************************************************************************
     * Returns a hexadecimal string representation of the left-aligned bits.
     * @return the left-aligned hexadecimal string.
     **************************************************************************/
    public String toHexLeft()
    {
        return HexUtils.toHexString( getLeftAligned() );
    }

    /***************************************************************************
     * Returns a hexadecimal string representation of the right-aligned bits.
     * @return the right-aligned hexadecimal string.
     **************************************************************************/
    public String toHexRight()
    {
        return HexUtils.toHexString( getRightAligned() );
    }

    /***************************************************************************
     * Returns a short description of the bits.
     * @return the description of the bits.
     **************************************************************************/
    public String getDescription()
    {
        return String.format( "%s (<- = 0x%s) (-> = 0x%s)", toString(),
            toHexLeft(), toHexRight() );
    }
}
