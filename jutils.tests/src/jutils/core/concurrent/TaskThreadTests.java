package jutils.core.concurrent;

import org.junit.Test;

import jutils.core.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskThreadTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testRestart()
    {
        ITask task = ( h ) -> {
            while( h.canContinue() )
            {
                Utils.sleep( 250 );
            }
        };

        TaskThread thread = new TaskThread( task, "RestartTest" );

        thread.start();
        Utils.sleep( 200 );
        thread.stop();
        thread.interrupt();
        thread.waitFor();

        // thread.reset();

        thread.start();
        Utils.sleep( 200 );
        thread.stop();
        thread.interrupt();
        thread.waitFor();
    }
}
