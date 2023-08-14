package jutils.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jutils.core.io.IParser;
import jutils.core.ui.StandardFormView;
import jutils.core.ui.fields.ComboFormField;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.ParserFormField;
import jutils.core.ui.model.IDataView;

/*******************************************************************************
 * Utility class for showing {@link JOptionPane}s. All functions use
 * {@link ModalityType#DOCUMENT_MODAL} unless otherwise noted.
 ******************************************************************************/
public class OptionUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private OptionUtils()
    {
    }

    /***************************************************************************
     * Shows the provided message in a {@link ModalityType#DOCUMENT_MODAL}
     * JDialog with an error icon associated with the provided message type.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog displayed.
     * @param messageType the type of message to be displayed:
     * {@link JOptionPane#ERROR_MESSAGE},
     * {@link JOptionPane#INFORMATION_MESSAGE},
     * {@link JOptionPane#WARNING_MESSAGE},
     * {@link JOptionPane#QUESTION_MESSAGE},or {@link JOptionPane#PLAIN_MESSAGE}
     * @return the option pane shown.
     **************************************************************************/
    private static JOptionPane showMessage( Component parent, Object message,
        String title, int messageType )
    {
        return showOptionPane( parent, message, title, messageType,
            JOptionPane.DEFAULT_OPTION, null, null, null );
    }

    /***************************************************************************
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog displayed.
     * @param messageType the type of message to be displayed:
     * {@link JOptionPane#ERROR_MESSAGE},
     * {@link JOptionPane#INFORMATION_MESSAGE},
     * {@link JOptionPane#WARNING_MESSAGE},
     * {@link JOptionPane#QUESTION_MESSAGE}, or
     * {@link JOptionPane#PLAIN_MESSAGE}.
     * @param optionType the choices the user can select:
     * {@link JOptionPane#OK_OPTION}, {@link JOptionPane#OK_CANCEL_OPTION},
     * {@link JOptionPane#YES_NO_OPTION}, or
     * {@link JOptionPane#YES_NO_CANCEL_OPTION}.
     * @param icon the Icon image to display or the stock icon associated with
     * the specified message type if {@code null}.
     * @param options the choices the user can select
     * @param initialValue the choice that is initially selected; if null, then
     * nothing will be initially selected;only meaningful if options is used
     * @return the option pane shown.
     **************************************************************************/
    public static JOptionPane showOptionPane( Component parent, Object message,
        String title, int messageType, int optionType, Icon icon,
        Object [] options, Object initialValue )
    {
        JOptionPane jop = new JOptionPane( message, messageType, optionType,
            icon, options, initialValue );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );

        dialog.setVisible( true );

        return jop;
    }

    /***************************************************************************
     * Shows an option pane with a combo box displaying the provided items.
     * @param <T> the type of items the user will choose.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the user message to be displayed over the combo box.
     * @param title the title of the dialog.
     * @param name the name of the field to be displayed to the left of the
     * combo box.
     * @param items the items to be shown in the combo box.
     * @param initialValue the default selection of the combo box.
     * @return the value selected or {@code null} if cancelled.
     **************************************************************************/
    public static <T> T showComboDialog( Component parent, String message,
        String title, String name, T [] items, T initialValue )
    {
        return showComboDialog( parent, message, title, name, items,
            initialValue, StockIcon.QUESTION );
    }

    /***************************************************************************
     * @param <T> the type of items the user will choose.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the user message to be displayed over the combo box.
     * @param title the title of the dialog.
     * @param name the name of the field to be displayed to the left of the
     * combo box.
     * @param items the items to be shown in the combo box.
     * @param initialValue the default selection of the combo box.
     * @param icon the stock icon to be shown to the left of the text and field.
     * @return the value selected or {@code null} if cancelled.
     **************************************************************************/
    public static <T> T showComboDialog( Component parent, String message,
        String title, String name, T [] items, T initialValue, StockIcon icon )
    {
        return showComboDialog( parent, message, title, name, items,
            initialValue, icon.messageType, null );
    }

    /***************************************************************************
     * @param <T> the type of items the user will choose.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the user message to be displayed over the combo box.
     * @param title the title of the dialog.
     * @param name the name of the field to be displayed to the left of the
     * combo box.
     * @param items the items to be shown in the combo box.
     * @param initialValue the default selection of the combo box.
     * @param icon the stock icon to be shown to the left of the text and field.
     * @return the value selected or {@code null} if cancelled.
     **************************************************************************/
    public static <T> T showComboDialog( Component parent, String message,
        String title, String name, T [] items, T initialValue, Icon icon )
    {
        return showComboDialog( parent, message, title, name, items,
            initialValue, JOptionPane.INFORMATION_MESSAGE, icon );
    }

    /***************************************************************************
     * @param <T> the type of items the user will choose.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the user message to be displayed over the combo box.
     * @param title the title of the dialog.
     * @param name the name of the field to be displayed to the left of the
     * combo box.
     * @param items the items to be shown in the combo box.
     * @param initialValue the default selection of the combo box.
     * @param messageType the type of message to be displayed:
     * {@link JOptionPane#ERROR_MESSAGE},
     * {@link JOptionPane#INFORMATION_MESSAGE},
     * {@link JOptionPane#WARNING_MESSAGE},
     * {@link JOptionPane#QUESTION_MESSAGE}, or
     * {@link JOptionPane#PLAIN_MESSAGE}.
     * @param icon the custom icon to be shown to the left of the text and
     * field.
     * @return the value selected or {@code null} if cancelled.
     **************************************************************************/
    private static <T> T showComboDialog( Component parent, String message,
        String title, String name, T [] items, T initialValue, int messageType,
        Icon icon )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StandardFormView form = new StandardFormView();
        ComboFormField<T> field = new ComboFormField<T>( name, items );

        field.setValue( initialValue );

        form.addField( field );

        panel.add( new JLabel( message ), BorderLayout.NORTH );
        panel.add( form.getView(), BorderLayout.CENTER );

        JOptionPane jop = new JOptionPane( panel, messageType,
            JOptionPane.OK_CANCEL_OPTION, icon, null, null );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setVisible( true );

        T ans = null;
        Object value = jop.getValue();

        if( value instanceof Integer &&
            ( Integer )value == JOptionPane.OK_OPTION )
        {
            ans = field.getValue();
        }

        return ans;
    }

    /***************************************************************************
     * Shows the provided message in a {@link ModalityType#DOCUMENT_MODAL}
     * JDialog with an error icon.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog displayed.
     **************************************************************************/
    public static void showErrorMessage( Component parent, Object message,
        String title )
    {
        showMessage( parent, message, title, JOptionPane.ERROR_MESSAGE );
    }

    /***************************************************************************
     * Shows the provided message in a {@link ModalityType#DOCUMENT_MODAL}
     * JDialog with an information icon.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog displayed.
     **************************************************************************/
    public static void showInfoMessage( Component parent, Object message,
        String title )
    {
        showMessage( parent, message, title, JOptionPane.INFORMATION_MESSAGE );
    }

    /***************************************************************************
     * Shows the provided message in a {@link ModalityType#DOCUMENT_MODAL}
     * JDialog with an warning icon.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog displayed.
     **************************************************************************/
    public static void showWarningMessage( Component parent, Object message,
        String title )
    {
        showMessage( parent, message, title, JOptionPane.WARNING_MESSAGE );
    }

    /***************************************************************************
     * Displays an option dialog with a question icon, OK/Cancel buttons, the
     * provided message, title, and choices.
     * @param <T> the data type of the choices presented.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed in the dialog.
     * @param title the title of the dialog.
     * @param choices the choices to be displayed in combo box.
     * @param defaultChoice the choice to be selected by default.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static <T> String showEditableMessage( Component parent,
        String message, String title, T [] choices, T defaultChoice )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel msgLabel = new JLabel( message );
        JComboBox<T> nameField = new JComboBox<>( choices );
        GridBagConstraints constraints;

        Object ans;
        String name;

        // ---------------------------------------------------------------------
        // Build message UI.
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        panel.add( msgLabel, constraints );

        nameField.setEditable( true );
        nameField.setSelectedItem( defaultChoice );

        constraints = new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( nameField, constraints );

        // ---------------------------------------------------------------------
        // Prompt user.
        // ---------------------------------------------------------------------
        JOptionPane jop = new JOptionPane( panel, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, null, null, null );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setResizable( true );
        dialog.setVisible( true );

        ans = jop.getValue();

        name = nameField.getSelectedItem().toString();

        if( ans != null )
        {
            if( ans instanceof Integer )
            {
                int ians = ( Integer )ans;
                if( ians != JOptionPane.OK_OPTION )
                {
                    name = null;
                }
            }
            else
            {
                name = null;
            }
        }
        else
        {
            name = null;
        }

        return name;
    }

    /***************************************************************************
     * Displays an question dialog with the provided message, title, and button
     * choices.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the String or JComponent to be displayed in the dialog.
     * @param title the title of the dialog.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static boolean showConfirmMessage( Component parent, Object message,
        String title )
    {
        int result = JOptionPane.showConfirmDialog( parent, message, title,
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE );

        if( result == JOptionPane.OK_OPTION )
        {
            return true;
        }

        return false;
    }

    /***************************************************************************
     * Displays an question dialog with the provided message, title, and options
     * as individual buttons.
     * @param <T> the data type of the options presented.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the String or JComponent to be displayed in the dialog.
     * @param title the title of the dialog.
     * @param options the choices to be displayed in buttons.
     * @param defaultOption the choice to be selected by default.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static <T> T showOptionMessage( Component parent, Object message,
        String title, T [] options, T defaultOption )
    {
        return showOptionMessage( parent, message, title, options,
            defaultOption, false );
    }

    /***************************************************************************
     * Displays an question dialog with the provided message, title, and options
     * as individual buttons.
     * @param <T> the data type of the options presented.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog.
     * @param options the choices to be displayed in buttons.
     * @param defaultOption the choice to be selected by default.
     * @param resizable indicates that the dialog is resizable with
     * {@code true}.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static <T> T showOptionMessage( Component parent, Object message,
        String title, T [] options, T defaultOption, boolean resizable )
    {
        JOptionPane jop = new JOptionPane( message,
            JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
            options, defaultOption );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setResizable( resizable );
        dialog.setVisible( true );

        Object result = jop.getValue();

        try
        {
            @SuppressWarnings( "unchecked")
            T ans = ( T )result;
            return ans;
        }
        catch( ClassCastException ex )
        {
        }

        return null;
    }

    /***************************************************************************
     * Displays an OK/Cancel dialog with the provided message, title, and data
     * view.
     * @param <T> the type of data displayed in the provided view.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed above the data view.
     * @param title the title of the dialog.
     * @param field the data field to be displayed.
     * @return the edited data value or null if the dialog was cancelled or
     * closed.
     **************************************************************************/
    public static <T> T showQuestionField( Component parent, String message,
        String title, IDataFormField<T> field )
    {
        JPanel panel = new JPanel( new BorderLayout() );
        StandardFormView form = new StandardFormView();

        form.addField( field );

        panel.add( new JLabel( message ), BorderLayout.NORTH );
        panel.add( form.getView(), BorderLayout.CENTER );

        JOptionPane jop = new JOptionPane( panel, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, null, null, null );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setVisible( true );

        // ---------------------------------------------------------------------
        // Prompt user.
        // ---------------------------------------------------------------------
        Object ans = jop.getValue();
        T data = null;

        if( field.getValidity().isValid && ans instanceof Integer &&
            ( Integer )ans == JOptionPane.OK_OPTION )
        {
            data = field.getValue();
        }

        return data;
    }

    /***************************************************************************
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed to the user.
     * @param title the title of the dialog.
     * @return the answer the user provided. Returns {@link YncAnswer#CANCEL} if
     * the user closes the dialog without answering.
     **************************************************************************/
    public static YncAnswer showQuestionMessage( Component parent,
        String message, String title )
    {
        return showQuestionMessage( parent, message, title, "Yes", "No", null );
    }

    /***************************************************************************
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed to the user.
     * @param title the title of the dialog.
     * @param yesName the text of the "Yes" button.
     * @param noName the text of the "No" button.
     * @return the answer the user provided. Returns {@link YncAnswer#CANCEL} if
     * the user closes the dialog without answering.
     **************************************************************************/
    public static YncAnswer showQuestionMessage( Component parent,
        String message, String title, String yesName, String noName )
    {
        return showQuestionMessage( parent, message, title, yesName, noName,
            null );
    }

    /***************************************************************************
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed to the user.
     * @param title the title of the dialog.
     * @param yesName the name of the "Yes" button.
     * @param noName the text of the "No" button.
     * @param cancelName the text of the "Cancel" button or {@code null} to not
     * show. The user can still cancel by dismissing the dialog.
     * @return the answer the user provided. Returns {@link YncAnswer#CANCEL} if
     * the user closes the dialog without answering.
     **************************************************************************/
    public static YncAnswer showQuestionMessage( Component parent,
        String message, String title, String yesName, String noName,
        String cancelName )
    {
        String [] buttonNames = cancelName == null
            ? new String[] { yesName, noName }
            : new String[] { yesName, noName, cancelName };

        String result = showOptionMessage( parent, message, title, buttonNames,
            noName );
        YncAnswer ans = YncAnswer.CANCEL;

        if( result != null )
        {
            if( result.equals( yesName ) )
            {
                ans = YncAnswer.YES;
            }
            else if( result.equals( noName ) )
            {
                ans = YncAnswer.NO;
            }
            else if( result.equals( cancelName ) )
            {
                ans = YncAnswer.CANCEL;
            }
        }

        return ans;
    }

    /***************************************************************************
     * Displays an OK/Cancel dialog with the provided message, title, and data
     * view.
     * @param <T> the type of data displayed in the provided view.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed above the data view.
     * @param title the title of the dialog.
     * @param view the data view to be displayed.
     * @return the edited data value or null if the dialog was cancelled or
     * closed.
     **************************************************************************/
    public static <T> T showQuestionView( Component parent, String message,
        String title, IDataView<T> view )
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( new JLabel( message ), BorderLayout.NORTH );
        panel.add( view.getView(), BorderLayout.CENTER );

        JOptionPane jop = new JOptionPane( panel, JOptionPane.QUESTION_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, null, null, null );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setVisible( true );

        // ---------------------------------------------------------------------
        // Prompt user.
        // ---------------------------------------------------------------------
        Object ans = jop.getValue();
        T data = null;

        if( ans instanceof Integer && ( Integer )ans == JOptionPane.OK_OPTION )
        {
            data = view.getData();
        }

        return data;
    }

    /***************************************************************************
     * Displays an OK/Cancel dialog with the provided message, title, and data
     * view.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the message to be displayed above the data view.
     * @param title the title of the dialog.
     * @param okText the text of the OK button.
     * @param initialFocusSelector the callback to request focus for the
     * message.
     * @return {@code true} if the ok button was pressed; {@code false}
     * otherwise.
     **************************************************************************/
    public static boolean showOkCancelDialog( Component parent, Object message,
        String title, String okText, final Runnable initialFocusSelector )
    {
        String [] choices = new String[] { okText, "Cancel" };
        JDialog dialog;

        JOptionPane pane = new JOptionPane( message,
            JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
            choices, okText )
        {
            private static final long serialVersionUID = -2554071173382615212L;

            @Override
            public void selectInitialValue()
            {
                initialFocusSelector.run();
            }
        };

        dialog = pane.createDialog( parent, title );
        dialog.setResizable( true );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setSize( 500, dialog.getHeight() );
        dialog.setVisible( true );

        return okText.equals( pane.getValue() );
    }

    /***************************************************************************
     * Prompts for an item using a {@link JOptionPane}.
     * @param <T> the type of the item to be parsed from a text field.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param name the name of the item to prompt the user to enter.
     * @param parser the method used to interpret the entered text as the item
     * needed.
     * @param message the message to be displayed on the dialog.
     * @return the value entered by the user or {@code null} if cancelled or
     * invalid.
     **************************************************************************/
    public static <T> T promptForValue( Component parent, String name,
        IParser<T> parser, String message )
    {
        return promptForValue( parent, name, parser, new JLabel( message ) );
    }

    /***************************************************************************
     * Prompts for an item using an {@link ModalityType#APPLICATION_MODAL}
     * @{link JOptionPane} .
     * @param <T> the type of the item to be parsed from a text field.
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param name the name of the item to prompt the user to enter.
     * @param parser the method used to interpret the entered text as the item
     * needed.
     * @param message the component to be displayed as the message on the
     * dialog.
     * @return the value entered by the user or {@code null} if cancelled or
     * invalid.
     **************************************************************************/
    public static <T> T promptForValue( Component parent, String name,
        IParser<T> parser, JComponent message )
    {
        ParserFormField<T> field = new ParserFormField<>( name, parser );
        T value = null;
        StandardFormView form = new StandardFormView();

        form.addComponent( message );
        form.addField( field );

        boolean result = showConfirmMessage( parent, form.getView(),
            "Enter " + name );

        if( result && field.getValidity().isValid )
        {
            value = field.getValue();
        }

        return value;
    }

    /***************************************************************************
     * Defines the icons that are available with a {@link JOptionPane}.
     **************************************************************************/
    public static enum StockIcon
    {
        /**  */
        QUESTION( JOptionPane.QUESTION_MESSAGE ),
        /**  */
        INFO( JOptionPane.INFORMATION_MESSAGE ),
        /**  */
        WARNING( JOptionPane.WARNING_MESSAGE ),
        /**  */
        ERROR( JOptionPane.ERROR_MESSAGE ),
        /**  */
        PLAIN( JOptionPane.PLAIN_MESSAGE );

        /** The type of message to be used by a {@link JOptionPane}. */
        private final int messageType;

        /**
         * Defines the icon with the provided message type.
         * @param messageType the type of message to be used by a
         * {@link JOptionPane}.
         */
        private StockIcon( int messageType )
        {
            this.messageType = messageType;
        }
    }

    /***************************************************************************
     * Defines the answers Yes, No, and Cancel.
     **************************************************************************/
    public static enum YncAnswer
    {
        /** If the user chooses yes. */
        YES,
        /** If the user chooses no. */
        NO,
        /** If the user chooses cancel or dismisses the dialog. */
        CANCEL;
    }
}
