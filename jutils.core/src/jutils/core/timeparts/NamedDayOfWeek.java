package jutils.core.timeparts;

import java.time.DayOfWeek;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum NamedDayOfWeek implements INamedValue
{
    /**  */
    SUNDAY( 7, "U", "Sunday", DayOfWeek.SUNDAY ),
    /**  */
    MONDAY( 1, "M", "Monday", DayOfWeek.MONDAY ),
    /**  */
    TUESDAY( 2, "T", "Tuesday", DayOfWeek.TUESDAY ),
    /**  */
    WEDNESDAY( 3, "W", "Wednesday", DayOfWeek.WEDNESDAY ),
    /**  */
    THURSDAY( 4, "R", "Thursday", DayOfWeek.THURSDAY ),
    /**  */
    FRIDAY( 5, "F", "Friday", DayOfWeek.FRIDAY ),
    /**  */
    SATURDAY( 6, "S", "Saturday", DayOfWeek.SATURDAY ),;

    /**  */
    public final int value;
    /**  */
    public final String abbr;
    /**  */
    public final String name;
    /**  */
    public final DayOfWeek day;

    /***************************************************************************
     * @param day
     * @param name
     * @param abbr
     * @param value
     **************************************************************************/
    private NamedDayOfWeek( int value, String abbr, String name, DayOfWeek day )
    {
        this.value = value;
        this.abbr = abbr;
        this.name = name;
        this.day = DayOfWeek.SUNDAY;
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
}
