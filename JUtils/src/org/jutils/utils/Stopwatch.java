package org.jutils.utils;

import java.util.Date;

/*******************************************************************************
 * Defines a class to time tasks.
 ******************************************************************************/
public class Stopwatch
{
    /** The start time in milliseconds since epoch. */
    private long startTime;
    /**  */
    private long elapsed;

    /**  */
    private WatchState state;

    /***************************************************************************
     * Creates a new, unhacked, stopwatch.
     **************************************************************************/
    public Stopwatch()
    {
        startTime = 0;

        start();
    }

    /***************************************************************************
     * Hacks the current system time to start the watch.
     * @return the current system time.
     **************************************************************************/
    public long start()
    {
        return start( System.currentTimeMillis() );
    }

    public long start( long now )
    {
        this.startTime = now;
        this.elapsed = 0;
        this.state = WatchState.STARTED;

        return startTime;
    }

    /***************************************************************************
     * Hacks the current system time to stop the watch.
     * @return the current system time.
     **************************************************************************/
    public long stop()
    {
        long now = System.currentTimeMillis();
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
        long now = System.currentTimeMillis();

        pauseResume( now );
    }

    public void pauseResume( long now )
    {
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
    public Date getElapsedDate()
    {
        return new Date( getElapsed() );
    }

    /***************************************************************************
     * @return the elapsed time in milliseconds
     **************************************************************************/
    public long getElapsed()
    {
        long now = System.currentTimeMillis();

        return getElapsed( now );
    }

    /***************************************************************************
     * @param now
     * @return the elapsed time in milliseconds
     **************************************************************************/
    public long getElapsed( long now )
    {
        long finalTime = elapsed;

        if( state == WatchState.STARTED )
        {
            finalTime += now - startTime;
        }

        return finalTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private enum WatchState
    {
        STOPPED,
        STARTED,
        PAUSED;
    }
}
