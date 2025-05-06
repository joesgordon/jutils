package jutils.telemetry.ch10;

import jutils.core.data.EnumValue;
import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Time1Body implements IPacketBody
{
    /**  */
    public EnumValue<TimeSource> source;
    /**  */
    public DateFormat format;
    /**  */
    public IrigTimeSource irigSource;
    /**  */
    public int reserved;
    /**  */
    public ITime1 time;

    /***************************************************************************
     * 
     **************************************************************************/
    public Time1Body()
    {
        this.source = new EnumValue<>( TimeSource.NONE );
        this.format = DateFormat.IRIG_DAY;
        this.irigSource = IrigTimeSource.NO_SOURCE;
        this.reserved = 0;
        this.time = new IrigDayTime();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Time Source", source );
        printer.printField( "Format", format );
        printer.printField( "IRIG Source", irigSource );
        printer.printField( "Reserved", reserved );
        printer.printTier( "Time", time );
    }
}
