package org.jutils.insomnia;

import org.jutils.core.concurrent.TaskThread;
import org.jutils.insomnia.data.InsomniaConfig;
import org.jutils.insomnia.data.InsomniaStatus;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaController
{
    /**  */
    private InsomniaTask task;
    /**  */
    private TaskThread thread;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaController()
    {
        this.task = null;
        this.thread = null;
    }

    /***************************************************************************
     * @param config
     * @param status
     **************************************************************************/
    public synchronized void start( InsomniaConfig config,
        InsomniaStatus status )
    {
        this.task = new InsomniaTask( config, status );
        this.thread = new TaskThread( task, "Insomnia Thread" );

        thread.start();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public synchronized void stop()
    {
        if( thread != null )
        {
            thread.stop();

            thread = null;
            task = null;
        }
    }
}
