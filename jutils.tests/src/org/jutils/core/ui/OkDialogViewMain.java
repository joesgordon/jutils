package org.jutils.core.ui;

import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jutils.core.IconConstants;
import org.jutils.core.OptionUtils;
import org.jutils.core.SwingUtils;
import org.jutils.core.ui.OkDialogView.OkDialogButtons;
import org.jutils.core.ui.app.AppRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.event.ActionAdapter;
import org.jutils.core.ui.fields.ComboFormField;
import org.jutils.core.ui.fields.StringFormField;
import org.jutils.core.ui.model.IView;

/**
 *
 */
public class OkDialogViewMain
{
    /**
     * @param args
     */
    public static void main( String [] args )
    {
        AppRunner.invokeLater( new OkDialogViewApp(), false );
    }

    /**
     *
     */
    private static class OkDialogViewApp implements IFrameApp
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            OkDialogViewFrame frame = new OkDialogViewFrame();

            return frame.getView();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    private static class OkDialogViewFrame implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView frame;
        /**  */
        private final ComboFormField<ModalityType> modalityField;
        /**  */
        private final ComboFormField<OkDialogButtons> buttonsField;
        /**  */
        private final StringFormField titleField;
        /**  */
        private final StringFormField okTextField;

        /**
         * 
         */
        public OkDialogViewFrame()
        {
            this.frame = new StandardFrameView();

            this.modalityField = new ComboFormField<>( "Modality Type",
                ModalityType.values() );
            this.buttonsField = new ComboFormField<>( "Buttons Type",
                OkDialogButtons.values() );
            this.titleField = new StringFormField( "Title", 0, 200 );
            this.okTextField = new StringFormField( "Ok Button Text", 1, 20 );

            this.modalityField.setValue( ModalityType.DOCUMENT_MODAL );
            this.buttonsField.setValue( OkDialogButtons.OK_APPLY_CANCEL );
            this.titleField.setValue( "Title" );
            this.okTextField.setValue( "Yep" );

            frame.setTitle( "OkDialogView Test App" );
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setToolbar( createToolbar() );
            frame.setContent( createContent() );
        }

        /**
         * @return
         */
        private JToolBar createToolbar()
        {
            JToolBar toolbar = new JGoodiesToolBar();

            ActionListener listener = ( e ) -> showDialog();
            Icon icon = IconConstants.getIcon( IconConstants.LAUNCH_16 );
            Action action = new ActionAdapter( listener, "Show Dialog", icon );

            SwingUtils.addActionToToolbar( toolbar, action );

            return toolbar;
        }

        /**
         * @return
         */
        private Container createContent()
        {
            StandardFormView form = new StandardFormView();

            form.addField( modalityField );
            form.addField( buttonsField );
            form.addField( titleField );
            form.addField( okTextField );

            return form.getView();
        }

        /**
         * 
         */
        private void showDialog()
        {
            OkDialogView view = new OkDialogView( getView(), new JPanel(),
                modalityField.getValue(), buttonsField.getValue() );

            view.setOkButtonText( okTextField.getValue() );
            view.setTitle( titleField.getValue() );
            view.addOkListener(
                ( e ) -> notifyUser( "Ok Listener Called: " + e.getItem() ) );

            if( view.show() )
            {
                notifyUser( "OK Pressed" );
            }
        }

        /**
         * @param message
         */
        private void notifyUser( String message )
        {
            OptionUtils.showInfoMessage( getView(), message, "INFO" );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame getView()
        {
            return frame.getView();
        }
    }
}
