package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Rcc106Version implements INamedValue
{
    /**  */
    RCC_106_07( 7, "RCC 106-07" ),
    /**  */
    RCC_106_09( 8, "RCC 106-09" ),
    /**  */
    RCC_106_11( 9, "RCC 106-11" ),
    /**  */
    RCC_106_13( 10, "RCC 106-13" ),
    /**  */
    RCC_106_15( 11, "RCC 106-15" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private Rcc106Version( int value, String name )
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
     * @param field
     * @return
     **************************************************************************/
    public static Rcc106Version fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
