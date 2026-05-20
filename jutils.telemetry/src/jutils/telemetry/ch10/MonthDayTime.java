package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonthDayTime implements ITime1
{
    /**  */
    public int milliseconds;
    /**  */
    public int seconds;
    /**  */
    public int minutes;
    /**  */
    public int hours;
    /**  */
    public int dayofMonth;
    /**  */
    public int month;
    /**  */
    public int year;

    /***************************************************************************
     * 
     **************************************************************************/
    public MonthDayTime()
    {
        this.milliseconds = 0;
        this.seconds = 0;
        this.minutes = 0;
        this.hours = 0;
        this.dayofMonth = 0;
        this.month = 0;
        this.year = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Year", year );
        printer.printField( "Month", month );
        printer.printField( "Day of Month", dayofMonth );
        printer.printField( "Hours", hours );
        printer.printField( "Minute", minutes );
        printer.printField( "Seconds", seconds );
        printer.printField( "Milliseconds", milliseconds );
    }
}
