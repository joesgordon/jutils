package jutils.core.io;

import java.io.File;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;

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
        try
        {
            IOUtils.byteCount( -10000 );
            Assert.fail( "Exception not thrown" );
        }
        catch( IllegalArgumentException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_byteCount1029()
    {
        String actual = IOUtils.byteCount( 1029 );
        String expected = "1.0 KB";

        Assert.assertEquals( expected, actual );
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_getRelativePath()
    {
        File from = new File( "/blah/sub1/" );
        File to = new File( "/blah/sub2/bar.txt" );

        String expected = "..\\sub2\\bar.txt";
        String actual = IOUtils.getRelativePath( from, to );

        Assert.assertEquals( expected, actual );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_getRelativePath2()
    {
        File from = new File( "C:\\Windows" );
        File to = new File( "D:\\foo\\bar.txt" );

        try
        {
            IOUtils.getRelativePath( from, to );
            Assert.fail( "Exception not thrown" );
        }
        catch( IllegalArgumentException ex )
        {
        }
    }
}
