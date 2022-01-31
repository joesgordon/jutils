package org.jutils;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.core.io.options.OptionsSerializer;
import org.jutils.duak.DuakConstants;
import org.jutils.duak.DuakOptions;

/**
 * 
 */
public class AllOptionsTests
{
    @Test
    public void testDuakOptions()
    {
        OptionsSerializer<DuakOptions> userio = DuakConstants.getOptions();

        DuakOptions options = userio.getOptions();
        DuakOptions copy = new DuakOptions( options );

        options.recentDirs.push( new File( "\\" ) );

        userio.write( options );
        DuakOptions options2 = userio.read();
        userio.write( copy );

        Assert.assertTrue( options.equals( options2 ) );
    }
}
