package jutils.core.timeparts;

import java.time.Month;

import jutils.core.INamedValue;

/*******************************************************************************
 * Defines an enumeration of Months that are serializable to an integer and an
 * ASCII string.
 ******************************************************************************/
public enum NamedMonth implements INamedValue
{
    /**  */
    JANUARY( 1, "January", Month.JANUARY ),
    /**  */
    FEBRUARY( 2, "February", Month.FEBRUARY ),
    /**  */
    MARCH( 3, "March", Month.MARCH ),
    /**  */
    APRIL( 4, "April", Month.APRIL ),
    /**  */
    MAY( 5, "May", Month.MAY ),
    /**  */
    JUNE( 6, "June", Month.JUNE ),
    /**  */
    JULY( 7, "July", Month.JULY ),
    /**  */
    AUGUST( 8, "August", Month.AUGUST ),
    /**  */
    SEPTEMBER( 9, "September", Month.SEPTEMBER ),
    /**  */
    OCTOBER( 10, "October", Month.OCTOBER ),
    /**  */
    NOVEMBER( 11, "November", Month.NOVEMBER ),
    /**  */
    DECEMBER( 12, "December", Month.DECEMBER );

    /**  */
    public final int value;
    /**  */
    public final String name;
    /**  */
    public final Month month;

    /***************************************************************************
     * @param value
     * @param name
     * @param month
     **************************************************************************/
    private NamedMonth( int value, String name, Month month )
    {
        this.value = value;
        this.name = name;
        this.month = month;
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
     * @param month
     * @return
     **************************************************************************/
    public static NamedMonth fromMonth( Month month )
    {
        NamedMonth item = null;

        for( NamedMonth v : values() )
        {
            if( v.month == month )
            {
                item = v;
                break;
            }
        }

        return item;
    }

    /***************************************************************************
     * @param id
     * @return
     **************************************************************************/
    public static NamedMonth fromValue( int id )
    {
        return INamedValue.fromValue( id, values(), null );
    }
}
