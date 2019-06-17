package org.jutils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jutils.data.BuildInfo;
import org.jutils.ui.BuildInfoView;
import org.jutils.ui.app.AppRunner;
import org.jutils.ui.app.IApplication;

/*******************************************************************************
 * Defines an application to display the current version of this library.
 ******************************************************************************/
public class JUtilsInfoMain
{
    /** The name of the file containing the JUtils build information. */
    private static final String FILE_NAME = "info.properties";

    /***************************************************************************
     * Defines the application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new BuildInfoMainApp() );
    }

    /***************************************************************************
     * Defines the app that builds the UI to be displayed.
     **************************************************************************/
    private static final class BuildInfoMainApp implements IApplication
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void createAndShowUi()
        {
            BuildInfo info = JUtilsInfoMain.load();
            BuildInfoView.show( info );
        }
    }

    /***************************************************************************
     * @return the build information for JUtils.
     **************************************************************************/
    public static BuildInfo load()
    {
        URL url = IconConstants.loader.loader.getUrl( FILE_NAME );

        try( InputStream stream = url.openStream() )
        {
            Properties props = new Properties();

            props.load( stream );

            String version = props.getProperty( "version" );
            String date = props.getProperty( "buildtime" );
            String sub = props.getProperty( "subversion" );

            if( sub != null )
            {
                version += "." + sub;
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
