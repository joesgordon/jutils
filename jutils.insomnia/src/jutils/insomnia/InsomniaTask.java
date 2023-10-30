package jutils.insomnia;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import jutils.core.Utils;
import jutils.core.concurrent.ITask;
import jutils.core.concurrent.ITaskHandler;
import jutils.core.io.LogUtils;
import jutils.core.time.TimeUtils;
import jutils.insomnia.data.ActiveState;
import jutils.insomnia.data.InsomniaConfig;
import jutils.insomnia.data.InsomniaStatus;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaTask implements ITask
{
    /**  */
    private final InsomniaConfig config;
    /**  */
    private final InsomniaStatus status;
    /**  */
    private final ActivityMonitor monitor;
    /**  */
    private final SystemStimulator stimulator;

    /**  */
    private LocalDateTime lastActive;
    /**  */
    private LocalDateTime lastInActive;
    /**  */
    private LocalDateTime lastReset;

    /***************************************************************************
     * @param config the time between waking up in milliseconds
     * @param status
     **************************************************************************/
    public InsomniaTask( InsomniaConfig config, InsomniaStatus status )
    {
        this.config = new InsomniaConfig( config );
        this.status = status;
        this.monitor = new ActivityMonitor( () -> handleActivity() );
        this.stimulator = new SystemStimulator();

        this.lastActive = null;
        this.lastReset = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run( ITaskHandler handler )
    {
        // Assume the user had an active role in starting this task
        this.lastActive = LocalDateTime.now();
        this.lastInActive = lastActive;
        this.lastReset = lastActive;

        status.startTime = LocalDateTime.now( ZoneOffset.UTC );

        monitor.connect();

        try
        {
            while( handler.canContinue() )
            {
                Utils.sleep( 100L );

                updateStatus();
            }
        }
        finally
        {
            monitor.disconnect();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleActivity()
    {
        LocalDateTime now = LocalDateTime.now();

        if( status.currentActivity.state != ActiveState.ACTIVE )
        {
            this.lastInActive = now;
        }

        this.lastActive = now;
    }

    /***************************************************************************
     * @throws InterruptedException
     **************************************************************************/
    private void updateStatus()
    {
        monitor.checkMouse();

        LocalDateTime now = LocalDateTime.now();

        long inactiveDuration = TimeUtils.getMillisDuration( lastActive, now );

        if( inactiveDuration > config.resetPeriod )
        {
            long resetDuration = TimeUtils.getMillisDuration( lastReset, now );

            if( resetDuration > config.resetPeriod )
            {
                lastReset = stimulator.stimulateMouse();
            }
        }

        if( inactiveDuration > config.inactiveDuration )
        {
            status.currentActivity.state = ActiveState.IDLE;
            status.currentActivity.duration = inactiveDuration;
        }
        else if( inactiveDuration > config.inactiveDelay )
        {
            status.currentActivity.state = ActiveState.INACTIVE;
            status.currentActivity.duration = inactiveDuration;
        }
        else
        {
            status.currentActivity.state = ActiveState.ACTIVE;
            status.currentActivity.duration = TimeUtils.getMillisDuration(
                lastInActive, now );
        }

        // LogUtils.printDebug(
        // "Inactive Duration = %d ---> state = %s, duration = %d",
        // inactiveDuration, status.state.name, status.activityDuration );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SystemStimulator
    {
        /**
         * 
         */
        public SystemStimulator()
        {
        }

        /**
         * @return
         * @throws InterruptedException
         */
        public LocalDateTime stimulateMouse()
        {
            try
            {
                Robot r = new Robot();
                PointerInfo info = MouseInfo.getPointerInfo();
                Point p = info == null ? new Point( 0, 0 ) : info.getLocation();
                Point n = new Point( p.x - 1, p.y );

                if( info != null )
                {
                    n.x = n.x < 0 ? 1 : n.x;

                    r.mouseMove( n.x, n.y );

                    Utils.sleep( 100 );

                    r.mouseMove( p.x, p.y );
                }
            }
            catch( SecurityException ex )
            {
                LogUtils.printWarning( "Unable to create Robot." );
            }
            catch( AWTException ex )
            {
                throw new IllegalStateException( ex );
            }

            return LocalDateTime.now();
        }
    }
}
