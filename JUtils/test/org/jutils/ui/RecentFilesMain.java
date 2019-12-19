package org.jutils.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.model.IView;

/**
 *
 */
public class RecentFilesMain
{
    /**
     * @param args
     */
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( () -> createFrame() );
    }

    /**
     * @return
     */
    private static JFrame createFrame()
    {
        RecentFilesFrame frameView = new RecentFilesFrame();
        JFrame frame = frameView.getView();

        return frame;
    }

    /**
     *
     */
    private static final class RecentFilesFrame implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView frameView;
        /**  */
        private final RecentFilesViews recentFiles;

        /**
         * 
         */
        public RecentFilesFrame()
        {
            this.frameView = new StandardFrameView();
            this.recentFiles = new RecentFilesViews();

            frameView.setToolbar( createToolbar() );
            frameView.setTitle( "Test Application for Recent Files" );
            frameView.setSize( 500, 500 );
            frameView.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

            frameView.getFileMenu().add( recentFiles.getMenu(), 0 );
            frameView.getFileMenu().add( new JSeparator(), 1 );

            recentFiles.setData( createFileList() );
        }

        /**
         * @return
         */
        private List<File> createFileList()
        {
            List<File> files = new ArrayList<>();

            File homeDir = new File( System.getProperty( "user.home" ) );
            File rootDir = homeDir;

            File f;

            while( ( f = rootDir.getParentFile() ) != null )
            {
                rootDir = f;
            }

            listFiles( rootDir, files );

            return files;
        }

        /**
         * @param dir
         * @param files
         */
        private void listFiles( File dir, List<File> files )
        {
            if( files.size() > 4 )
            {
                return;
            }

            File [] fs = dir.listFiles();

            if( fs == null )
            {
                return;
            }

            for( File f : fs )
            {
                if( files.size() > 4 )
                {
                    return;
                }

                if( f.isDirectory() )
                {
                    listFiles( f, files );
                }
                else
                {
                    files.add( f );
                }
            }

        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            recentFiles.install( toolbar, ( e ) -> fileChosen() );

            return toolbar;
        }

        /**
         * @param f
         */
        private void fileChosen()
        {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame getView()
        {
            return frameView.getView();
        }
    }
}
