package jutils.telemetry.data.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TimeFormat implements INamedValue
{
    /**  */
    IRIG_B( 0, "IRIG-B" ),
    /**  */
    IRIG_A( 1, "IRIG-A" ),
    /**  */
    IRIG_G( 2, "IRIG-G" ),
    /**  */
    REAL_TIME_CLOCK( 3, "Real-Time Clock" ),
    /**  */
    UTC_GPS( 4, "UTC from GPS" ),
    /**  */
    GPS( 5, "GPS" ),
    /**  */
    PTP( 6, "PTP" ),
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
    private TimeFormat( int value, String name )
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
    public static TimeFormat fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
