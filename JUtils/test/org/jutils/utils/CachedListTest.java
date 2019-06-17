package org.jutils.utils;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.ValidationException;
import org.jutils.io.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CachedListTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizeMinusOne()
    {
        final int testCount = 7;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddExactSize()
    {
        final int testCount = 8;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizePlusOne()
    {
        final int testCount = 9;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceSizeMinusOne()
    {
        final int testCount = 15;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceExactSize()
    {
        final int testCount = 16;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceSizePlusOne()
    {
        final int testCount = 17;

        testNAdditions( testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizePlusOneThenGet()
    {
        final int testCount = 9;
        IDataSerializer<Integer> cacher = new IntegerCacher();

        try( ByteArrayStream byteStream = new ByteArrayStream();
             BufferedStream bufStream = new BufferedStream( byteStream, 8 );
             DataStream stream = new DataStream( bufStream );
             CachedList<Integer> list = new CachedList<>( 4, cacher, stream ) )
        {
            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            int i = list.get( 1 );
            Assert.assertEquals( 1, i );

            list.add( testCount );
            i = list.get( testCount );
            Assert.assertEquals( testCount, i );

            i = list.get( 1 );
            Assert.assertEquals( 1, i );
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAdd32ThenGet32()
    {
        final int testCount = 33;
        IDataSerializer<Integer> cacher = new IntegerCacher();

        try( ByteArrayStream bufStream = new ByteArrayStream();
             DataStream stream = new DataStream( bufStream );
             CachedList<Integer> list = new CachedList<>( 4, cacher, stream ) )
        {
            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            for( int i = 0; i < testCount; i++ )
            {
                int val = list.get( i );
                Assert.assertEquals( i, val );
            }
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * @param testCount the number of items to add
     **************************************************************************/
    private static void testNAdditions( int testCount )
    {
        IDataSerializer<Integer> cacher = new IntegerCacher();

        try( ByteArrayStream bufStream = new ByteArrayStream();
             DataStream stream = new DataStream( bufStream );
             CachedList<Integer> list = new CachedList<>( 4, cacher, stream ) )
        {
            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            Assert.assertEquals( testCount, list.size() );

            int cacheCount = list.size();
            int cacheSize = 4;
            long expectedSize = cacheCount * cacheSize;

            Assert.assertEquals( "count: " + cacheCount + ", size: " +
                cacheSize + ", #: " + list.size(), expectedSize,
                stream.getLength() );
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntegerCacher implements IDataSerializer<Integer>
    {
        @Override
        public Integer read( IDataStream stream )
            throws IOException, ValidationException
        {
            return stream.readInt();
        }

        @Override
        public void write( Integer item, IDataStream stream ) throws IOException
        {
            stream.writeInt( item );
        }
    }
}
