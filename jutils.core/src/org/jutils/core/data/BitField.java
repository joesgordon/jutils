package org.jutils.core.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitField implements IBitField
{
    /** The 0-relative start bit this flag refers to, inclusive. */
    public final int startBit;
    /** The 0-relative end bit this flag refers to, inclusive. */
    public final int endBit;
    /**
     * The mask created by shifting the value 0x01, {@link #bit} number of times
     * to the left
     */
    public final long mask;

    /***************************************************************************
     * @param startBit the 0-relative start bit this flag refers to, inclusive.
     * @param endBit the 0-relative end bit this flag refers to, inclusive.
     **************************************************************************/
    public BitField( int startBit, int endBit )
    {
        this.startBit = startBit;
        this.endBit = endBit;
        this.mask = IBitField.generateMask( startBit, endBit );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getStartBit()
    {
        return this.startBit;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getEndBit()
    {
        return this.endBit;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getMask()
    {
        return mask;
    }

    /***************************************************************************
     * @param startBit
     * @param bitCount
     * @return
     **************************************************************************/
    public static BitField createSized( int startBit, int bitCount )
    {
        return new BitField( startBit, startBit + bitCount - 1 );
    }
}
