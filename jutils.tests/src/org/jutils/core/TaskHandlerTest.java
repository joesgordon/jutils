package org.jutils.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jutils.core.concurrent.TaskHandler;

/*******************************************************************************
 * Defines unit tests for a {@link TaskHandler}.
 ******************************************************************************/
public class TaskHandlerTest
{
    /***************************************************************************
     * Test method for {@link TaskHandler#canContinue()}.
     **************************************************************************/
    @Test
    public void testCanContinue()
    {
        TaskHandler handler = new TaskHandler();
        boolean canContinuePrior = handler.canContinue();
        handler.stop();
        boolean canContinuePost = handler.canContinue();
        assertTrue( canContinuePrior && !canContinuePost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.core.concurrent.Taskable#isFinished()}.
     **************************************************************************/
    @Test
    public void testIsFinished()
    {
        TaskHandler handler = new TaskHandler();

        boolean isFinishedPrior = handler.isFinished();
        handler.signalFinished();
        boolean isFinishedPost = handler.isFinished();
        assertTrue( !isFinishedPrior && isFinishedPost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.core.concurrent.Taskable#stop()}.
     **************************************************************************/
    @Test
    public void testStop()
    {
        TaskHandler handler = new TaskHandler();

        handler.stop();
        handler.signalFinished();

        handler.waitFor();

        assertTrue( handler.isFinished() );
    }
}
