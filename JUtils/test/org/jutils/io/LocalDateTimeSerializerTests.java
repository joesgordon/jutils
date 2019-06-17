package org.jutils.io;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class LocalDateTimeSerializerTests
{
    /**
     * 
     */
    @Test
    public void testSerializationEpoch()
    {
        LocalDateTime time = LocalDateTime.of( 1970, Month.JANUARY, 1, 0, 0, 0,
            0 );

        testSerialization( time );
    }

    /**
     * 
     */
    @Test
    public void testSerializationEpochNanoBeforeMidnight()
    {
        LocalDateTime time = LocalDateTime.of( 1970, Month.JANUARY, 1, 23, 59,
            59, 999999999 );

        testSerialization( time );
    }

    /**
     * 
     */
    @Test
    public void testSerializationEpochEndOfYear()
    {
        LocalDateTime time = LocalDateTime.of( 1970, Month.DECEMBER, 31, 23, 59,
            59, 999999999 );

        testSerialization( time );
    }

    /**
     * @param time the value to test;
     */
    public void testSerialization( LocalDateTime time )
    {
        LocalDateTimeSerializer serializer = new LocalDateTimeSerializer();

        try( ByteArrayStream bas = new ByteArrayStream( 8 );
             IDataStream stream = new DataStream( bas ) )
        {
            serializer.write( time, stream );

            stream.seek( 0L );

            LocalDateTime actual = serializer.read( stream );

            Assert.assertEquals( time, actual );
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }
}
