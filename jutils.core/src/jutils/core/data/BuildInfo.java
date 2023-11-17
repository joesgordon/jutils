package jutils.core.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/*******************************************************************************
 * Defines an immutable build information object.
 ******************************************************************************/
public class BuildInfo
{
    /** The version of the build. */
    public final String version;
    /** The build date. */
    public final String buildDate;

    /***************************************************************************
     * Creates a new build information object with the provided version and
     * date.
     * @param version the version of the build.
     * @param date the build date.
     **************************************************************************/
    public BuildInfo( String version, String date )
    {
        this.version = version;
        this.buildDate = date;
    }

    /***************************************************************************
     * @return the build information for JUtils.
     **************************************************************************/
    public static BuildInfo load( URL url )
    {
        try( InputStream stream = url.openStream() )
        {
            Properties props = new Properties();

            props.load( stream );

            String version = props.getProperty( "version" );
            String date = props.getProperty( "buildtime" );
            String minor = props.getProperty( "minor.version" );
            String eng = props.getProperty( "eng.version" );

            if( minor != null )
            {
                version += "." + minor;
            }

            if( eng != null )
            {
                version += "/" + eng;
            }

            return new BuildInfo( version, date );
        }
        catch( IOException ex )
        {
            throw new IllegalStateException(
                "Unable to open/parse build information.", ex );
        }
    }
}
