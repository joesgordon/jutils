package jutils.core.timeparts;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DayOfWeekPart implements ITimePart
{
    /**  */
    public DayOfWeek day;

    /***************************************************************************
     * 
     **************************************************************************/
    public DayOfWeekPart()
    {
        this.day = DayOfWeek.SUNDAY;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Day of Week", getDescription() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime updateDateTime( LocalDateTime time )
    {
        return time.with( day );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void fromDateTime( LocalDateTime time )
    {
        this.day = time.getDayOfWeek();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private String getDescription()
    {
        switch( day )
        {
            case SUNDAY:
                return "Sunday";

            case MONDAY:
                return "Monday";

            case TUESDAY:
                return "Tuesday";

            case WEDNESDAY:
                return "Wednesday";

            case THURSDAY:
                return "Thursday";

            case FRIDAY:
                return "Friday";

            case SATURDAY:
                return "Saturday";
        }

        throw new NullPointerException(
            "Unable to get description for null day of week" );
    }
}
