package jutils.telemetry.ch10.io.time1;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.utils.ByteOrdering;
import jutils.telemetry.ch10.IrigDayTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IrigDayTimeSerializerTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testStartOfYearReflection()
    {
        IrigDayTime time = new IrigDayTime();

        testReflection( time );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testEndOfYearReflection()
    {
        IrigDayTime time = new IrigDayTime();

        time.days = 366;
        time.hours = 23;
        time.minutes = 59;
        time.seconds = 59;
        time.milliseconds = 990;

        testReflection( time );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testEndOfYearValues()
    {
        IrigDayTime time = new IrigDayTime();

        time.days = 366;
        time.hours = 23;
        time.minutes = 59;
        time.seconds = 59;
        time.milliseconds = 990;

        try( ByteArrayStream bas = new ByteArrayStream( 6 );
             DataStream stream = new DataStream( bas,
                 ByteOrdering.LITTLE_ENDIAN ) )
        {
            IrigDayTimeSerializer serializer = new IrigDayTimeSerializer();
            serializer.write( time, stream );

            byte [] buf = bas.getBuffer();

            Assert.assertEquals( 0x99, Byte.toUnsignedLong( buf[0] ) );
            Assert.assertEquals( 0x59, Byte.toUnsignedLong( buf[1] ) );

            Assert.assertEquals( 0x59, Byte.toUnsignedLong( buf[2] ) );
            Assert.assertEquals( 0x23, Byte.toUnsignedLong( buf[3] ) );

            Assert.assertEquals( 0x66, Byte.toUnsignedLong( buf[4] ) );
            Assert.assertEquals( 0x03, Byte.toUnsignedLong( buf[5] ) );
        }
        catch( IOException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    private void testReflection( IrigDayTime time )
    {
        IrigDayTimeSerializer serializer = new IrigDayTimeSerializer();

        try( ByteArrayStream bas = new ByteArrayStream( 6 );
             DataStream stream = new DataStream( bas,
                 ByteOrdering.LITTLE_ENDIAN ) )
        {
            serializer.write( time, stream );
            stream.seek( 0L );
            IrigDayTime actual = serializer.read( stream );

            Assert.assertEquals( time, actual );
        }
        catch( IOException ex )
        {
            Assert.fail( ex.getMessage() );
        }
        catch( ValidationException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
