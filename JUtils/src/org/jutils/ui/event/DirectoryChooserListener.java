package org.jutils.ui.event;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.jutils.SwingUtils;
import org.jutils.io.IOUtils;
import org.jutils.ui.DirectoryChooser;
import org.jutils.ui.event.FileChooserListener.*;

/*******************************************************************************
 * Generic {@link ActionListener} for prompting a user for a directory.
 ******************************************************************************/
public class DirectoryChooserListener implements ActionListener
{
    /** The parent component of the dialog to be displayed. */
    private final Component parent;
    /** The title of the dialog to be displayed. */
    private final String title;
    /** The listener called when the directory is selected. Never null. */
    private final IFileSelected dirListener;
    /**
     * The listener called when multiple directories are selected. Never null.
     */
    private final IFilesSelected dirsListener;
    /** The callback that gets the last chosen directory. Never null. */
    private final ILastFile lastDir;
    /** The callback that gets the last chosen directories. Never null. */
    private final ILastFiles lastDirs;

    /***************************************************************************
     * Creates a new listener with the values:
     * @param parent the parent component of the dialog to be displayed.
     * @param title the title of the dialog to be displayed.
     * @param dirListener the listener called when a directory is selected.
     **************************************************************************/
    public DirectoryChooserListener( Component parent, String title,
        IFileSelected dirListener )
    {
        this( parent, title, dirListener, null );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param dirListener
     * @param lastDir
     **************************************************************************/
    public DirectoryChooserListener( Component parent, String title,
        IFileSelected dirListener, ILastFile lastDir )
    {
        this( parent, title, dirListener, null, lastDir, null );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param dirsListener
     **************************************************************************/
    public DirectoryChooserListener( Component parent, String title,
        IFilesSelected dirsListener )
    {
        this( parent, title, dirsListener, null );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param dirsListener
     * @param lastDirs
     **************************************************************************/
    public DirectoryChooserListener( Component parent, String title,
        IFilesSelected dirsListener, ILastFiles lastDirs )
    {
        this( parent, title, null, dirsListener, null, lastDirs );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param dirListener
     * @param dirsListener
     * @param lastDir
     * @param lastDirs
     **************************************************************************/
    private DirectoryChooserListener( Component parent, String title,
        IFileSelected dirListener, IFilesSelected dirsListener,
        ILastFile lastDir, ILastFiles lastDirs )
    {
        this.parent = parent;
        this.title = title;
        this.dirListener = dirListener == null ? IFileSelected.nullSelector()
            : dirListener;
        this.dirsListener = dirsListener == null ? IFilesSelected.nullSelector()
            : dirsListener;
        this.lastDir = lastDir == null ? ILastFile.nullSelector() : lastDir;
        this.lastDirs = lastDirs == null ? ILastFiles.nullSelector() : lastDirs;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        Window w = SwingUtils.getComponentsWindow( parent );
        DirectoryChooser chooser = new DirectoryChooser( w );

        chooser.setTitle( title );
        if( w != null )
        {
            chooser.setIconImages( w.getIconImages() );
        }

        String lastPaths = "";
        File lastFile = lastDir.getLastFile();
        if( lastFile != null )
        {
            lastPaths = lastFile.getAbsolutePath();
        }
        else
        {
            File [] lastFiles = lastDirs.getLastFiles();
            if( lastFiles != null && lastFiles.length > 0 )
            {
                lastPaths = IOUtils.getStringFromFiles( lastFiles );
            }
        }
        chooser.setSelectedPaths( lastPaths );

        chooser.setSize( 400, 500 );
        chooser.setVisible( true );

        File [] selected = chooser.getSelected();

        if( selected != null )
        {
            boolean eachIsDir = true;

            for( File file : selected )
            {
                if( !file.isDirectory() )
                {
                    eachIsDir = false;
                    break;
                }
            }

            if( eachIsDir )
            {
                dirListener.fileChosen( selected[0] );
                dirsListener.filesChosen( selected );
            }
        }
    }
}
