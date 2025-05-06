package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IrigTimeSource implements INamedValue
{
    /**  */
    NO_SOURCE( 0, "No time Source" ),
    /**  */
    TIME_CMD( 1, "Time Command" ),
    /**  */
    RMM_TIME( 2, "RMM Time" ),
    /**  */
    IRIG_SIGNAL( 3, "IRIG Signal" ),
    /**  */
    EXTERNAL_GPS( 4, "External GPS" ),
    /**  */
    NTP( 5, "NTP" ),
    /**  */
    PTP( 6, "PTP" ),
    /**  */
    EXTERNAL_EMBEDDED( 7, "External Embedded" ),;

    /**  */
    public final int value;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private IrigTimeSource( int value, String name )
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
    public static IrigTimeSource fromValue( int field )
    {
        return INamedValue.fromValue( field, values(), null );
    }
}
