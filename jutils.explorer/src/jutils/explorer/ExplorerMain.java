package jutils.explorer;

import java.io.File;

import javax.swing.JFrame;

import jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class ExplorerMain
{
    /***************************************************************************
     * Create the Explorer frame.
     * @return
     **************************************************************************/
    public static JFrame createFrame()
    {
        ExplorerFrame frame = new ExplorerFrame();

        frame.setDirectory( new File( "/" ) );

        return frame.getView();
    }

    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( () -> createFrame() );
    }
}
