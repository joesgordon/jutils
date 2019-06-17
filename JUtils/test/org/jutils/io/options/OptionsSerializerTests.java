package org.jutils.io.options;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.jutils.io.*;

public class OptionsSerializerTests
{
    @Test
    public void test()
    {
        File file;
        try
        {
            file = File.createTempFile( "OptionsSerializerTests", ".xml" );
        }
        catch( IOException ex )
        {
            fail( "I/O Exception: " + ex.getMessage() );
            ex.printStackTrace();
            return;
        }

        TestType testObj = new TestType( 3, 3.14f );
        String xml;

        try
        {
            xml = XStreamUtils.writeObjectXStream( testObj );
        }
        catch( IOException ex )
        {
            fail( "I/O Exception: " + ex.getMessage() );
            ex.printStackTrace();
            return;
        }

        xml = xml.replaceAll( "testint", "george" ).replaceAll( "testfloat",
            "lenny" );

        try( FilePrintStream stream = new FilePrintStream( file ) )
        {
            stream.println( xml );
        }
        catch( IOException ex )
        {
            fail( "I/O Exception: " + ex.getMessage() );
            ex.printStackTrace();
            return;
        }

        OptionsSerializer<TestType> os = OptionsSerializer.getOptions(
            TestType.class, file, new TestTypeCreator() );

        os.read();
    }

    private static class TestType
    {
        public int testint;
        public float testfloat;

        public TestType( int testint, float testfloat )
        {
            this.testint = testint;
            this.testfloat = testfloat;
        }
    }

    private static class TestTypeCreator implements IOptionsCreator<TestType>
    {
        @Override
        public TestType createDefaultOptions()
        {
            return new TestType( 1, 1.0f );
        }

        @Override
        public TestType initialize( TestType tt )
        {
            return new TestType( tt.testint, tt.testfloat );
        }

        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
