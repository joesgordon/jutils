package org.jutils.core.time;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NanoWatch
{
    /**
     * The start time in nanoseconds as returned by {@link System#nanoTime()}.
     */
    private long startTime;
    /** The elapsed time in nanoseconds. */
    private long elapsed;

    /**  */
    private WatchState state;

    /***************************************************************************
     * Creates a new, unhacked, stopwatch.
     **************************************************************************/
    public NanoWatch()
    {
        startTime = 0;

        start();
    }

    /***************************************************************************
     * Hacks the current system time to start the watch.
     * @return the current system time in nanoseconds.
     **************************************************************************/
    public long start()
    {
        this.startTime = System.nanoTime();
        this.elapsed = 0;
        this.state = WatchState.STARTED;

        return startTime;
    }

    /***************************************************************************
     * Hacks the current system time to stop the watch.
     * @return the current system time in nanoseconds.
     **************************************************************************/
    public long stop()
    {
        long now = System.nanoTime();
        long tempElapsed = now - startTime;

        this.elapsed += tempElapsed;
        this.state = WatchState.STOPPED;

        return now;
    }

    /***************************************************************************
     * Pauses the watch if started, resumes the watch if paused, and does
     * nothing if stopped.
     **************************************************************************/
    public void pauseResume()
    {
        long now = System.nanoTime();

        long tempElapsed = now - startTime;

        switch( state )
        {
            case STARTED:
                elapsed += tempElapsed;
                startTime = 0;
                state = WatchState.PAUSED;
                break;
            case STOPPED:
                break;
            case PAUSED:
                startTime = now;
                state = WatchState.STARTED;
                break;
        }
    }

    /***************************************************************************
     * @return {@code true} if the watch is started and not paused;
     * {@code false} otherwise.
     **************************************************************************/
    public boolean isStarted()
    {
        return state == WatchState.STARTED;
    }

    /***************************************************************************
     * @return {@code true} if the watch is stopped; {@code false} otherwise.
     **************************************************************************/
    public boolean isStopped()
    {
        return state == WatchState.STOPPED;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isPaused()
    {
        return state == WatchState.PAUSED;
    }

    /***************************************************************************
     * Returns the elapsed time in a <code>Date</code> object.
     * @return the elapsed time in a <code>Date</code> object.
     **************************************************************************/
    public Duration getElapsedDate()
    {
        Duration d = Duration.of( getElapsed(), ChronoUnit.NANOS );

        return d;
    }

    /***************************************************************************
     * @return the elapsed time in nanoseconds
     **************************************************************************/
    public long getElapsed()
    {
        long finalTime = elapsed;

        if( state == WatchState.STARTED )
        {
            long now = System.nanoTime();
            finalTime += now - startTime;
        }

        return finalTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private enum WatchState
    {
        /**  */
        STOPPED,
        /**  */
        STARTED,
        /**  */
        PAUSED;
    }
}
