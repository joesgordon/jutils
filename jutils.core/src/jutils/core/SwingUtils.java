package jutils.core;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import jutils.core.io.xs.XsUtils;
import jutils.core.ui.StatusBarPanel;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.ItemActionEvent;
import jutils.core.ui.event.ItemActionListener;
import jutils.core.ui.model.IDataView;

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
        if( icon == null )
        {
            return;
        }

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
                String str = XsUtils.writeObjectXStream( data );
                Utils.setClipboardText( str );
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }
            catch( ValidationException ex )
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
                T data = XsUtils.readObjectXStream( str );

                itemListener.actionPerformed(
                    new ItemActionEvent<T>( itemListener, data ) );
            }
            catch( ValidationException ex )
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
     * @param deviceId the ID corresponding to a device's
     * {@link GraphicsDevice#getIDstring()}.
     * @return {@code true} if the screen was found.
     **************************************************************************/
    public static boolean setFullScreen( JFrame frame, String deviceId )
    {
        boolean found = false;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        GraphicsDevice [] devs = ge.getScreenDevices();

        for( GraphicsDevice device : devs )
        {
            if( deviceId.equals( device.getIDstring() ) )
            {
                found = true;
                setFullScreen( frame, device );
            }
        }

        return found;
    }

    /***************************************************************************
     * Makes the provided frame full screen on the provided device.
     * @param frame the frame to make full screen.
     * @param device the screen on which the frame shall be full screen.
     **************************************************************************/
    public static void setFullScreen( JFrame frame, GraphicsDevice device )
    {
        frame.dispose();
        frame.setUndecorated( true );
        frame.setAlwaysOnTop( true );
        frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        if( device != null && device.isFullScreenSupported() )
        {
            device.setFullScreenWindow( frame );
        }
        else
        {
            frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        }

        frame.setVisible( true );
    }

    /***************************************************************************
     * Makes the provided frame undecorated and full-screen using
     * {@link #setFullScreen(JFrame)} or reversing the actions there-in.
     * @param frame the frame to be toggled to/from full-screen.
     **************************************************************************/
    public static void toggleFullScreen( JFrame frame )
    {
        boolean isFullscreen = frame.isUndecorated();

        if( isFullscreen )
        {
            frame.dispose();
            frame.setUndecorated( false );
            frame.setVisible( true );
        }
        else
        {
            SwingUtils.setFullScreen( frame );
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
     * @throws IllegalArgumentException when the keystroke cannot be found via
     * {@link KeyStroke#getKeyStroke(String)}.
     **************************************************************************/
    public static Action addKeyListener( JComponent view, String keystoke,
        ActionListener callback, String actionName, boolean inWindow )
        throws IllegalArgumentException
    {
        Action action = new ActionAdapter( callback, actionName, null );

        addKeyListener( view, keystoke, action, inWindow );

        return action;
    }

    /***************************************************************************
     * Binds a listener to a key on a component.
     * @param view the component on which to bind the key.
     * @param keystoke the key to be bound.
     * @param callback the listener to be called when the key is pressed.
     * @param actionName the name of the action used to bind the listener.
     * @return the action created.
     * @param condition the condition under which the key listener is called
     * {@link JComponent#getInputMap(int)}.
     * @return the action created.
     * @throws IllegalArgumentException when the keystroke cannot be found via
     * {@link KeyStroke#getKeyStroke(String)}.
     */
    public static Action addKeyListener( JComponent view, String keystoke,
        ActionListener callback, String actionName, int condition )
        throws IllegalArgumentException
    {
        Action action = new ActionAdapter( callback, actionName, null );

        addKeyListener( view, keystoke, action, condition );

        return action;
    }

    /***************************************************************************
     * Binds a listener to a key on a component.
     * @param view the component on which to bind the key.
     * @param keystoke the key to be bound.
     * @param inWindow uses {@link JComponent#WHEN_IN_FOCUSED_WINDOW} for
     * binding if {@code true};
     * @param action the action to be bound to the key.
     * @throws IllegalArgumentException when the keystroke cannot be found via
     * {@link KeyStroke#getKeyStroke(String)}.
     **************************************************************************/
    public static void addKeyListener( JComponent view, String keystoke,
        Action action, boolean inWindow ) throws IllegalArgumentException
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
     * {@link JComponent#getInputMap(int)}.
     * @param action the action to be bound to the key.
     * @throws IllegalArgumentException when the keystroke cannot be found via
     * {@link KeyStroke#getKeyStroke(String)}.
     **************************************************************************/
    public static void addKeyListener( JComponent view, String keystoke,
        Action action, int condition ) throws IllegalArgumentException
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
     * @param table
     * @param columnIndex
     * @param width
     **************************************************************************/
    public static void fixWidth( final JTable table, final int columnIndex,
        final int width )
    {
        TableColumn column = table.getColumnModel().getColumn( columnIndex );
        column.setMinWidth( width );
        column.setMaxWidth( width );
        column.setPreferredWidth( width );
    }

    /***************************************************************************
     * Scrolls to the row and column provided.
     * @param table the table to be scrolled.
     * @param row the row to be scrolled to.
     * @param col the column to be scrolled to.
     **************************************************************************/
    public static void scrollToVisible( JTable table, int row, int col )
    {
        Rectangle rect = table.getCellRect( row, col, true );

        rect = new Rectangle( rect );

        // LogUtils.printDebug( "Scrolling to: " + row + ", " + col + ":" +
        // rect.toString() );

        table.scrollRectToVisible( rect );
    }

    /***************************************************************************
     * @param table
     * @param rowMax
     **************************************************************************/
    public static void resizeTable( JTable table, int rowMax )
    {
        int horzSpace = 6;
        String colName;
        TableModel model = table.getModel();
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        int widths[] = new int[model.getColumnCount()];
        Component cellRenderer;
        TableCellRenderer tableCellRenderer;
        int defaultWidth;

        int rrow = table.getRowCount();

        rrow = rrow == 0 ? -1 : 0;

        rrow = Math.min( rrow, rowMax );

        // ---------------------------------------------------------------------
        // Compute all widths.
        // ---------------------------------------------------------------------
        for( int col = 0; col < colCount; col++ )
        {
            colName = model.getColumnName( col );
            defaultWidth = 20;

            // -----------------------------------------------------------------
            // Compute header width.
            // -----------------------------------------------------------------
            tableCellRenderer = table.getColumnModel().getColumn(
                col ).getHeaderRenderer();
            if( tableCellRenderer == null )
            {
                tableCellRenderer = table.getTableHeader().getDefaultRenderer();
            }
            cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                table, colName, false, false, -1, col );

            widths[col] = ( int )cellRenderer.getPreferredSize().getWidth() +
                horzSpace;
            widths[col] = Math.max( widths[col], defaultWidth );

            tableCellRenderer = table.getCellRenderer( rrow, col );

            // -----------------------------------------------------------------
            // check if cell values fit in their cells
            // -----------------------------------------------------------------
            for( int row = 0; row < rowCount; row++ )
            {
                Object obj = model.getValueAt( row, col );
                int width = 0;
                if( obj != null )
                {
                    tableCellRenderer = table.getCellRenderer( row, col );
                    cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                        table, obj, false, false, row, col );
                    width = ( int )cellRenderer.getPreferredSize().getWidth() +
                        horzSpace;
                }
                widths[col] = Math.max( widths[col], width );
            }
        }

        TableColumnModel colModel = table.getColumnModel();

        // ---------------------------------------------------------------------
        // Set the column widths.
        // ---------------------------------------------------------------------
        for( int i = 0; i < colCount; i++ )
        {
            colModel.getColumn( i ).setPreferredWidth( widths[i] );
            // colModel.getColumn( i ).setMinWidth( widths[i] );
        }
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
