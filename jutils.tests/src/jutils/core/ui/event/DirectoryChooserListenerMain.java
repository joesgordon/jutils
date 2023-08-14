package jutils.core.ui.event;

import java.io.File;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import jutils.core.IconConstants;
import jutils.core.SwingUtils;
import jutils.core.io.LogUtils;
import jutils.core.ui.JGoodiesToolBar;
import jutils.core.ui.StandardFrameView;
import jutils.core.ui.app.AppRunner;
import jutils.core.ui.app.IFrameApp;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.DirectoryChooserListener;
import jutils.core.ui.event.FileChooserListener.IFileSelected;
import jutils.core.ui.event.FileChooserListener.ILastFile;

/**
 *
 */
public class DirectoryChooserListenerMain
{
    /**
     * @param args
     */
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new DirectoryChooserListenerApp() );
    }

    /**
     *
     */
    private static final class DirectoryChooserListenerApp implements IFrameApp
    {
        /**  */
        private static final String TITLE = "Directory Chooser Listener App";

        private StandardFrameView frameView;

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            frameView = new StandardFrameView();

            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frameView.setToolbar( createToolbar() );
            frameView.setSize( 500, 500 );
            frameView.setTitle( TITLE );

            return frameView.getView();
        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            SwingUtils.addActionToToolbar( toolbar, createOpenAction() );

            return toolbar;
        }

        /**
         * @return
         */
        private Action createOpenAction()
        {
            IFileSelected fileSelected = ( f ) -> openFile( f );
            ILastFile lastFile = null;
            DirectoryChooserListener listener = new DirectoryChooserListener(
                frameView.getView(), "Open Directory", fileSelected, lastFile );
            Icon icon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );
            return new ActionAdapter( listener, "Open", icon );
        }

        /**
         * @param f
         * @return
         */
        private static void openFile( File f )
        {
            LogUtils.printDebug( "File chosen: " + f );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }
}
