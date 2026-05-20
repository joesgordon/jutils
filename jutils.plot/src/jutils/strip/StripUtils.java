package jutils.strip;

import jutils.strip.data.AxisTicks;
import jutils.strip.data.Range;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StripUtils
{
    /**  */
    public static final int TICK_MIN = 30;
    /**  */
    public static final int TICK_MAX = 150;
    /**  */
    public static final int BG_DEFAULT = 0x333333;
    /**  */
    public static final int TICK_DEFAULT = 0x555555;
    /**  */
    public static final int FG_DEFAULT = 0xdddddd;

    /***************************************************************************
     * @param axis the axis for which ticks and bounds will be calculated.
     * @param range the range of the data for this axis.
     * @param length the size of this axis in pixels.
     **************************************************************************/
    public static void calcTicks( AxisTicks axis, Range range, int length )
    {
        int maxTicks = ( int )Math.ceil(
            length / ( double )StripUtils.TICK_MIN );
        int minTicks = ( int )Math.floor(
            length / ( double )StripUtils.TICK_MAX );
        double dataRange = range.getRange();

        double rlog = Math.log10( dataRange );
        int order = ( int )Math.floor( rlog );

        // LogUtils.printDebug( "%s, %d", metrics, length, rlog, order );
        // LogUtils.printDebug(
        // "=> minTicks: %d, maxTicks: %d rlog: %f, order: %d", minTicks,
        // maxTicks, rlog, order );

        double div = calcTicksDivisor( dataRange, order, minTicks, maxTicks );

        if( div < 0 )
        {
            order += 1;
            div = calcTicksDivisor( dataRange, order, minTicks, maxTicks );

            if( div < 0 )
            {
                String msg = String.format(
                    "The maths have failed me!!: %s, over %d pixels", range,
                    length );
                throw new IllegalStateException( msg );
            }
        }

        int startTc = ( int )Math.floor( range.min * div );
        int endTc = ( int )Math.ceil( range.max * div );

        // LogUtils.printDebug( "=> startTc: %d, endTc: %d", startTc, endTc
        // );

        double start = startTc / div;
        double end = endTc / div;
        int count = endTc - startTc;
        double width = ( end - start ) / ( count );

        axis.minValue = start;
        axis.tickWidth = width;
        axis.sectionCount = count;

        // LogUtils.printDebug(
        // "=> start: %f, end: %f, range: %f, count: %d, width: %f", start,
        // end, end - start, count, width );
        //
        // double twpx = length / ( double )( count - 1 );
        //
        // LogUtils.printDebug( "=> twpx: %f px", twpx );
    }

    /***************************************************************************
     * @param range the range of this axis.
     * @param order the order of the range of this axis.
     * @param minTicks the minimum number of ticks for this axis.
     * @param maxTicks the maximum number of ticks for this axis.
     * @return the divisor calculated or {@code -1} if none valid.
     **************************************************************************/
    private static double calcTicksDivisor( double range, int order,
        int minTicks, int maxTicks )
    {
        double powder = range / Math.pow( 10, order );

        int tc01 = ( int )Math.ceil( powder );
        int tc02 = ( int )Math.ceil( 2.0 * powder );
        int tc05 = ( int )Math.ceil( 5.0 * powder );
        int tc10 = ( int )Math.ceil( 10.0 * powder );

        // LogUtils.printDebug( "=> tc01: %d, tc02: %d, tc05: %d, tc10: %d",
        // tc01, tc02, tc05, tc10 );

        double div = Math.pow( 10, -order );
        if( minTicks <= tc10 && tc10 <= maxTicks )
        {
            div *= 10.0;
        }
        else if( minTicks <= tc05 && tc05 <= maxTicks )
        {
            div *= 5.0;
        }
        else if( minTicks <= tc02 && tc02 <= maxTicks )
        {
            div *= 2.0;
        }
        else if( minTicks <= tc01 && tc01 <= maxTicks )
        {
            div *= 1.0;
        }
        else
        {
            div = -1;
        }

        return div;
    }
}
