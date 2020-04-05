package org.jutils.core;

import java.net.URL;

import org.jutils.core.data.BuildInfo;
import org.jutils.core.ui.BuildInfoView;
import org.jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * Defines an application to display the current version of this library.
 ******************************************************************************/
public class JUtilsInfo
{
    /** The name of the file containing the JUtils build information. */
    private static final String FILE_NAME = "info.properties";

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private JUtilsInfo()
    {
    }

    /***************************************************************************
     * Defines the application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createAndShowUi() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void createAndShowUi()
    {
        BuildInfo info = JUtilsInfo.load();
        BuildInfoView.show( info );
    }

    /***************************************************************************
     * @return the build information for JUtils.
     **************************************************************************/
    public static BuildInfo load()
    {
        URL url = IconConstants.loader.loader.getUrl( FILE_NAME );

        return BuildInfo.load( url );
    }
}
