package org.jutils.core.io;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IOUtilsTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_byteCountNegativeValue()
    {
        String value = IOUtils.byteCount( -10000 );

        LogUtils.printDebug( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_findClosestCommonAncestor_UNC_success()
    {
        String expected = "\\\\some\\unc";
        String [] paths = { expected + "\\path",
            expected + "\\alternate\\path" };
        File [] files = new File[paths.length];

        for( int i = 0; i < paths.length; i++ )
        {
            files[i] = new File( paths[i] );
        }

        File cca = IOUtils.findClosestCommonAncestor( files );

        Assert.assertEquals( expected, cca.getAbsolutePath() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_8BitEncodingReflectivelySymmetric()
    {
        Charset encoding = IOUtils.get8BitEncoding();

        for( int i = 0; i < 256; i++ )
        {
            byte [] bytes = new byte[] { ( byte )i };
            String str = new String( bytes, encoding );
            byte [] actuals = str.getBytes( encoding );

            Assert.assertArrayEquals( bytes, actuals );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_AsciiBitEncodingReflectivelySymmetric()
    {
        Charset encoding = Charset.forName( "US-ASCII" );

        byte [] bytes = new byte[] { ( byte )128 };
        String str = new String( bytes, encoding );
        byte [] actuals = str.getBytes( encoding );

        Assert.assertNotEquals( bytes[0], actuals[0] );
    }
}