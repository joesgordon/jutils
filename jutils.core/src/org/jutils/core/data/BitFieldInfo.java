package org.jutils.core.data;

/*******************************************************************************
 * Class for supporting {@link IBitFlag}.
 ******************************************************************************/
public class BitFieldInfo
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
    /** The name of this item. */
    public final String name;

    /***************************************************************************
     * Creates a new structure for supporting {@link IBitFlag}.
     * @param bit the bit to create a mask for.
     * @param name the name of this field/flag.
     **************************************************************************/
    public BitFieldInfo( int bit, String name )
    {
        this( bit, bit, name );
    }

    /***************************************************************************
     * @param startBit the first bit of this field, inclusive.
     * @param endBit the last bit of this field, inclusive.
     * @param name the name of this field/flag.
     **************************************************************************/
    public BitFieldInfo( int startBit, int endBit, String name )
    {
        this.startBit = startBit;
        this.endBit = endBit;
        this.mask = IBitField.generateMask( startBit, endBit );
        this.name = name;
    }
}
