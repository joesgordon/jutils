package org.jutils.io;

import java.io.File;

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
}
