package org.jutils.insomnia;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.jutils.core.concurrent.ITask;
import org.jutils.core.concurrent.ITaskHandler;
import org.jutils.core.time.TimeUtils;
import org.jutils.insomnia.data.ActiveState;
import org.jutils.insomnia.data.InsomniaConfig;
import org.jutils.insomnia.data.InsomniaStatus;

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
                Thread.sleep( 100L );

                updateStatus();
            }
        }
        catch( InterruptedException ex )
        {
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
    private void updateStatus() throws InterruptedException
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
        /**  */
        private final Toolkit tk;
        /**  */
        private final int keyCode;

        /**
         * 
         */
        public SystemStimulator()
        {
            this.tk = Toolkit.getDefaultToolkit();
            this.keyCode = KeyEvent.VK_SCROLL_LOCK;
        }

        /**
         * @return
         * @throws InterruptedException
         */
        public LocalDateTime stimulateKey() throws InterruptedException
        {
            boolean flag = tk.getLockingKeyState( keyCode );

            // LogUtils.printDebug( "Toggling..." );

            tk.setLockingKeyState( keyCode, !flag );

            Thread.sleep( 100 );

            // LogUtils.printDebug( "Toggling back..." );

            tk.setLockingKeyState( keyCode, flag );

            return LocalDateTime.now();
        }

        /**
         * @return
         * @throws InterruptedException
         */
        public LocalDateTime stimulateMouse() throws InterruptedException
        {
            try
            {
                Robot r = new Robot();
                Point p = MouseInfo.getPointerInfo().getLocation();
                Point n = new Point( p.x - 1, p.y );

                n.x = n.x < 0 ? 1 : n.x;

                r.mouseMove( n.x, n.y );

                Thread.sleep( 100 );

                r.mouseMove( p.x, p.y );
            }
            catch( AWTException ex )
            {
                throw new IllegalStateException( ex );
            }

            return LocalDateTime.now();
        }
    }
}
