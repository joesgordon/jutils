package jutils.core.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import jutils.core.ValidationException;
import jutils.core.io.xs.XsUtils;
import jutils.core.ui.ColorMapView;

/*******************************************************************************
 *
 ******************************************************************************/
public class XStreamUtilsTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void test_writeObjectXStream()
    {
        try
        {
            XsUtils.writeObjectXStream( 42.0 );
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
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
    public void test_readObjectXStream()
    {
        try
        {
            Double expected = 42.0;
            String xml = XsUtils.writeObjectXStream( expected,
                FileState.class.getPackage().getName() );
            Double d = XsUtils.readObjectXStream( xml,
                FileState.class.getPackage().getName() );

            Assert.assertEquals( expected, d );
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
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
    public void test_readObjectXStream2()
    {
        try
        {
            File file = new File( "C:\\Windows\\System32\\Calc.exe" );
            FileState expected = new FileState( file );
            String xml = XsUtils.writeObjectXStream( expected,
                FileState.class.getPackage().getName() );
            FileState actual = XsUtils.readObjectXStream( xml, "org",
                FileState.class.getPackage().getName() );

            Assert.assertEquals( expected, actual );
        }
        catch( ValidationException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
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
    public void test_buildDependencyList()
    {
        List<String> pkgs = XsUtils.buildDependencyList(
            ColorMapView.class );

        for( String pkg : pkgs )
        {
            LogUtils.printDebug( "Package: " + pkg );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private static class FileState
    {
        /**  */
        public final String path;
        /**  */
        public final long length;

        /**
         * @param file
         */
        public FileState( File file )
        {
            this.path = file.getAbsolutePath();
            this.length = file.length();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }
            else if( obj instanceof FileState )
            {
                FileState fs = ( FileState )obj;

                if( !path.equals( fs.path ) )
                {
                    return false;
                }
                else if( length != fs.length )
                {
                    return false;
                }
            }
            else
            {
                return false;
            }

            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            return Objects.hash( path, length );
        }
    }
}
