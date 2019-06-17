package org.jutils.io.bits;

import org.jutils.io.BitBuffer;

/*******************************************************************************
 * This bit shifter caches all ways of shifting bytes across byte boundaries and
 * provides a source/destination bit agnostic method of shifting.
 ******************************************************************************/
public class BitShifter
{
    /** The cache of all ways of shifting bytes. */
    private final BitShifterFactory factory;

    /***************************************************************************
     * Creates a new shifter.
     **************************************************************************/
    public BitShifter()
    {
        this.factory = new BitShifterFactory();
    }

    /***************************************************************************
     * Writes the specified number of bits from the source to the destination.
     * @param src the buffer containing the bits to be written; positioned at
     * the first bit to be read.
     * @param dst the buffer to which the bits will be written; positioned at
     * the first bit to be written.
     * @param bitCount the number of bits to be written.
     **************************************************************************/
    public void write( BitBuffer src, BitBuffer dst, int bitCount )
    {
        int byteCnt = bitCount / 8;
        int bitsRemaining = bitCount;

        if( byteCnt > 10 )
        {
            bitsRemaining = bitCount - ( 8 * byteCnt );

            IBitShifter shifter = factory.getShifter( src.getBit(),
                dst.getBit() );

            shifter.shift( src, dst, byteCnt );
        }

        if( bitsRemaining > 0 )
        {
            src.writeTo( dst, bitsRemaining );
        }
    }
}
