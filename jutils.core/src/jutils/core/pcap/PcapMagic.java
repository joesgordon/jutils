package jutils.core.pcap;

import jutils.core.INamedValue;
import jutils.core.swap.ByteSwapper;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum PcapMagic implements INamedValue
{
    /**  */
    MAGIC_MICROS( 0xA1B2C3D4, "Magic Number (Microseconds)" ),
    /**  */
    MAGIC_NANOS( 0xA1B23C4D, "Magic Number (Nanoseconds)" );

    /**  */
    public final int value;
    /**  */
    public final int swappedValue;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private PcapMagic( int value, String name )
    {
        this.value = value;
        this.swappedValue = ByteSwapper.swap( value );
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * Returns the magic number matching the unswapped value.
     * @param value the integer representation of the enumerated value.
     * @return the matching magic number for the value.
     **************************************************************************/
    public static PcapMagic fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }
}
