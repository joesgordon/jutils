package org.jutils.time;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TimeOfDayPrinter
{
    /**  */
    private static final long MICROS_IN_HOUR = TimeUtils.MILLIS_IN_HOUR * 1000;
    /**  */
    private static final long MICROS_IN_MIN = TimeUtils.MILLIS_IN_MIN * 1000;

    /**  */
    private final char [] buffer;

    /***************************************************************************
     * 
     **************************************************************************/
    public TimeOfDayPrinter()
    {
        this.buffer = new char["XX:XX:XX.XXXXXX".length()];

        buffer[2] = ':';
        buffer[5] = ':';
        buffer[8] = '.';
    }

    // TODO write instance function that uses buffer

    /***************************************************************************
     * @param microseconds
     * @return
     **************************************************************************/
    public static String toString( long microsIntoDay )
    {
        long us = microsIntoDay;
        long h;
        long m;
        long s;
        long f;

        h = us / MICROS_IN_HOUR;
        us = us - h * MICROS_IN_HOUR;

        m = us / MICROS_IN_MIN;
        us = us - m * MICROS_IN_MIN;

        s = us / 1000000;
        us = us - s * 1000000;

        f = us;

        return String.format( "%02d:%02d:%02d.%06d", h, m, s, f );
    }
}
