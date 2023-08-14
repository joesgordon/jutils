package jutils.core.utils;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.utils.CircularQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CircularQueueTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testPushOneLess()
    {
        int count = 10;
        CircularQueue<Integer> queue = new CircularQueue<>( count );

        for( int i = 0; i < ( count - 1 ); i++ )
        {
            queue.push( i );
        }

        Assert.assertEquals( count - 1, queue.size() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testExactly()
    {
        int count = 10;
        CircularQueue<Integer> queue = new CircularQueue<>( count );

        for( int i = 0; i < ( count ); i++ )
        {
            queue.push( i );
        }

        Assert.assertEquals( count, queue.size() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testPushOneExtra()
    {
        int count = 10;
        CircularQueue<Integer> queue = new CircularQueue<>( count );

        for( int i = 0; i < ( count + 1 ); i++ )
        {
            queue.push( i );
        }

        Assert.assertEquals( count, queue.size() );

        Assert.assertEquals( 1, queue.get( 0 ).intValue() );
        Assert.assertEquals( count, queue.get( count - 1 ).intValue() );
    }
}
