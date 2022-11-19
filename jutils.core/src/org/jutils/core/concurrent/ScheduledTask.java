package org.jutils.core.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jutils.core.io.LogUtils;
import org.jutils.core.time.NanoWatch;

/***************************************************************************
 *
 **************************************************************************/
public class ScheduledTask
{
    /**  */
    private final IPeriodic task;
    /**  */
    private final double rate;
    /**  */
    private final long period;
    /**  */
    private final NanoWatch runTimer;

    /**  */
    private ScheduledExecutorService scheduler;
    /**  */
    private ScheduledFuture<?> future;
    /**  */
    private long count;

    /***************************************************************************
     * @param task
     **************************************************************************/
    public ScheduledTask( IPeriodic task )
    {
        this( 10.0, task );
    }

    /***************************************************************************
     * @param rate
     * @param task
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
     * @param name
     * @return
     **************************************************************************/
    public synchronized boolean start()
    {
        if( future == null )
        {
            this.scheduler = Executors.newScheduledThreadPool( 1 );
            this.future = scheduler.scheduleAtFixedRate( () -> execute(), 0,
                period, TimeUnit.NANOSECONDS );

            return true;
        }

        return false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        ScheduledExecutorService s = this.scheduler;

        if( s != null )
        {
            s.shutdown();

            runTimer.stop();
        }
    }

    /***************************************************************************
     * 
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
     * 
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
     * @return
     **************************************************************************/
    public long getCount()
    {
        return count;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getRuntime()
    {
        return runTimer.getElapsed();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getRate()
    {
        return rate;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getPeriod()
    {
        return period;
    }

    /***************************************************************************
     * @param handler
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
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        ScheduledTask t = new ScheduledTask( 500.0,
            ( c, d ) -> doSomething( 10000 ) );

        t.start();

        try
        {
            Thread.sleep( 5000 );
        }
        catch( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        t.stop();
        t.waitFor();

        LogUtils.printDebug( "Processing took %d ns, executed %d times",
            t.getRuntime(), t.getCount() );
    }

    /***************************************************************************
     * @param count
     **************************************************************************/
    private static void doSomething( int count )
    {
        for( int i = 0; i < count; i++ )
        {
            System.nanoTime();
        }
    }

    /***************************************************************************
     *
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
