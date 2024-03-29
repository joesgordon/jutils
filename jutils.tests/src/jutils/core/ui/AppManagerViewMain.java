package jutils.core.ui;

import java.io.File;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.FileChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.explorer.AppManagerView;
import jutils.core.ui.explorer.data.AppManagerConfig;
import jutils.core.ui.explorer.data.ApplicationConfig;
import jutils.core.ui.explorer.data.ExtensionConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppManagerViewMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new TestMainApp() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static AppManagerConfig getUnitTestData()
    {
        AppManagerConfig configData = new AppManagerConfig();
        ApplicationConfig pgm = null;
        ExtensionConfig ext = null;

        // Create java files and associated programs.

        ext = new ExtensionConfig( "java", "Java Source File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = ( "gedit" );
        pgm.path = new File( "/usr/bin/gedit" );
        pgm.args = ( "-hoopde" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        // Create txt files and associated programs.

        ext = new ExtensionConfig( "txt", "Ascii Text File" );
        configData.exts.add( ext );

        pgm = new ApplicationConfig();
        pgm.name = "file-roller";
        pgm.path = new File( "/usr/bin/file-roller" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        pgm = new ApplicationConfig();
        pgm.name = "Firefox";
        pgm.path = new File( "/usr/bin/firefox" );

        configData.apps.add( pgm );
        ext.apps.add( pgm.name );

        return configData;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class TestMainApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            StandardFrameView frameView = new StandardFrameView();

            AppManagerView appmanView = new AppManagerView();
            appmanView.setData( getUnitTestData() );

            frameView.setTitle( "File Config Test" );
            frameView.setToolbar( createToolbar( appmanView ) );
            frameView.setContent( appmanView.getView() );
            frameView.setSize( 600, 550 );

            return frameView.getView();
        }

        @Override
        public void finalizeGui()
        {
        }

        private static JToolBar createToolbar( AppManagerView appmanView )
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar,
                createOpenAction( appmanView ) );
            SwingUtils.addActionToToolbar( toolbar,
                createSaveAction( appmanView ) );

            return toolbar;
        }

        /**
         * @param appmanView
         * @return
         */
        private static Action createOpenAction( AppManagerView appmanView )
        {
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
            IFileSelected ifs = ( f ) -> appmanView.openFile( f );
            FileChooserListener fcl = new FileChooserListener(
                appmanView.getView(), "Open Application Manager Configuration",
                false, ifs );
            return new ActionAdapter( fcl, "Open", icon );
        }

        /**
         * @param appmanView
         * @return
         */
        private static Action createSaveAction( AppManagerView appmanView )
        {
            Icon icon = IconConstants.getIcon( IconConstants.SAVE_16 );
            IFileSelected ifs = ( f ) -> appmanView.saveFile( f );
            FileChooserListener fcl = new FileChooserListener(
                appmanView.getView(), "Save Application Manager Configuration",
                true, ifs );
            return new ActionAdapter( fcl, "Save", icon );
        }
    }
}
