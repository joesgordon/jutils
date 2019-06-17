package org.jutils.ui;

import java.io.File;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.FileChooserListener;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.explorer.AppManagerView;
import org.jutils.ui.explorer.data.*;

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
        FrameRunner.invokeLater( new TestMainApp() );
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
