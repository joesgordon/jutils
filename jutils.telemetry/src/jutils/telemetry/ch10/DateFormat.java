package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum DateFormat implements INamedValue
{
    /**  */
    IRIG_DAY( 0, "IRIG Day" ),
    /**  */
    MONTH_YEAR( 1, "Month/Day" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private DateFormat( int value, String name )
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
    public static DateFormat fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
