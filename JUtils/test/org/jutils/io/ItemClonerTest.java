package org.jutils.io;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ItemClonerTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSingleUse()
    {
        ItemCloner<Object []> cloner = new ItemCloner<>();

        Object [] item;
        Object [] clone;

        item = buildArray( 32 );
        clone = cloner.cloneItem( item );

        Assert.assertArrayEquals( item, clone );
        Assert.assertNotSame( item, clone );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testMultipleUse()
    {
        ItemCloner<Object []> cloner = new ItemCloner<>();

        Object [] item;
        Object [] clone;

        item = buildArray( 32 );
        clone = cloner.cloneItem( item );

        Assert.assertArrayEquals( item, clone );
        Assert.assertNotSame( item, clone );

        item = buildArray( 55 );
        clone = cloner.cloneItem( item );

        Assert.assertArrayEquals( item, clone );
        Assert.assertNotSame( item, clone );

        item = buildArray( 12 );
        clone = cloner.cloneItem( item );

        Assert.assertArrayEquals( item, clone );
        Assert.assertNotSame( item, clone );
    }

    /***************************************************************************
     * @param count
     * @return
     **************************************************************************/
    private static Object [] buildArray( int count )
    {
        Object [] values = new Object[count];

        Random rand = new Random();
        for( int i = 0; i < count; i++ )
        {
            values[i] = rand.nextInt();
        }

        return values;
    }
}
