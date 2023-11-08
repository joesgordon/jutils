package jutils.core;

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

import jutils.core.io.LogUtils;
import jutils.core.io.ResourceLoader;
import jutils.core.io.parsers.IntegerParser;
import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EmbeddedResources
{
    /**  */
    private final File resourcesDir;
    /**  */
    private final List<EmbeddedResource> embeddeds;

    /***************************************************************************
     * @param resourcesDir
     **************************************************************************/
    public EmbeddedResources( File resourcesDir )
    {
        this.resourcesDir = resourcesDir;
        this.embeddeds = new ArrayList<EmbeddedResource>();
    }

    /***************************************************************************
     * @param cls
     * @param packageName
     * @param filename
     * @param isLibrary
     **************************************************************************/
    public void addEmbeddedResource( Class<?> cls, String packageName,
        String filename, boolean isLibrary )
    {
        embeddeds.add(
            new EmbeddedResource( cls, packageName, filename, isLibrary ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void checkResources()
    {
        if( !resourcesDir.isDirectory() )
        {
            if( !resourcesDir.mkdirs() )
            {
                throw new RuntimeException(
                    "Cannot create resoure directory: " +
                        resourcesDir.getAbsolutePath() );
            }
        }

        for( EmbeddedResource emb : embeddeds )
        {
            ResourceInfo resource = new ResourceInfo( emb.filename );
            ResourceLoader loader = emb.getLoader();
            String propsName = emb.filename + ".props";
            URL url = loader.getUrl( propsName );

            resource.readInfo( url );

            File file = new File( resourcesDir, resource.filename );

            if( isResourceStale( loader, resource, file ) )
            {
                try
                {
                    copyResource( loader, resource.filename, file );
                }
                catch( IOException ex )
                {
                    throw new RuntimeException(
                        "Cannot copy resource to: " + file.getAbsolutePath(),
                        ex );
                }
            }
            else
            {
                LogUtils.printDebug( "Resource %s is not stale", emb.filename );
            }

            if( emb.isLibrary )
            {
                System.load( file.getAbsolutePath() );
            }
        }
    }

    /***************************************************************************
     * @param loader
     * @param resource
     * @param file
     * @return
     **************************************************************************/
    private static boolean isResourceStale( ResourceLoader loader,
        ResourceInfo resource, File file )
    {
        boolean copy = false;

        if( file.isFile() )
        {
            String propsName = resource.getPropsName();
            URL url = loader.getUrl( propsName );

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
     * @param loader
     * @param name
     * @param file
     * @throws IOException
     **************************************************************************/
    private static void copyResource( ResourceLoader loader, String name,
        File file ) throws IOException
    {
        try( InputStream stream = loader.getInputStream( name ) )
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
    private static final class EmbeddedResource
    {
        /**  */
        private final Class<?> cls;
        /**  */
        private final String packageName;
        /**  */
        private final String filename;
        /**  */
        private final boolean isLibrary;

        /**
         * @param cls
         * @param packageName
         * @param filename
         * @param isLibrary
         */
        public EmbeddedResource( Class<?> cls, String packageName,
            String filename, boolean isLibrary )
        {
            this.cls = cls;
            this.packageName = packageName;
            this.filename = filename;
            this.isLibrary = isLibrary;
        }

        /**
         * @return
         */
        public ResourceLoader getLoader()
        {
            return new ResourceLoader( cls, packageName );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ResourceInfo
    {
        /**  */
        private static final String TIME_DATE_FMT = "uuuu-MM-dd HHmmss";

        /**  */
        public final String filename;
        /**  */
        public long size;
        /**  */
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

        /**
         * @return
         */
        public String getPropsName()
        {
            return filename + ".props";
        }

        /**
         * @param url
         * @return the build information for JUtils.
         */
        public void readInfo( URL url )
        {
            IntegerParser parser = new IntegerParser( 0, null );
            DateTimeFormatter dtf = TimeUtils.buildFormatter( TIME_DATE_FMT );

            try( InputStream stream = url.openStream() )
            {
                Properties props = new Properties();

                props.load( stream );

                String time = props.getProperty( "time" );
                String size = props.getProperty( "length" );

                this.time = LocalDateTime.parse( time, dtf );
                this.size = parser.parse( size );
            }
            catch( IOException ex )
            {
                LogUtils.printError(
                    "Unable to open resource information for %s: %s",
                    url.toString(), ex.getMessage() );
            }
            catch( ValidationException | DateTimeParseException ex )
            {
                LogUtils.printError( "Unable to parse resource information.",
                    ex );
            }
        }
    }
}
