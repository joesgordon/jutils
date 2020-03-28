package org.jutils.core.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;

import javax.swing.*;

import org.jutils.core.data.UIProperty;
import org.jutils.core.ui.app.AppRunner;
import org.jutils.core.ui.app.IApplication;
import org.jutils.core.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VerboseMessageView implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JLabel shortMessageField;
    /**  */
    private final ScrollableEditorPaneView verboseMessageField;

    /***************************************************************************
     * 
     **************************************************************************/
    public VerboseMessageView()
    {
        this.shortMessageField = new JLabel();
        this.verboseMessageField = new ScrollableEditorPaneView();

        view = createView();

        verboseMessageField.setBackground(
            UIProperty.TEXTFIELD_INACTIVEBACKGROUND.getColor() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JScrollPane errorsPane = new JScrollPane(
            verboseMessageField.getView() );
        GridBagConstraints constraints;

        Dimension dim = new Dimension( 600, 200 );
        errorsPane.setPreferredSize( dim );
        errorsPane.setMinimumSize( dim );
        errorsPane.setMaximumSize( dim );

        verboseMessageField.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );
        verboseMessageField.setEditable( false );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 4, 4, 4, 4 ), 0, 0 );
        panel.add( shortMessageField, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 4, 4, 4 ), 0, 0 );
        panel.add( errorsPane, constraints );

        return panel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param shortMessage
     * @param verboseMessage
     **************************************************************************/
    public void setMessages( String shortMessage, String verboseMessage )
    {
        shortMessageField.setText( shortMessage );
        verboseMessageField.setText( verboseMessage );

        verboseMessageField.setCaretPosition( 0 );
    }

    /***************************************************************************
     * @param parent
     * @param title
     **************************************************************************/
    public void show( Component parent, String title )
    {
        show( parent, title, ModalityType.DOCUMENT_MODAL );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param modality
     **************************************************************************/
    public void show( Component parent, String title, ModalityType modality )
    {
        show( parent, title, modality, 600, 600 );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param width
     * @param height
     **************************************************************************/
    public void show( Component parent, String title, int width, int height )
    {
        show( parent, title, ModalityType.DOCUMENT_MODAL, width, height );
    }

    /***************************************************************************
     * @param parent
     * @param title
     * @param modality
     * @param width
     * @param height
     **************************************************************************/
    public void show( Component parent, String title, ModalityType modality,
        int width, int height )
    {
        OkDialogView dialogView = new OkDialogView( parent, getView(),
            modality );
        JDialog dialog = dialogView.getView();

        dialog.setTitle( title );
        dialog.setResizable( true );
        dialog.setSize( width, height );
        dialog.validate();
        dialog.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
        dialog.setLocationRelativeTo( parent );
        dialog.setVisible( true );
        dialog.toFront();
    }

    /***************************************************************************
     * @param shortMessage
     * @param verboseMessage
     **************************************************************************/
    public static void invoke( Component parent, String title,
        String shortMessage, String verboseMessage )
    {
        IApplication app = new VerboseMessageApp( parent, title, shortMessage,
            verboseMessage );
        AppRunner.invokeLater( app );
    }

    /***************************************************************************
     * @param shortMessage
     * @param verboseMessage
     **************************************************************************/
    public static void invokeAndWait( Component parent, String title,
        String shortMessage, String verboseMessage )
    {
        IApplication app = new VerboseMessageApp( parent, title, shortMessage,
            verboseMessage );
        AppRunner.invokeAndWait( app );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class VerboseMessageApp implements IApplication
    {
        private final Component parent;
        private final String title;
        private final String shortMessage;
        private final String verboseMessage;

        public VerboseMessageApp( Component parent, String title,
            String shortMessage, String verboseMessage )
        {
            this.parent = parent;
            this.title = title;
            this.shortMessage = shortMessage;
            this.verboseMessage = verboseMessage;
        }

        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        @Override
        public void createAndShowUi()
        {
            VerboseMessageView view = new VerboseMessageView();

            view.setMessages( shortMessage, verboseMessage );

            view.show( parent, title );
        }
    }
}
