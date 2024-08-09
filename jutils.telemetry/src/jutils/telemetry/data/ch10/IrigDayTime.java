package jutils.telemetry.data.ch10;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrigDayTime implements ITime1
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
    public int days;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Days", days );
        printer.printField( "Hours", hours );
        printer.printField( "Minute", minutes );
        printer.printField( "Seconds", seconds );
        printer.printField( "Milliseconds", milliseconds );
    }
}
