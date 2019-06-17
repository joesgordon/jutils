package org.jutils.ui.event;

import java.io.File;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.io.LogUtils;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.FileChooserListener.IFileSelected;
import org.jutils.ui.event.FileChooserListener.ILastFile;

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
        FrameRunner.invokeLater( new DirectoryChooserListenerApp() );
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
