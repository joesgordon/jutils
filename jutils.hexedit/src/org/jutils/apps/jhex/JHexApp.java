package org.jutils.apps.jhex;

import java.io.File;

import javax.swing.JFrame;

import org.jutils.apps.jhex.ui.JHexFrame;
import org.jutils.core.ui.app.IFrameApp;

/*******************************************************************************
 * Defines the {@link IFrameApp} that starts the JHex application.
 ******************************************************************************/
public class JHexApp implements IFrameApp
{
    /** The file to be loaded when the application is started. */
    private final File file;
    /** Closes the file when */
    private final boolean closeFileWithFrame;

    /** The view created by {@link #createFrame()}. */
    private JHexFrame view;

    /***************************************************************************
     * Creates a new app with no file that closes the file when the frame is
     * closed.
     **************************************************************************/
    public JHexApp()
    {
        this( null );
    }

    /***************************************************************************
     * Creates a new app with the provided file that closes the file when the
     * frame is closed.
     * @param file the file to be displayed when the application is started.
     **************************************************************************/
    public JHexApp( File file )
    {
        this( file, true );
    }

    /***************************************************************************
     * Creates a new app with the provided file that closes the file when the
     * frame is closed according to the provided parameter.
     * @param file the file to be displayed when the application is started.
     * @param closeFileWithFrame closes the file when the frame is closed if
     * {@code true}.
     **************************************************************************/
    public JHexApp( File file, boolean closeFileWithFrame )
    {
        this.file = file;
        this.closeFileWithFrame = closeFileWithFrame;
    }

    /***************************************************************************
     * Returns the created view.
     * @return the view created by {@link #createFrame()}.
     **************************************************************************/
    public JHexFrame getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        view = new JHexFrame( closeFileWithFrame );
        JFrame frame = view.getView();

        return frame;
    }

    /***************************************************************************
     * Displays the file specified if defined. {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
        if( file != null )
        {
            view.openFile( file );
        }
    }
}
