package jutils.core.concurrent;

import jutils.core.io.LogUtils;

/*******************************************************************************
 * Defines a method of triggering (on average) at a given rate based on a higher
 * rate.
 ******************************************************************************/
public class RateTrigger
{
    /** The base rate used in the default constructor. */
    public static final float DEFAULT_BASE = 50.0f;
    /** The trigger rate used in the default constructor. */
    public static final float DEFAULT_TRIGGER = 10.0f;

    /** The fractional number of seconds between checks. */
    private float basePeriod;
    /** The accuracy of the check in seconds (half the {@link #basePeriod}). */
    private float threshold;

    /** The number of seconds between triggers. */
    private float triggerPeriod;
    /** * The number of seconds since the last trigger (accumulating). */
    private float period;

    /***************************************************************************
     * Creates a new rate trigger with a base rate of {@link #DEFAULT_BASE} and
     * trigger rate of {@link #DEFAULT_TRIGGER}. The {@link #check()} function
     * will return {@code true} the first time it is called.
     **************************************************************************/
    public RateTrigger()
    {
        this( 50, 10 );
    }

    /***************************************************************************
     * Creates a new rate trigger with the provided base and trigger rates. The
     * {@link #check()} function will return {@code true} the first time it is
     * called.
     * @param baseRate the rate at which {@link #check()} will be called in Hz.
     * @param triggerRate the rate at which it is desired that {@link #check()}
     * will return true in Hz.
     **************************************************************************/
    public RateTrigger( float baseRate, float triggerRate )
    {
        set( baseRate, triggerRate );
    }

    /***************************************************************************
     * Tests to see if it is time to trigger. This function should be called at
     * the {@code base} rate.
     * @return {@code true} if the desired rate is triggered; {@code false}
     * otherwise.
     **************************************************************************/
    public boolean check()
    {
        boolean trigger = false;

        if( ( period + threshold ) >= triggerPeriod )
        {
            trigger = true;
            period -= triggerPeriod;
        }

        period += basePeriod;

        return trigger;
    }

    /***************************************************************************
     * Resets this trigger so any accumulation is nulled and the next call to
     * {@link #check()} will return {@code true}.
     **************************************************************************/
    public void reset()
    {
        this.period = triggerPeriod;
    }

    /***************************************************************************
     * Sets this trigger to signal at the provided {@code triggerRate} using the
     * provided {@code baseRate}.
     * @param baseRate the rate at which {@link #check()} will be called in Hz.
     * @param triggerRate the rate at which it is desired that {@link #check()}
     * will return true in Hz.
     **************************************************************************/
    public void set( float baseRate, float triggerRate )
    {
        this.basePeriod = 1.0f / baseRate;
        this.threshold = basePeriod / 2.0f;
        this.triggerPeriod = 1.0f / triggerRate;

        this.period = triggerPeriod;
    }

    /***************************************************************************
     * @param args not used
     **************************************************************************/
    public static void main( String [] args )
    {
        float baseRate = 10;
        float triggerRate = 9;
        RateTrigger trigger = new RateTrigger( baseRate, triggerRate );

        float time = 0.0f;
        int testCount = 1005;
        int triggeredCount = 0;

        for( int i = 0; i < testCount; i++ )
        {
            boolean triggered = trigger.check();

            triggeredCount += triggered ? 1 : 0;

            LogUtils.print( "%.2f: %s", time, triggered ? "Y" : "N" );

            time += trigger.basePeriod;
        }

        float averageRate = triggeredCount / ( trigger.basePeriod * testCount );

        LogUtils.print( "\nAverage rate is %.2f/%.2f over %d/%d iterations",
            averageRate, baseRate, triggeredCount, testCount );
    }
}
