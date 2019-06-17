package org.jutils;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jutils.io.IParser;
import org.jutils.io.XStreamUtils;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.event.*;
import org.jutils.ui.fields.ParserFormField;
import org.jutils.ui.model.IDataView;

import com.thoughtworks.xstream.XStreamException;

/*******************************************************************************
 * Utility class for AWT/Swing static functions.
 ******************************************************************************/
public final class SwingUtils
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private SwingUtils()
    {
    }

    /***************************************************************************
     * Installs a listener to close the provided frame with its default close
     * operation when Escape is pressed.
     * @param frame the frame to which the listener shall be installed.
     **************************************************************************/
    public static void installEscapeCloseOperation( JFrame frame )
    {
        installEscapeCloseOperation( frame, frame.getRootPane() );
    }

    /***************************************************************************
     * Installs a listener to close the provided dialog with its default close
     * operation when Escape is pressed.
     * @param dialog the dialog to which the listener shall be installed.
     **************************************************************************/
    public static void installEscapeCloseOperation( JDialog dialog )
    {
        installEscapeCloseOperation( dialog, dialog.getRootPane() );
    }

    /***************************************************************************
     * Installs a listener to close the provided window with its default close
     * operation when Escape is pressed.
     * @param win the window to be closed.
     * @param rootPane the window's root pane.
     **************************************************************************/
    private static void installEscapeCloseOperation( Window win,
        JRootPane rootPane )
    {
        ActionListener closeListener = ( e ) -> closeWindow( win );
        String mapKey = "com.spodding.tackline.dispatch:WINDOW_CLOSING";
        Action closeAction = new ActionAdapter( closeListener, mapKey, null );

        addKeyListener( rootPane, "ESCAPE", closeAction, true );
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
        JOptionPane jop = new JOptionPane( message, JOptionPane.ERROR_MESSAGE,
            JOptionPane.DEFAULT_OPTION, null );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );

        dialog.setVisible( true );
    }

    /***************************************************************************
     * Displays an OK/Cancel option dialog with the provided message, title, and
     * choices.
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
     * @param choices the choices to be displayed in buttons.
     * @param defaultChoice the choice to be selected by default.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static String showConfirmMessage( Component parent, Object message,
        String title, String [] choices, String defaultChoice )
    {
        return showConfirmMessage( parent, message, title, choices,
            defaultChoice, false );
    }

    /***************************************************************************
     * @param parent determines the {@link Frame} in which the dialog is
     * displayed; if the {@code parent} has no {@link Frame}, a default
     * {@link Frame} is used.
     * @param message the {@link String} or {@link Component} that represents
     * the message to be displayed.
     * @param title the title of the dialog.
     * @param choices the choices to be displayed in buttons.
     * @param defaultChoice the choice to be selected by default.
     * @param resizable indicates that the dialog is resizable with
     * {@code true}.
     * @return the user's choice or null if the user closes the dialog.
     **************************************************************************/
    public static String showConfirmMessage( Component parent, Object message,
        String title, String [] choices, String defaultChoice,
        boolean resizable )
    {
        JOptionPane jop = new JOptionPane( message,
            JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
            choices, defaultChoice );

        JDialog dialog = jop.createDialog( parent, title );
        dialog.setModalityType( ModalityType.DOCUMENT_MODAL );
        dialog.setResizable( resizable );
        dialog.setVisible( true );

        Object valueObj = jop.getValue();
        String value = null;

        if( valueObj instanceof String )
        {
            value = valueObj.toString();
        }

        return value;
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
     * @param view the data view to be displayed
     * @return the edited data value or null if the dialog was cancelled or
     * closed.
     **************************************************************************/
    public static <T> T showQuestion( Component parent, String message,
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

        // TODO because JOptionPane sometimes returns -1.

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
     * Prompts for an item using a {@link JOptionPane}.
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

        int result = JOptionPane.showConfirmDialog( parent, form.getView(),
            "Enter " + name, JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE );

        if( result == JOptionPane.OK_OPTION && field.getValidity().isValid )
        {
            value = field.getValue();
        }

        return value;
    }

    /***************************************************************************
     * Creates a panel with the provided toolbar and content with a status bar.
     * @param toolbar the toolbar to be added.
     * @param container the container to be added.
     * @return the panel built.
     **************************************************************************/
    public static JPanel createStandardConentPane( JToolBar toolbar,
        Container container )
    {
        StatusBarPanel statusbar = new StatusBarPanel();
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;
        int row = 0;

        if( toolbar != null )
        {
            constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            panel.add( toolbar, constraints );
        }

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( container, constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( new JSeparator(), constraints );

        constraints = new GridBagConstraints( 0, row++, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( statusbar.getView(), constraints );

        return panel;
    }

    /***************************************************************************
     * Set the standard defaults for a toolbar: non-floatable, rollover enabled,
     * and empty border.
     * @param toolbar the toolbar on which to set defaults.
     **************************************************************************/
    public static void setToolbarDefaults( JToolBar toolbar )
    {
        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );
    }

    /***************************************************************************
     * Adds the provided action to the toolbar and returns the created button.
     * @param toolbar the toolbar to which the action is added.
     * @param action the action to be performed.
     * @return the created button.
     **************************************************************************/
    public static JButton addActionToToolbar( JToolBar toolbar, Action action )
    {
        JButton button = new JButton();

        addActionToToolbar( toolbar, action, button );

        return button;
    }

    /***************************************************************************
     * Adds the provided button to the toolbar with the provided action set to
     * the button.
     * @param toolbar the toolbar to which the button is added.
     * @param action the action to be performed.
     * @param button the button to be added.
     **************************************************************************/
    public static void addActionToToolbar( JToolBar toolbar, Action action,
        JButton button )
    {
        button.setAction( action );
        button.setFocusable( false );
        button.setText( null );
        button.setToolTipText( action.getValue( Action.NAME ).toString() );
        button.setMnemonic( -1 );
        toolbar.add( button );
    }

    /***************************************************************************
     * Sets the tool tip of the provided action.
     * @param action the action for which the tool tip will be set.
     * @param tooltip the tool tip to be set.
     **************************************************************************/
    public static void setActionToolTip( Action action, String tooltip )
    {
        action.putValue( Action.SHORT_DESCRIPTION, tooltip );
    }

    /***************************************************************************
     * Creates and displays a tray icon with the provided image, tool tip, and
     * popup menu which displays/hides the provided frame when double-clicked.
     * @param img The image (icon) to be displayed.
     * @param tooltip The tool tip to be displayed.
     * @param frame The frame to be displayed/hidden.
     * @param popup The popup menu to be displayed on right-click.
     * @return The tray icon created or {@code null} if the system tray is not
     * supported.
     * @throws UnsupportedOperationException if {@link SystemTray#add(TrayIcon)}
     * throws an {@link AWTException}.
     **************************************************************************/
    public static TrayIcon createTrayIcon( Image img, String tooltip,
        JFrame frame, PopupMenu popup ) throws UnsupportedOperationException
    {
        TrayIcon icon = null;

        if( SystemTray.isSupported() )
        {
            ActionListener trayListener = new MiniMaximizeListener( frame );

            SystemTray tray = SystemTray.getSystemTray();

            icon = new TrayIcon( img, tooltip );
            icon.addActionListener( trayListener );
            icon.setImageAutoSize( true );
            try
            {
                tray.add( icon );
                frame.addWindowListener( new HideOnMinimizeListener( frame ) );
            }
            catch( AWTException ex )
            {
                throw new UnsupportedOperationException(
                    "Cannot load icon in tray", ex );
            }

            icon.setPopupMenu( popup );
        }

        return icon;
    }

    /***************************************************************************
     * Adds the menu to the tray icon
     * @param icon the tray icon to be displayed.
     * @param popup the menu to be added.
     **************************************************************************/
    public static void addTrayMenu( TrayIcon icon, JPopupMenu popup )
    {
        icon.addMouseListener( new TrayMouseListener( popup ) );
    }

    /***************************************************************************
     * Return the default fixed font of the specified size.
     * @param size the size of the font.
     * @return the fixed font.
     **************************************************************************/
    public static Font getFixedFont( int size )
    {
        return new Font( Font.MONOSPACED, Font.PLAIN, size );
    }

    /***************************************************************************
     * Returns the inverse of the provided color.
     * @param color the color to be inverted.
     * @return the inverted color.
     **************************************************************************/
    public static Color inverseColor( Color color )
    {
        int a = color.getAlpha();
        int r = a - color.getRed();
        int g = a - color.getGreen();
        int b = a - color.getBlue();

        return new Color( r, g, b, a );
    }

    /***************************************************************************
     * Ensures the provided frame does not pop under any other windows.
     * @param frame the frame to be displayed.
     **************************************************************************/
    private static void handleExtendedState( JFrame frame )
    {
        if( frame.isVisible() )
        {
            // -------------------------------------------------------------
            // This stinks because it cannot retain the extended state.
            // I've tried all sorts of ways and this is the only thing
            // that doesn't pop the window up behind other things and still
            // give it focus. So, don't remove it.
            // -------------------------------------------------------------
            frame.setExtendedState( JFrame.NORMAL );
            frame.requestFocus();
            frame.toFront();
        }
    }

    /***************************************************************************
     * Displays the frame, restores the extended state, and ensures it does not
     * pop under any other windows.
     * @param frame the frame to be displayed.
     **************************************************************************/
    public static void toFrontRestoreState( JFrame frame )
    {
        if( frame.isVisible() )
        {
            int extState = frame.getExtendedState();

            if( ( extState & JFrame.ICONIFIED ) == JFrame.ICONIFIED )
            {
                if( ( extState &
                    JFrame.MAXIMIZED_BOTH ) == JFrame.MAXIMIZED_BOTH )
                {
                    frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
                }
                else if( ( extState &
                    JFrame.MAXIMIZED_HORIZ ) == JFrame.MAXIMIZED_HORIZ )
                {
                    frame.setExtendedState( JFrame.MAXIMIZED_HORIZ );
                }
                else if( ( extState &
                    JFrame.MAXIMIZED_VERT ) == JFrame.MAXIMIZED_VERT )
                {
                    frame.setExtendedState( JFrame.MAXIMIZED_VERT );
                }
                else
                {
                    frame.setExtendedState( JFrame.NORMAL );
                }
            }

            frame.requestFocus();
            frame.toFront();
        }
        else
        {
            frame.setVisible( true );
        }
    }

    /***************************************************************************
     * Creates an action to copy data from the provided view to the system
     * clipboard.
     * @param <T> the type of data to be copied.
     * @param view the view containing the data to be copied.
     * @return the created action.
     **************************************************************************/
    public static <T> Action createCopyAction( IDataView<T> view )
    {
        ActionListener listener = ( e ) -> {
            T data = view.getData();
            try
            {
                String str = XStreamUtils.writeObjectXStream( data );
                Utils.setClipboardText( str );
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }
        };
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_COPY_16 );
        Action action = new ActionAdapter( listener, "Copy", icon );

        return action;
    }

    /***************************************************************************
     * Creates an action to paste data from the system clipboard to the provided
     * listener.
     * @param <T> the type of data to be pasted.
     * @param itemListener the listener to be called when the action is invoked.
     * @return the created action.
     **************************************************************************/
    public static <T> Action createPasteAction(
        ItemActionListener<T> itemListener )
    {
        ActionListener listener = ( e ) -> {
            try
            {
                String str = Utils.getClipboardText();
                T data = XStreamUtils.readObjectXStream( str );

                itemListener.actionPerformed(
                    new ItemActionEvent<T>( itemListener, data ) );
            }
            catch( XStreamException ex )
            {
            }
        };
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_PASTE_16 );
        Action action = new ActionAdapter( listener, "Paste", icon );

        return action;
    }

    /***************************************************************************
     * Creates a {@link ComboBoxModel} with the provided array of items.
     * @param <T> the type of data in the model.
     * @param items the items to be contained within the model.
     * @return the model containing the items.
     **************************************************************************/
    public static <T> ComboBoxModel<T> createModel( T [] items )
    {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();

        for( T option : items )
        {
            model.addElement( option );
        }

        return model;
    }

    /***************************************************************************
     * Programmatically closes the provided window. See the StackOverflow
     * question <a href="http://stackoverflow.com/questions/1234912">How to
     * programmatically close a JFrame</a> for more information.
     * @param win the window to be closed.
     **************************************************************************/
    public static void closeWindow( Window win )
    {
        win.dispatchEvent( new WindowEvent( win, WindowEvent.WINDOW_CLOSING ) );
    }

    /***************************************************************************
     * Search the component's parent tree looking for an object of the provided
     * type.
     * @param <T> the type of parent desired.
     * @param comp the child component.
     * @param type the type of parent to be found.
     * @return the component of the type provided or {@code null} if not found.
     **************************************************************************/
    public static <T extends Component> T getParentOfType( Component comp,
        Class<T> type )
    {
        Component parentComp = comp;

        while( parentComp != null )
        {
            if( type.isAssignableFrom( parentComp.getClass() ) )
            {
                @SuppressWarnings( "unchecked")
                T parent = ( T )parentComp;
                return parent;
            }
            parentComp = parentComp.getParent();
        }

        return null;
    }

    /***************************************************************************
     * Returns the {@link Window} containing the provided component.
     * @param comp the child component.
     * @return the window owning the provided component or {@code null} if the
     * component is orphaned.
     **************************************************************************/
    public static Window getComponentsWindow( Component comp )
    {
        Object win = getParentOfType( comp, Window.class );
        return win != null ? ( Window )win : null;
    }

    /***************************************************************************
     * Returns the {@link Frame} containing the provided component.
     * @param comp the child component.
     * @return the frame owning the provided component or {@code null} if the
     * component is orphaned or the owning window is not a frame.
     **************************************************************************/
    public static Frame getComponentsFrame( Component comp )
    {
        Object win = getParentOfType( comp, Frame.class );
        return win != null ? ( Frame )win : null;
    }

    /***************************************************************************
     * Returns the {@link JFrame} containing the provided component or
     * {@code null} if none exists.
     * @param comp the child component.
     * @return the frame containing the provided component.
     **************************************************************************/
    public static JFrame getComponentsJFrame( Component comp )
    {
        Object win = getParentOfType( comp, JFrame.class );
        return win != null ? ( JFrame )win : null;
    }

    /***************************************************************************
     * Finds the maximum width and length of the provided components and sets
     * the preferred size of each to the maximum.
     * @param comps the components to be evaluated.
     * @return the maximum size.
     **************************************************************************/
    public static Dimension setMaxComponentSize( Component... comps )
    {
        Dimension dim = getMaxComponentSize( comps );

        for( Component comp : comps )
        {
            comp.setPreferredSize( dim );
        }

        return dim;
    }

    /***************************************************************************
     * Finds the maximum width and length of the provided components.
     * @param comps the components to be evaluated.
     * @return the maximum size.
     **************************************************************************/
    public static Dimension getMaxComponentSize( Component... comps )
    {
        Dimension max = new Dimension( 0, 0 );
        Dimension dim;

        for( Component comp : comps )
        {
            dim = comp.getPreferredSize();
            max.width = Math.max( max.width, dim.width );
            max.height = Math.max( max.height, dim.height );
        }

        return max;
    }

    /***************************************************************************
     * Make the provided frame full screen on it's screen or the default
     * {@link GraphicsDevice} if the frame's screen is not accessible (e.g. it
     * hasn't been shown).
     * @param frame the frame to make full screen.
     **************************************************************************/
    public static void setFullScreen( JFrame frame )
    {
        GraphicsConfiguration gc;
        GraphicsDevice gd = null;

        gc = frame.getGraphicsConfiguration();
        if( gc != null )
        {
            gd = gc.getDevice();
        }

        if( gd == null )
        {
            GraphicsEnvironment ge;
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            gd = ge.getDefaultScreenDevice();
        }

        setFullScreen( frame, gd );
    }

    /***************************************************************************
     * Makes the provided frame full screen on the provided device.
     * @param frame the frame to make full screen.
     * @param device the screen on which the frame shall be full screen.
     **************************************************************************/
    public static void setFullScreen( JFrame frame, GraphicsDevice device )
    {
        frame.setUndecorated( true );
        frame.setAlwaysOnTop( true );
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        if( device.isFullScreenSupported() && !"".isEmpty() )
        {
            device.setFullScreenWindow( frame );
        }
        else
        {
            frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        }
    }

    /***************************************************************************
     * Builds a {@link TreePath} for the provided node.
     * @param node the node for which a path is to be built.
     * @return the path to the provided node.
     **************************************************************************/
    public static TreePath getPath( TreeNode node )
    {
        List<Object> nodes = new ArrayList<Object>();

        if( node != null )
        {
            nodes.add( node );
            node = node.getParent();
            while( node != null )
            {
                nodes.add( 0, node );
                node = node.getParent();
            }
        }

        return nodes.isEmpty() ? null : new TreePath( nodes.toArray() );

    }

    /***************************************************************************
     * Binds a listener to a key on a component.
     * @param view the component on which to bind the key.
     * @param keystoke the key to be bound.
     * @param inWindow uses {@link JComponent#WHEN_IN_FOCUSED_WINDOW} for
     * binding if {@code true};
     * {@link JComponent#WHEN_ANCESTOR_OF_FOCUSED_COMPONENT} otherwise.
     * @param callback the listener to be called when the key is pressed.
     * @param actionName the name of the action used to bind the listener.
     * @return the action created.
     **************************************************************************/
    public static Action addKeyListener( JComponent view, String keystoke,
        ActionListener callback, String actionName, boolean inWindow )
    {
        Action action = new ActionAdapter( callback, actionName, null );

        addKeyListener( view, keystoke, action, inWindow );

        return action;
    }

    /***************************************************************************
     * Binds a listener to a key on a component.
     * @param view the component on which to bind the key.
     * @param keystoke the key to be bound.
     * @param inWindow uses {@link JComponent#WHEN_IN_FOCUSED_WINDOW} for
     * binding if {@code true};
     * @param action the action to be bound to the key.
     **************************************************************************/
    public static void addKeyListener( JComponent view, String keystoke,
        Action action, boolean inWindow )
    {
        int condition = inWindow ? JComponent.WHEN_IN_FOCUSED_WINDOW
            : JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        InputMap inMap = view.getInputMap( condition );
        ActionMap acMap = view.getActionMap();
        KeyStroke stroke = KeyStroke.getKeyStroke( keystoke );
        String mapKey = action.getValue( Action.NAME ).toString();

        if( stroke == null )
        {
            throw new IllegalArgumentException(
                "Invalid keystroke: " + keystoke );
        }

        action.putValue( Action.ACCELERATOR_KEY, stroke );
        inMap.put( stroke, mapKey );
        acMap.put( mapKey, action );
    }

    /***************************************************************************
     * Binds a listener to a key on a component.
     * @param view the component on which to bind the key.
     * @param keystoke the key to be bound.
     * @param condition the condition under which the key listener is called
     * {@link JComponent#getInputMap(int)};
     * @param action the action to be bound to the key.
     **************************************************************************/
    public static void addKeyListener( JComponent view, String keystoke,
        Action action, int condition )
    {
        InputMap inMap = view.getInputMap( condition );
        ActionMap acMap = view.getActionMap();
        KeyStroke stroke = KeyStroke.getKeyStroke( keystoke );
        String mapKey = action.getValue( Action.NAME ).toString();

        if( stroke == null )
        {
            throw new IllegalArgumentException(
                "Invalid keystroke: " + keystoke );
        }

        action.putValue( Action.ACCELERATOR_KEY, stroke );
        inMap.put( stroke, mapKey );
        acMap.put( mapKey, action );
    }

    /***************************************************************************
     * An action listener to display a frame (or bring it to the front) when
     * invoked.
     **************************************************************************/
    public static class ShowFrameListener implements ActionListener
    {
        /** The frame to be shown. */
        private final JFrame f;

        /**
         * Creates a new listener.
         * @param frame the frame to be shown.
         */
        public ShowFrameListener( JFrame frame )
        {
            f = frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( f.isVisible() )
            {
                f.toFront();
            }
            else
            {
                f.setVisible( true );
                handleExtendedState( f );
            }
        }
    }

    /***************************************************************************
     * An action listener to toggle a frames visibility when run.
     **************************************************************************/
    protected static class MiniMaximizeListener implements ActionListener
    {
        /** The frame to restore/hide. */
        private final JFrame f;

        /**
         * Creates a new listener.
         * @param frame the frame to restore/hide.
         */
        public MiniMaximizeListener( JFrame frame )
        {
            f = frame;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed( ActionEvent e )
        {
            f.setVisible( !f.isVisible() );
            SwingUtils.handleExtendedState( f );
        }
    }

    /***************************************************************************
     * An action listener that sets a frame's visibility to false when run.
     **************************************************************************/
    protected static class HideOnMinimizeListener extends WindowAdapter
    {
        /** The frame to be hidden. */
        private final JFrame frame;

        /**
         * Creates a new listener.
         * @param f the frame to be hidden.
         */
        public HideOnMinimizeListener( JFrame f )
        {
            frame = f;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void windowIconified( WindowEvent e )
        {
            frame.setVisible( false );
        }
    }

    /***************************************************************************
     * A mouse listener display the popup menu on right-click.
     **************************************************************************/
    private static class TrayMouseListener extends MouseAdapter
    {
        /** The popup to be shown on right-click. */
        private final JPopupMenu popup;
        /** A dialog to be the parent of the popup. Needed for reasons. */
        private final JDialog dialog;

        /**
         * Creates the new listener.
         * @param popup the popup to be shown on right-click.
         */
        public TrayMouseListener( JPopupMenu popup )
        {
            this.popup = popup;
            this.dialog = new JDialog();

            popup.validate();

            dialog.setUndecorated( true );
            dialog.setSize( 10, 10 );
            dialog.validate();

            dialog.addWindowFocusListener( new WindowFocusListener()
            {
                @Override
                public void windowLostFocus( WindowEvent we )
                {
                    dialog.setVisible( false );
                }

                @Override
                public void windowGainedFocus( WindowEvent we )
                {
                }
            } );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased( MouseEvent e )
        {
            if( SwingUtilities.isRightMouseButton( e ) &&
                e.getClickCount() == 1 && !e.isConsumed() )
            {
                Point p = calcLocation( e.getPoint(), e.getLocationOnScreen() );

                popup.setLocation( p.x, p.y );
                dialog.setLocation( p.x, p.y );
                dialog.setMinimumSize( new Dimension( 0, 0 ) );
                dialog.setSize( 0, 0 );
                popup.setInvoker( dialog );
                dialog.setVisible( true );
                popup.setVisible( true );
            }
        }

        /**
         * Calculates the point at which the popup menu should be shown based on
         * the location of right-click.
         * @param srcLoc the right-click location relative to the source of the
         * right-click.
         * @param scnLoc the right-click location relative to the screen.
         * @return the calculated point.
         */
        private Point calcLocation( Point srcLoc, Point scnLoc )
        {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice [] gs = ge.getScreenDevices();

            for( int i = 0; i < gs.length; i++ )
            {
                DisplayMode dm = gs[i].getDisplayMode();
                GraphicsConfiguration gc = gs[i].getDefaultConfiguration();
                Rectangle r = new Rectangle( gc.getBounds() );

                if( r.contains( scnLoc ) )
                {
                    if( ( dm.getHeight() + popup.getHeight() + 8 ) > srcLoc.y )
                    {
                        srcLoc.y -= ( popup.getHeight() + 8 );
                    }
                }
            }

            return srcLoc;
        }
    }
}
