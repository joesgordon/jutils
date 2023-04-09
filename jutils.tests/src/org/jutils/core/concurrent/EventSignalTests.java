package org.jutils.core.concurrent;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.time.NanoWatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EventSignalTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testTimeout()
    {
        long expected = 1000;
        EventSignal s = new EventSignal();
        NanoWatch w = new NanoWatch();

        w.start();
        s.mawait( expected );
        w.stop();
        long actual = w.getElapsed() / 1000 / 1000;

        long delta = Math.abs( actual - expected );

        Assert.assertTrue( String.format( "Delta too large: %d - %d = %d",
            actual, expected, delta ), delta < 20 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSignal()
    {
        long expected = 1000;
        EventSignal s = new EventSignal();
        NanoWatch w = new NanoWatch();

        Thread t = new Thread( () -> {
            try
            {
                Thread.sleep( expected );
            }
            catch( InterruptedException ex )
            {
                throw new RuntimeException( ex );
            }
            s.signal();
        } );
        t.start();

        w.start();
        s.mawait( 2 * expected );
        w.stop();

        long actual = w.getElapsed() / 1000 / 1000;

        long delta = Math.abs( actual - expected );

        Assert.assertTrue( String.format( "Delta too large: %d - %d = %d",
            actual, expected, delta ), delta < 20 );
    }
}
