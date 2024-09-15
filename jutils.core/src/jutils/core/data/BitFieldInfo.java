package jutils.core.data;

/*******************************************************************************
 * Class for supporting {@link IBitFlag}.
 ******************************************************************************/
public class BitFieldInfo implements INamedBitField
{
    /** The name of this item. */
    public final String name;
    /**  */
    public final BitField field;

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
        this.name = name;
        this.field = new BitField( startBit, endBit );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return this.name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getStartBit()
    {
        return field.startBit;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getEndBit()
    {
        return field.endBit;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getMask()
    {
        return field.mask;
    }
}
