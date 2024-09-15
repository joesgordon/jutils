package jutils.core;

import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jutils.core.data.BuildInfo;
import jutils.core.licensing.LicensesView;
import jutils.core.ui.BuildInfoView;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * Defines an application to display the current version of this library.
 ******************************************************************************/
public class JUtilsCoreMain
{
    /** The name of the file containing the JUtils build information. */
    private static final String FILE_NAME = "info.properties";

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private JUtilsCoreMain()
    {
    }

    /***************************************************************************
     * Defines the application entry point.
     * @param args ignored
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.DEFAULT_LAF = AppRunner.SIMPLE_LAF;

        AppRunner.invokeLater( () -> createFrame() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        JFrame frame = frameView.getView();

        frameView.setTitle( "JUtils Information" );
        frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frameView.setSize( 800, 800 );
        frameView.setContent( createContent() );

        frame.setIconImages( IconConstants.getAllImages( "jutils" ) );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static Container createContent()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        BuildInfoView buildView = new BuildInfoView();
        BuildInfo info = JUtilsCoreMain.load();
        LicensesView licensesView = new LicensesView();

        buildView.setData( info );

        panel.add( buildView.getView(), BorderLayout.NORTH );
        panel.add( licensesView.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * @return the build information for JUtils.
     **************************************************************************/
    public static BuildInfo load()
    {
        URL url = IconConstants.loader.getUrl( FILE_NAME );

        return BuildInfo.load( url );
    }
}
