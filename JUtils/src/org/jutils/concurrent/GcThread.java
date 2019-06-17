package org.jutils.concurrent;

/*******************************************************************************
 * Simple thread that only runs the java garbage collector. The class exists so
 * that the caller does not have to wait on garbage collection to complete.
 ******************************************************************************/
public class GcThread extends Thread
{
    /***************************************************************************
     * Creates a new garbage collection thread.
     **************************************************************************/
    public GcThread()
    {
        super();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void run()
    {
        Runtime.getRuntime().gc();
    }

    /***************************************************************************
     * Creates a GcThread and starts it.
     **************************************************************************/
    public static void createAndStart()
    {
        GcThread t = new GcThread();
        t.start();
    }
}
