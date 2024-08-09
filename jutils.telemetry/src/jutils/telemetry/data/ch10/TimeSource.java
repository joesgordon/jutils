package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TimeSource implements INamedValue
{
    /**  */
    INTERNAL( 0, "Internal" ),
    /**  */
    EXTERNAL( 1, "External" ),
    /**  */
    INTERNAL_RMM( 2, "Internal from RMM" ),
    /**  */
    NONE( 15, "None" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private TimeSource( int value, String name )
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
    public static TimeSource fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
