package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Format1Type implements INamedValue
{
    /**  */
    FULL( 0x00, "Full Packets" ),
    /**  */
    SEGMENTED( 0x01, "Segmented Packets" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private Format1Type( int value, String name )
    {
        this.value = value;
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
     * @param value
     * @return
     **************************************************************************/
    public static Format1Type fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static Format1Type fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte getByteValue()
    {
        return ( byte )value;
    }
}
