package jutils.telemetry.ch10;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10Utils
{
    /** 10 MHz */
    public static final long RELTIME_RATE = 10000000;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private Ch10Utils()
    {
    }

    /***************************************************************************
     * @param reltime
     * @return
     **************************************************************************/
    public static String reltimeToSecondsString( long reltime )
    {
        long secs = reltime / RELTIME_RATE;
        long frac = Math.abs( reltime % RELTIME_RATE );

        return String.format( "%d.%07d", secs, frac );
    }
}
