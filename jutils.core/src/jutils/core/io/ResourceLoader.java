package jutils.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import jutils.core.Utils;

/*******************************************************************************
 * A resource loader establishes a base path seeded with either a directory or a
 * class location. A resource loader provides access to items included with
 * source code.
 ******************************************************************************/
public class ResourceLoader
{
    /** The base path to which all resources are relative. */
    private final URL baseUrl;

    /***************************************************************************
     * Creates a new loader with the provided class and relative path. For
     * instance, you may have a list of icon names in SuperAwesomeIcon.java
     * sibling to a directory of icons named "icons".
     * @param baseClass a class close to the resources.
     * @param relPath the path from the class to the resources.
     **************************************************************************/
    public ResourceLoader( Class<?> baseClass, String relPath )
    {
        this( Utils.loadResourceURL( baseClass,
            relPath.endsWith( "/" ) ? relPath : relPath + "/" ) );
    }

    /***************************************************************************
     * Creates a new loader seeded with the provided directory.
     * @param directory the directory that contains the resources.
     **************************************************************************/
    public ResourceLoader( File directory )
    {
        this( IOUtils.fileToUrl( directory ) );
    }

    /***************************************************************************
     * Creates a loader seeded with the provided URL.
     * @param url the path that contains the resources.
     **************************************************************************/
    public ResourceLoader( URL url )
    {
        baseUrl = url;

        if( url == null )
        {
            throw new IllegalArgumentException(
                "Does not support null initialization" );
        }
    }

    /***************************************************************************
     * Builds a URL to a resource of the provided name. Name can be a relative
     * path (e.g. {@code sprites/angry_young_man.png}).
     * @param name the name/relative path to the resource.
     * @return the URL to the resource or null if the URL cannot be built.
     **************************************************************************/
    public URL getUrl( String name )
    {
        URL url = null;

        try
        {
            url = baseUrl.toURI().resolve( "./" + name ).toURL();
        }
        catch( URISyntaxException | MalformedURLException ex )
        {
            String err = String.format( "Unable to create URL using \"%s\"",
                name );
            throw new RuntimeException( err, ex );
        }

        return url;
    }

    /***************************************************************************
     * Creates an I/O stream for the provided resource name.
     * @param name the name/relative path of the resource.
     * @return the I/O stream for the resource.
     **************************************************************************/
    public InputStream getInputStream( String name )
    {
        InputStream stream = null;

        try
        {
            stream = getUrl( name ).openStream();
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

        return stream;
    }
}
