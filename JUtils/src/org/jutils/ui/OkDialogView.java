package org.jutils.ui;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * Represents a view that creates a dialog with an OK button at the bottom and
 * the content that the user provides above.
 ******************************************************************************/
public class OkDialogView implements IView<JDialog>
{
    /** The dialog to be shown. */
    private final JDialog dialog;
    /**  */
    private final JButton okButton;
    /**  */
    private final JButton cancelButton;
    /**  */
    private final JButton applyButton;
    /**
     * The listeners to be called when the dialog is closed by either the ok
     * button or the dialog's 'x' button.
     */
    private final ItemActionList<Boolean> okListeners;
    /**  */
    private final OkDialogButtons buttons;

    /***************************************************************************
     * Creates a {@link ModalityType#APPLICATION_MODAL} dialog whose owner is
     * the window containing the provided component.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param content the component to be displayed in this dialog.
     **************************************************************************/
    public OkDialogView( Component parent, Component content )
    {
        this( parent, content, ModalityType.DOCUMENT_MODAL );
    }

    /***************************************************************************
     * Creates a dialog with the provided {@link ModalityType} whose owner is
     * the window containing the provided component and displays an Ok button.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        ModalityType modalityType )
    {
        this( parent, content, modalityType, OkDialogButtons.OK_ONLY );
    }

    /***************************************************************************
     * Creates a dialog whose owner is the window containing the provided
     * {@code parent} and displays the provided button(s). The modality type is
     * set according to {@link OkDialogButtons}. To override the default
     * modality type, use
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param content the component to be displayed in this dialog.
     * @param buttons defines the buttons to be shown.
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        OkDialogButtons buttons )
    {
        this( parent, content, buttons.getModalityType(), buttons );
    }

    /***************************************************************************
     * Creates a dialog with the provided {@link ModalityType} whose owner is
     * the window containing the provided {@code parent} and displays the
     * provided button(s).
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     * @param buttons defines the buttons to be shown.
     **************************************************************************/
    public OkDialogView( Component parent, Component content,
        ModalityType modalityType, OkDialogButtons buttons )
    {
        this( SwingUtils.getComponentsWindow( parent ), content, modalityType,
            buttons );
    }

    /***************************************************************************
     * Creates a {@link ModalityType#APPLICATION_MODAL} dialog with the provided
     * owner.
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     **************************************************************************/
    public OkDialogView( Window owner, Component content )
    {
        this( owner, content, ModalityType.DOCUMENT_MODAL );
    }

    /***************************************************************************
     * Creates a dialog with the provided {@link ModalityType} and owner.
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        ModalityType modalityType )
    {
        this( owner, content, modalityType, OkDialogButtons.OK_ONLY );
    }

    /***************************************************************************
     * Creates a dialog with the provided buttons and owner. The modality type
     * is set according to {@link OkDialogButtons}. To override the default
     * modality type, use
     * {@link #OkDialogView(Window, Component, ModalityType, OkDialogButtons)}.
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     * @param buttons defines the buttons to be shown.
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        OkDialogButtons buttons )
    {
        this( owner, content, buttons.getModalityType(), buttons );
    }

    /***************************************************************************
     * @param owner the owner of this dialog.
     * @param content the component to be displayed in this dialog.
     * @param modalityType the modality of this dialog.
     * @param buttons defines the buttons to be shown.
     **************************************************************************/
    public OkDialogView( Window owner, Component content,
        ModalityType modalityType, OkDialogButtons buttons )
    {
        this.okListeners = new ItemActionList<>();
        this.dialog = new JDialog( owner, modalityType );
        this.okButton = new JButton();
        this.cancelButton = new JButton();
        this.applyButton = new JButton();
        this.buttons = buttons;

        dialog.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );

        dialog.addWindowListener( new DialogListener( this ) );

        dialog.setContentPane( createContentPane( content ) );

        SwingUtils.installEscapeCloseOperation( dialog );

        if( buttons.hasApply )
        {
            dialog.getRootPane().setDefaultButton( applyButton );
        }
        else
        {
            dialog.getRootPane().setDefaultButton( okButton );
        }

