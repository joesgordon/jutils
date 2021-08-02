package org.jutils.core.task;

import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;

import org.jutils.core.Utils;
import org.jutils.core.ui.OkDialogView;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.app.AppRunner;
import org.jutils.core.ui.app.IApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskViewApp implements IApplication
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getLookAndFeelName()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void createAndShowUi()
    {
        MultiTaskView view = new MultiTaskView();

        String message;
        ITaskView taskView;

        message = "Set 7 of 120: 24 Parameters" + Utils.NEW_LINE;
        message += "A01, A02, A03, A04, A05, A06, A07, A08, A09, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21, A22, A23, A24";
        taskView = view.addTaskView( message );
        taskView.signalPercent( 34 );

        message = "Set 6 of 120: 21 Parameters" + Utils.NEW_LINE;
        message += "G01, G02, G03, G04, G05, G06, G07, G08, G09, G10, G11, G12, G13, G14, G15, G16, G17, G18, G19, G20, G21";
        taskView = view.addTaskView( message );
        taskView.signalPercent( 10 );

        view.setTitle( "Sets 5 of 120 completed" );
        view.setPercent( 500 / 120 );

        OkDialogView okView = new OkDialogView( null, view.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_CANCEL );
        JDialog dialog = okView.getView();

        dialog.setTitle( "Decoding 2849 parameters" );
        dialog.setSize( 400, 400 );
        dialog.setLocationRelativeTo( null );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new MultiTaskViewApp() );
    }
}
