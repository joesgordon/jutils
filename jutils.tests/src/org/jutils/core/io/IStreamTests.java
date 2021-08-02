package org.jutils.core.io;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.ValidationException;

/***************************************************************************
 * 
 **************************************************************************/
public abstract class IStreamTests
{
    /***************************************************************************
     * @return
     **************************************************************************/
    protected abstract IStream createStream();

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public final void testWriteSeekBackWrite()
    {
        try( IStream stream = createStream() )
        {
            byte [] expecteds = new byte[16];
            byte [] actuals = new byte[expecteds.length];

            for( byte b = 15; b > -1; b-- )
            {
                stream.write( b );
            }

            stream.seek( 0L );

            for( byte b = 0; b < 16; b++ )
            {
                expecteds[b] = b;
                stream.write( b );
            }

            stream.seek( 0L );

            stream.readFully( actuals );

            Assert.assertArrayEquals( expecteds, actuals );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteDataJustPastBufferThenCheckEndOfFile()
    {
        try( IStream stream = createStream() )
        {
            byte count = 11;
            long expectedLen = 3L * 11L;

            for( byte i = 0; i < count; i++ )
            {
                stream.write( i );
                stream.write( i );
                stream.write( i );
            }

            stream.seek( 0L );

            for( byte i = 0; i < count; i++ )
            {
                byte b;

                b = stream.read();
                Assert.assertEquals( i, b );

                b = stream.read();
                Assert.assertEquals( i, b );

                b = stream.read();
                Assert.assertEquals( i, b );
            }

            Assert.assertEquals( expectedLen, stream.getLength() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public final void testWriteFarPastEndOfStream()
    {
        try( IStream stream = createStream() )
        {
            byte [] expecteds = new byte[32];
            byte [] actuals = new byte[expecteds.length];

            stream.seek( 16L );

            for( byte b = 0; b < 16; b++ )
            {
                expecteds[b + 16] = b;
                stream.write( b );
            }

            stream.seek( 0L );

            stream.readFully( actuals );

            Assert.assertArrayEquals( expecteds, actuals );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public final void testRead()
    {
        byte [] bytes = createBytes();

        try( IStream stream = createStream() )
        {
            stream.write( bytes );

            stream.seek( 0L );

            byte b = stream.read();

            Assert.assertEquals( bytes[0], b );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public final void testReadByteArray()
    {
        byte [] bytes = createBytes();
        byte [] buf = new byte[bytes.length];

        try( IStream stream = createStream() )
        {
            stream.write( bytes );
            stream.seek( 0L );
            stream.read( buf );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadFullyGreaterThanAvailable()
    {
        byte [] bytes = createBytes();
        byte [] buf = new byte[bytes.length + 10];

        try( IStream stream = createStream() )
        {
            stream.write( bytes );
            stream.seek( 0L );
            stream.readFully( buf );
            Assert.fail( "An EOF Exception should have been thrown." );
        }
        catch( EOFException ex )
        {
            return;
        }
        catch( IOException ex )
        {
            Assert.fail(
                "An EOF Exception should have been thrown instead of an I/O Exception." );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadByteArrayIntInt()
    {
        byte [] bytes = createBytes();
        byte [] buf = new byte[bytes.length];

        try( IStream stream = createStream() )
        {
            stream.write( bytes );
            stream.seek( 0L );
            stream.read( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadFullyByteArrayIntInt()
    {
        byte [] bytes = createBytes();
        byte [] buf = new byte[bytes.length];

        try( IStream stream = createStream() )
        {
            stream.write( bytes );
            stream.seek( 0L );
            stream.readFully( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSeek()
    {
        byte [] bytes = createBytes();
        int off = ( int )( bytes.length / 3.0 );
        int len = ( int )( bytes.length * 2 / 3.0 );

        try( IStream stream = createStream() )
        {
            stream.write( bytes );

            stream.seek( off );
            Assert.assertEquals( off, stream.getPosition() );
            byte b = stream.read();
            Assert.assertEquals( bytes[off], b );

            stream.seek( len );
            Assert.assertEquals( len, stream.getPosition() );
            Assert.assertEquals( bytes[len], stream.read() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testClose()
    {
        try( IStream stream = createStream() )
        {
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGetPosition()
    {
        try( IStream stream = createStream() )
        {
            Assert.assertEquals( 0, stream.getPosition() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGetLength()
    {
        try( IStream stream = createStream() )
        {
            Assert.assertEquals( 0L, stream.getLength() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteData()
    {
        byte [] bytes = createBytes();

        try( IStream stream = createStream() )
        {
            int len = 0;

            int count;// = ( int )( ByteCache.DEFAULT_SIZE / bytes.length ) + 1;

            count = 1;
            Assert.assertEquals( 0, stream.getLength() );

            stream.write( ( byte )55 );
            stream.write( ( byte )55 );
            stream.write( ( byte )55 );
            stream.write( ( byte )55 );

            len += 4;

            Assert.assertEquals( 4, stream.getLength() );

            for( int i = 0; i < count; i++ )
            {
                stream.write( bytes );
                len += bytes.length;
            }

            Assert.assertEquals( len, stream.getLength() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream2()
    {
        byte [] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte [] buffer = new byte[expected.length];

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer,
            buffer.length, 0, false );
             BufferedStream stream = new BufferedStream( byteStream ) )
        {
            stream.write( expected, 0, 4 );
            stream.write( expected[4] );
            stream.write( expected, 5, 6 );
            stream.flush();

            byte [] actual = new byte[expected.length];

            stream.seek( 0 );
            stream.readFully( actual );

            Assert.assertArrayEquals( expected, actual );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream3()
    {
        byte [] expected = new byte[7];
        byte [] actual = new byte[expected.length];
        int WRITE_COUNT = 235;

        for( int i = 0; i < expected.length; i++ )
        {
            expected[i] = ( byte )( i );
        }

        try
        {
            File file = File.createTempFile( getClass().getSimpleName() + "_",
                ".bin" );
            file.deleteOnExit();

            try( FileStream fstream = new FileStream( file );
                 BufferedStream stream = new BufferedStream( fstream, 16 ) )
            {
                for( int i = 0; i < WRITE_COUNT; i++ )
                {
                    stream.write( expected );
                }

                stream.write( ( byte )0 );
                stream.write( ( byte )10 );
                stream.write( ( byte )20 );
                stream.write( ( byte )30 );
            }

            Assert.assertEquals( WRITE_COUNT * expected.length + 4,
                file.length() );

            try( FileStream fstream = new FileStream( file, true );
                 BufferedStream stream = new BufferedStream( fstream, 16 ) )
            {
                stream.seek( 0 );
                for( int i = 0; i < WRITE_COUNT; i++ )
                {
                    stream.readFully( actual );

                    Assert.assertArrayEquals( expected, actual );
                }
            }

        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream()
    {
        byte [] buffer = new byte[100];

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer );
             BufferedStream bufStream = new BufferedStream( byteStream );
             DataStream stream = new DataStream( bufStream ) )
        {
            stream.writeInt( 4 );

            stream.write( ( byte )42 );

            Assert.assertEquals( 5, stream.getPosition() );

            stream.seek( 0 );

            Assert.assertEquals( 0, stream.getPosition() );

            stream.readInt();

            Assert.assertEquals( 4, stream.getPosition() );

            byte b = stream.read();

            Assert.assertEquals( 5, stream.getPosition() );

            Assert.assertEquals( 42, b );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteMockDataToDataStream()
    {
        byte [] buffer = new byte[100];

        MockObjectSerializer serializer = new MockObjectSerializer();
        MockObject expected = new MockObject();
        MockObject actual;

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer );
             BufferedStream bufStream = new BufferedStream( byteStream );
             DataStream stream = new DataStream( bufStream ) )
        {
            serializer.write( expected, stream );

            stream.seek( 0L );

            actual = serializer.read( stream );

            Assert.assertEquals( expected, actual );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MockObject
    {
        public int i = 8;
        public boolean b = true;
        public double d = 9.0;
        public float f = 4.5f;
        public char c = 'y';
        public long l = 42;

        @Override
        public boolean equals( Object obj )
        {
            if( obj != null )
            {
                if( obj == this )
                {
                    return true;
                }

                if( obj instanceof MockObject )
                {
                    MockObject that = ( MockObject )obj;
                    return i == that.i && b == that.b &&
                        Math.abs( d - that.d ) < 0.00001 &&
                        Math.abs( f - that.f ) < 0.00001f && c == that.c &&
                        l == that.l;
                }

                return false;
            }

            return false;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash( i, b, d, f, c, l );
        }

        @Override
        public String toString()
        {
            StringBuilder str = new StringBuilder();

            str.append( "i = " );
            str.append( i );

            str.append( ", b = " );
            str.append( b );

            str.append( ", d = " );
            str.append( d );

            str.append( ", f = " );
            str.append( f );

            str.append( ", c = " );
            str.append( c );

            str.append( ", l = " );
            str.append( l );

            return str.toString();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MockObjectSerializer
        implements IDataSerializer<MockObject>
    {
        @Override
        public MockObject read( IDataStream stream )
            throws IOException, ValidationException
        {
            MockObject obj = new MockObject();

            obj.i = stream.readInt();
            obj.b = stream.readBoolean();
            // obj.d = stream.readDouble();
            // obj.f = stream.readFloat();
            // obj.c = ( char )stream.read();
            // obj.l = stream.readLong();

            return obj;
        }

        @Override
        public void write( MockObject item, IDataStream stream )
            throws IOException
        {
            stream.writeInt( item.i );
            stream.writeBoolean( item.b );
            // stream.write( ( byte )1 );
            // stream.writeDouble( item.d );
            // stream.writeFloat( item.f );
            // stream.write( ( byte )item.c );
            // stream.writeLong( item.l );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static byte [] createBytes()
    {
        byte [] seed = { 42, 13, 37 };
        byte [] bytes = new byte[512];
        SecureRandom rand = new SecureRandom( seed );

        rand.nextBytes( bytes );

        return bytes;
    }
}