        if( owner != null )
        {
            dialog.setIconImages( owner.getIconImages() );
        }
    }

    /***************************************************************************
     * @param title
     **************************************************************************/
    public void setTitle( String title )
    {
        dialog.setTitle( title );
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setOkButtonText( String text )
    {
        okButton.setText( text );
    }

    /***************************************************************************
     * @param icon
     **************************************************************************/
    public void setOkButtonIcon( Icon icon )
    {
        okButton.setIcon( icon );
    }

    /***************************************************************************
     * @param text
     * @param icon
     **************************************************************************/
    public void setOkButtonText( String text, Icon icon )
    {
        okButton.setText( text );
        okButton.setIcon( icon );
    }

    /***************************************************************************
     * Adds the provided listener to the list of listeners to be called when the
     * dialog is closed. The boolean will be {@code true} if the ok button was
     * pressed and {@code false} if the cancel button was pressed or the dialog
     * is closed via the close button.
     * @param l the listener to be added.
     **************************************************************************/
    public void addOkListener( ItemActionListener<Boolean> l )
    {
        okListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JDialog getView()
    {
        return dialog;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private boolean showDialog()
    {
        StateListener listener = new StateListener();

        addOkListener( listener );

        dialog.setLocationRelativeTo( dialog.getParent() );
        dialog.setVisible( true );

        okListeners.removeListener( listener );

        return listener.getSelection();
    }

    /***************************************************************************
     * Shows the dialog after packing.
     * @return {@code true} if the ok button was pressed, false otherwise.
     **************************************************************************/
    public boolean show()
    {
        return show( null );
    }

    /***************************************************************************
     * Shows the dialog after setting the provided size.
     * @param width the desired width of the dialog.
     * @param height the desired height of the dialog.
     * @return {@code true} if the ok button was pressed, false otherwise.
     **************************************************************************/
    public boolean show( int width, int height )
    {
        return show( new Dimension( width, height ) );
    }

    /***************************************************************************
     * Shows the dialog after setting the provided size.
     * @param size the desired size of the dialog shown or {@code null} to pack.
     * @return {@code true} if the ok button was pressed, false otherwise.
     **************************************************************************/
    public boolean show( Dimension size )
    {
        if( size == null )
        {
            dialog.pack();
        }
        else
        {
            dialog.setSize( size );
            dialog.validate();
        }

        return showDialog();
    }

    /***************************************************************************
     * Shows the dialog after setting the provided parameters.
     * @param title the text displayed in the dialog's border; a {@code null}
     * value results in an empty title.
     * @param size the desired size of the dialog shown or {@code null} to pack.
     * @return {@code true} if the ok button was pressed, false otherwise.
     **************************************************************************/
    public boolean show( String title, Dimension size )
    {
        dialog.setTitle( title );

        return show( size );
    }

    /***************************************************************************
     * Shows the dialog after setting the provided parameters.
     * @param title the text displayed in the dialog's border; a {@code null}
     * value results in an empty title.
     * @param iconImages the list of icons displayed when asked by the system.
     * @param size the desired size of the dialog shown or {@code null} to pack.
     * @return {@code true} if the ok button was pressed, false otherwise.
     **************************************************************************/
    public boolean show( String title, List<Image> iconImages, Dimension size )
    {
        dialog.setIconImages( iconImages );

        return show( title, size );
    }

    /***************************************************************************
     * Sets the size of the dialog to the provided parameters.
     * @param width the width of the dialog shown.
     * @param height the height of the dialog shown.
     **************************************************************************/
    public void setSize( int width, int height )
    {
        dialog.setSize( width, height );
    }

    /***************************************************************************
     * Creates the content pane for this dialog.
     * @param content the user content to be displayed.
     * @return
     **************************************************************************/
    private Container createContentPane( Component content )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( content, constraints );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( createButtonPanel(), constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the button panel for this dialog.
     * @return
     **************************************************************************/
    private Component createButtonPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        okButton.setText( "OK" );
        // okButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        okButton.addActionListener( ( e ) -> handleButtonAction( true, true ) );

        cancelButton.setText( "Cancel" );
        // cancelButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        cancelButton.addActionListener(
            ( e ) -> handleButtonAction( true, false ) );

        applyButton.setText( "Apply" );
        // applyButton.setIcon( IconConstants.loader.getIcon(
        // IconConstants.CHECK_16 ) );
        applyButton.addActionListener(
            ( e ) -> handleButtonAction( false, true ) );

        SwingUtils.setMaxComponentSize( okButton, cancelButton, applyButton );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( Box.createHorizontalStrut( 0 ), constraints );

        if( buttons.hasOk )
        {
            constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 8, 8, 8, 8 ), 50, 5 );
            panel.add( okButton, constraints );
        }

        if( buttons.hasCancel )
        {
            constraints = new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 8, 0, 8, 8 ), 50, 5 );
            panel.add( cancelButton, constraints );
        }

        if( buttons.hasApply )
        {
            constraints = new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 8, 0, 8, 8 ), 50, 5 );
            panel.add( applyButton, constraints );
        }

        return panel;
    }

    /***************************************************************************
     * @param closeDialog
     * @param okIndicated
     **************************************************************************/
    private void handleButtonAction( boolean closeDialog, boolean okIndicated )
    {
        okListeners.fireListeners( this, okIndicated );

        if( closeDialog )
        {
            dialog.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void pack()
    {
        dialog.pack();
    }

    /***************************************************************************
     * Added because <a
     * href="http://stackoverflow.com/questions/2873449">bug</a>
     **************************************************************************/
    private static class DialogListener extends WindowAdapter
    {
        /**  */
        private final OkDialogView view;

        /**
         * @param view
         */
        public DialogListener( OkDialogView view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowClosing( WindowEvent e )
        {
            view.handleButtonAction( true, false );
        }
    }

    /***************************************************************************
     * Defines the button(s) to be shown in an {@link OkDialogView}. Each button
     * type defines a default modality of {@link ModalityType#MODELESS} when an
     * "Apply" is shown and {@link ModalityType#DOCUMENT_MODAL} otherwise.
     **************************************************************************/
    public static enum OkDialogButtons
    {
        /**  */
        OK_ONLY( true, false, false ),
        /**  */
        OK_CANCEL( true, false, true ),
        /**  */
        OK_APPLY_CANCEL( true, true, true ),
        /**  */
        OK_APPLY( true, true, false );

        /**  */
        public final boolean hasOk;
        /**  */
        public final boolean hasApply;
        /**  */
        public final boolean hasCancel;

        /**
         * @param hasOk
         * @param hasApply
         * @param hasCancel
         */
        private OkDialogButtons( boolean hasOk, boolean hasApply,
            boolean hasCancel )
        {
            this.hasOk = hasOk;
            this.hasApply = hasApply;
            this.hasCancel = hasCancel;
        }

        /**
         * @return
         */
        public ModalityType getModalityType()
        {
            return hasApply ? ModalityType.MODELESS
                : ModalityType.DOCUMENT_MODAL;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class StateListener implements ItemActionListener<Boolean>
    {
        /**  */
        private boolean selection;

        /**
         * 
         */
        public StateListener()
        {
            this.selection = false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            this.selection = event.getItem();
        }

        /**
         * @return
         */
        public boolean getSelection()
        {
            return this.selection;
        }
    }
}
