package jutils.core.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jutils.core.time.NanoWatch;

/*******************************************************************************
 * Defines a task that runs at a given rate. This object may be reused/restarted
 * after calling {@link #reset()}.
 ******************************************************************************/
public class ScheduledTask
{
    /** The rate at which tasks are executed if none provided. */
    public static final double DEFAULT_RATE = 10.0;

    /** The task to be executed at the provided rate. */
    private final IPeriodic task;
    /** The rate at which the task is run. */
    private final double rate;
    /** The period between task executions in nanoseconds. */
    private final long period;
    /** Timer to determine run duration. */
    private final NanoWatch runTimer;

    /** Scheduler used to start/stop the task. */
    private ScheduledExecutorService scheduler;
    /** Future used to cancel the task. */
    private ScheduledFuture<?> future;
    /** Number of times the task has been executed. */
    private long count;

    /***************************************************************************
     * Creates a scheduled task at {@link #DEFAULT_RATE}.
     * @param task the callback to be executed.
     **************************************************************************/
    public ScheduledTask( IPeriodic task )
    {
        this( DEFAULT_RATE, task );
    }

    /***************************************************************************
     * Creates a scheduled task using the provided rate.
     * @param rate rate at which the task is run.
     * @param task the callback to be executed.
     **************************************************************************/
    public ScheduledTask( double rate, IPeriodic task )
    {
        this.task = task;
        this.rate = rate;
        this.period = ( long )( 0.5 + 1e9 / rate );
        this.runTimer = new NanoWatch();

        this.scheduler = null;
        this.future = null;
        this.count = 0;
    }

    /***************************************************************************
     * Starts executing the task.
     * @return {@code false} if already started; {@code true} otherwise.
     **************************************************************************/
    public synchronized boolean start()
    {
        if( future == null )
        {
            this.count = 0;
            this.scheduler = Executors.newScheduledThreadPool( 1 );
            this.future = scheduler.scheduleAtFixedRate( () -> execute(), 0,
                period, TimeUnit.NANOSECONDS );

            return true;
        }

        return false;
    }

    /***************************************************************************
     * Signals the scheduler to stop executing the task.
     **************************************************************************/
    public void stop()
    {
        ScheduledExecutorService s = this.scheduler;

        if( s != null )
        {
            future.cancel( false );
            s.shutdown();

            runTimer.stop();
        }
    }

    /***************************************************************************
     * Waits for the scheduler to stop executing tasks.
     **************************************************************************/
    public void waitFor()
    {
        ScheduledExecutorService s = this.scheduler;

        if( s != null )
        {
            try
            {
                // Basically wait forever
                s.awaitTermination( Long.MAX_VALUE, TimeUnit.DAYS );
            }
            catch( InterruptedException e )
            {
            }
        }
    }

    /***************************************************************************
     * Resets this task to be rerun.
     **************************************************************************/
    public void reset()
    {
        stop();
        waitFor();
        this.scheduler = null;
        this.future = null;
        this.count = 0;
    }

    /***************************************************************************
     * @return the number of times this task has been executed.
     **************************************************************************/
    public long getCount()
    {
        return count;
    }

    /***************************************************************************
     * @return the number of nanoseconds this task has been running.
     **************************************************************************/
    public long getRuntime()
    {
        return runTimer.getElapsed();
    }

    /***************************************************************************
     * @return the rate at which the task is executed.
     **************************************************************************/
    public double getRate()
    {
        return rate;
    }

    /***************************************************************************
     * @return the period between task executions.
     **************************************************************************/
    public long getPeriod()
    {
        return period;
    }

    /***************************************************************************
     * Executes the provided {@link #task}.
     **************************************************************************/
    private void execute()
    {
        if( count == 0 )
        {
            this.runTimer.start();
        }

        task.run( count, runTimer.getElapsed() );

        count++;
        if( count < 0 )
        {
            count = 0;
        }
    }

    /***************************************************************************
     * Defines the callback to be executed.
     **************************************************************************/
    public static interface IPeriodic
    {
        /**
         * Executes at a periodic rate.
         * @param count number of times task has been executed from 0 to
         * {@link Long#MAX_VALUE} (213 billion days @ 500 Hz). I guess care
         * should still be taken for overflow to zero.
         * @param duration the number of nanoseconds since the task started.
         */
        public void run( long count, long duration );
    }
}
