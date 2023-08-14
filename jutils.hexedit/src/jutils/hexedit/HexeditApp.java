package jutils.hexedit;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.ui.app.IFrameApp;
import jutils.hexedit.ui.HexeditFrame;

/*******************************************************************************
 * Defines the {@link IFrameApp} that starts the Hexedit application.
 ******************************************************************************/
public class HexeditApp implements IFrameApp
{
    /** The file to be loaded when the application is started. */
    private final File file;
    /** Closes the file when */
    private final boolean closeFileWithFrame;

    /** The view created by {@link #createFrame()}. */
    private HexeditFrame view;

    /***************************************************************************
     * Creates a new app with no file that closes the file when the frame is
     * closed.
     **************************************************************************/
    public HexeditApp()
    {
        this( null );
    }

    /***************************************************************************
     * Creates a new app with the provided file that closes the file when the
     * frame is closed.
     * @param file the file to be displayed when the application is started.
     **************************************************************************/
    public HexeditApp( File file )
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
    public HexeditApp( File file, boolean closeFileWithFrame )
    {
        this.file = file;
        this.closeFileWithFrame = closeFileWithFrame;
    }

    /***************************************************************************
     * Returns the created view.
     * @return the view created by {@link #createFrame()}.
     **************************************************************************/
    public HexeditFrame getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        view = new HexeditFrame( closeFileWithFrame );
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
