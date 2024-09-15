package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum DataTypeVersion implements INamedValue
{
    /**  */
    RESERVED( 0x00, "Reserved" ),
    /** Initial Release */
    RCC_106_04( 0x01, "RCC 106-04" ),
    /**  */
    RCC_106_05( 0x02, "RCC 106-05" ),
    /**  */
    RCC_106_07( 0x03, "RCC 106-07" ),
    /**  */
    RCC_106_09( 0x04, "RCC 106-09" ),
    /**  */
    RCC_106_11( 0x05, "RCC 106-11" ),
    /**  */
    RCC_106_13( 0x06, "RCC 106-13" ),
    /**  */
    RCC_106_15( 0x07, "RCC 106-15" ),
    /**  */
    RCC_106_17( 0x08, "RCC 106-17" ),
    /**  */
    RCC_106_19( 0x09, "RCC 106-19" ),
    /**  */
    RCC_106_22( 0x0A, "RCC 106-22" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private DataTypeVersion( int value, String name )
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
    public static DataTypeVersion fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static DataTypeVersion fromValue( int value )
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
