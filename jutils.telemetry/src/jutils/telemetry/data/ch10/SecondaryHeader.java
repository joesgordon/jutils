package jutils.telemetry.data.ch10;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * As defined in chapter 11 section 11.2.1.2.
 ******************************************************************************/
public class SecondaryHeader implements ITierPrinter
{
    /**  */
    public static final int SIZE = 8;

    /**  */
    public ISecondaryTime time;
    /**  */
    public short reserved;
    /**  */
    public short checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public SecondaryHeader()
    {
        this.time = new ErtcTime();
        this.reserved = 0;
        this.checksum = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "Time", time );
        printer.printHexField( "Reserved", reserved );
        printer.printHexField( "Checksum", checksum );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ISecondaryTime extends ITierPrinter
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Chapter4Time implements ISecondaryTime
    {
        /**  */
        public short highOrderTime;
        /**  */
        public short lowOrderTime;
        /**  */
        public short microseconds;
        /**  */
        public short reserved;

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "High Order Time", highOrderTime );
            printer.printField( "Low Order Time", lowOrderTime );
            printer.printField( "Microseconds", microseconds );
            printer.printField( "Reserved", reserved );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class Ieee1588Time implements ISecondaryTime
    {
        /**  */
        public int seconds;
        /**  */
        public int nanoseconds;

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "Seconds", seconds );
            printer.printField( "Nanoseconds", nanoseconds );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class ErtcTime implements ISecondaryTime
    {
        /**  */
        public long nanoseconds;

        /**
         * {@inheritDoc}
         */
        @Override
        public void printFields( FieldPrinter printer )
        {
            printer.printField( "Nanoseconds", nanoseconds );
        }
    }
}
