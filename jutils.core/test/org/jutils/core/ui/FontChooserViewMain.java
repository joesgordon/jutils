package org.jutils.core.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.jutils.core.ui.FontChooserView;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.app.AppRunner;

/*******************************************************************************
 * Test UI for {@link FontChooserView}.
 ******************************************************************************/
public class FontChooserViewMain
{
    /***************************************************************************
     * Main entry point for the test application.
     * @param args ignored.
     **************************************************************************/
    public static void main( String args[] )
    {
        AppRunner.invokeLater( () -> createAndShow() );
    }

    /***************************************************************************
     * Creates and displays the test dialog.
     * @return the new test frame.
     **************************************************************************/
    private static JDialog createAndShow()
    {
        FontChooserView chooser = new FontChooserView();
        OkDialogView dialogView = new OkDialogView( null, chooser.getView() );
        JDialog dlg = dialogView.getView();
        dlg.addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                System.exit( 0 );
            }
        } );
        dlg.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dlg.pack();
        dlg.setLocationByPlatform( true );
        dlg.setVisible( true );

        return dlg;
    }
}
