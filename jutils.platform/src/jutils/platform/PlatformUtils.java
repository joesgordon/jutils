package jutils.platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jutils.core.EmbeddedResources;
import jutils.core.ValidationException;
import jutils.core.io.IOUtils;
import jutils.core.io.LogUtils;
import jutils.core.io.ResourceLoader;
import jutils.core.io.parsers.IntegerParser;
import jutils.platform.jni.JniUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class PlatformUtils
{
    /**  */
    private static final ResourceInfo [] RESOURCES = new ResourceInfo[] {
        new ResourceInfo( "jxinput.dll" ) };
    /**  */
    private static final File RESOURCES_DIR = IOUtils.getUsersFile( ".jxi" );
    /**  */
    private static final ResourceLoader LOADER;
    /**  */
    private static IPlatform PLATFORM = null;

    static
    {
        LOADER = new ResourceLoader( PlatformUtils.class, "resources" );
    }

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private PlatformUtils()
    {
        ;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static synchronized IPlatform getPlatform()
    {
        if( PLATFORM == null )
        {
            EmbeddedResources resources = new EmbeddedResources(
                RESOURCES_DIR );
            resources.addEmbeddedResource( PlatformUtils.class, "resources",
                "cutils_jni.dll", true );
            resources.checkResources();

            PLATFORM = JniUtils.getPlatform();
        }
        return PLATFORM;
    }

    /***************************************************************************
     * @param resource
     * @param file
     * @return
     **************************************************************************/
    private static boolean isResourceStale( ResourceInfo resource, File file )
    {
        boolean copy = false;

        if( file.isFile() )
        {
            String propsName = resource.filename + ".props";
            URL url = LOADER.getUrl( propsName );

            resource.readInfo( url );

            if( file.length() != resource.size )
            {
                copy = true;
            }
            else
            {
                LocalDateTime lmt = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli( file.lastModified() ),
                    ZoneId.systemDefault() );

                // LogUtils.printDebug( "Comparing %s to %s", resource.time, lmt
                // );

                int cmp = resource.time.compareTo( lmt );

                if( cmp > 0 )
                {
                    copy = true;
                }
            }
        }
        else
        {
            copy = true;
        }

        return copy;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void checkResources()
    {
        if( !RESOURCES_DIR.isDirectory() )
        {
            if( !RESOURCES_DIR.mkdirs() )
            {
                throw new RuntimeException(
                    "Cannot create resoure directory: " +
                        RESOURCES_DIR.getAbsolutePath() );
            }
        }

        List<File> files = new ArrayList<>();

        for( ResourceInfo resource : RESOURCES )
        {
            File file = new File( RESOURCES_DIR, resource.filename );

            if( isResourceStale( resource, file ) )
            {
                try
                {
                    copyResource( resource.filename, file );
                }
                catch( IOException ex )
                {
                    throw new RuntimeException(
                        "Cannot copy resource to: " + file.getAbsolutePath(),
                        ex );
                }
            }

            files.add( file );
        }

        for( File file : files )
        {
            // LogUtils.printDebug( "Loading " + file.getName() );

            System.load( file.getAbsolutePath() );
        }
    }

    /***************************************************************************
     * @param name
     * @param file
     * @throws IOException
     **************************************************************************/
    private static void copyResource( String name, File file )
        throws IOException
    {
        try( InputStream stream = LOADER.getInputStream( name ) )
        {
            if( file.isFile() )
            {
                if( !file.delete() )
                {
                    throw new IOException(
                        "Unable to delete file " + file.getAbsolutePath() );
                }
            }

            // LogUtils.printDebug(
            // "Copying " + name + " to " + file.getAbsolutePath() );
            Files.copy( stream, file.toPath() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ResourceInfo
    {
        public final String filename;
        public long size;
        public LocalDateTime time;

        /**
         * @param name
         * @param filename
         */
        public ResourceInfo( String filename )
        {
            this.filename = filename;
            this.size = 0L;
            this.time = LocalDateTime.MIN;
        }

        /***************************************************************************
         * @param url
         * @param info
         * @return the build information for JUtils.
         **************************************************************************/
        public void readInfo( URL url )
        {
            IntegerParser parser = new IntegerParser( 0, null );
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HHmmss" );

            try( InputStream stream = url.openStream() )
            {
                Properties props = new Properties();

                props.load( stream );

                String time = props.getProperty( "jxinput.time" );
                String size = props.getProperty( "jxinput.length" );

                this.size = parser.parse( size );
                this.time = LocalDateTime.parse( time, dtf );
            }
            catch( IOException ex )
            {
                LogUtils.printError( "Unable to open resource information.",
                    ex );
            }
            catch( ValidationException | DateTimeParseException ex )
            {
                LogUtils.printError( "Unable to parse resource information.",
                    ex );
            }
        }
    }
}
