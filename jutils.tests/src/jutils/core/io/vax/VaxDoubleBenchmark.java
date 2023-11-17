package jutils.core.io.vax;

import jutils.core.io.LogUtils;
import jutils.core.time.NanoWatch;
import jutils.core.vax.VaxDouble;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VaxDoubleBenchmark
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        long start = 0;
        long end = start + 200000000L;
        long ans;

        NanoWatch w = new NanoWatch();
        long binaryTime = 0;
        long scaleTime = 0;

        ans = 0L;
        w.start();
        for( long i = start; i < end; i++ )
        {
            ans += i;
            ans = ( long )VaxDouble.calcVaxMantissa( ans );
        }
        binaryTime = w.stop();

        ans = 0L;
        w.start();
        for( long i = start; i < end; i++ )
        {
            ans += i;
            ans = ( long )VaxDouble.calcVaxMantissaScale( ans );
        }
        scaleTime = w.stop();

        long delta = scaleTime - binaryTime;
        long whole = delta / 1000000000L;
        long fract = delta - ( whole * 1000000000L );

        fract *= fract < 0 ? -1L : 1L;

        double p = delta / ( double )end;

        LogUtils.printDebug( "From %d to %d", start, end );
        LogUtils.printDebug( "%d vs %d => %d.%09d (scale-binary) : %f",
            binaryTime, scaleTime, whole, fract, p );
    }
}
