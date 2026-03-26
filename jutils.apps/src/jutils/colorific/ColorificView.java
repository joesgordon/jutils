package jutils.colorific;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Window;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import jutils.core.SwingUtils;
import jutils.core.ui.event.ActionAdapter;
import jutils.core.ui.event.WindowCloseListener;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * Defines the primary view for the Colorific application.
 ******************************************************************************/
public class ColorificView implements IView<JComponent>
{
    /** The number of of color grab displays. */
    private final int SWATCH_COUNT = 16;
    /** The delay between grabs in milliseconds. */
    private final int GRAB_DELAY = 50;
    /**
     * The key(s) pressed to grab the color at the pixel under the mouse cursor.
     */
    private final String GRAB_KEY = "control G";

    /** The primary component view. */
    private final JPanel view;
    /** A view that displays a zoomed in area of the current mouse location. */
    private final ZoomView zoomArea;
    /** Displays the components of the grabbed color in several formats. */
    private final JColorChooser colorChooser;
    /** Displays the RGB hex of the grabbed/hovered color. */
    private final JLabel colorLabel;
    /** The color grab displays. */
    private final SwatchView [] swatches;
    /** The action that grabs the screen around the mouse cursor. */
    private final Action grabAction;
    /** Called when the component's window is closed to stop the timer. */
    private final WindowCloseListener windowListener;
    /** The button used to enable/disable picking. */
    private final JToggleButton pickerButton;

    /** The index of the selected swatch. */
    private int selectedIndex;
    /** The index of the next swatch. */
    private int currentIndex;

    /**  */
    private Timer timer;

    /***************************************************************************
     * Creates a new Colorific view.
     **************************************************************************/
    public ColorificView()
    {
        this.zoomArea = new ZoomView();
        this.colorChooser = new JColorChooser();
        this.colorLabel = new JLabel();
        this.swatches = new SwatchView[SWATCH_COUNT];
        this.grabAction = new ActionAdapter( ( e ) -> handleGrab(),
            "grabKeyStroke", null );
        this.windowListener = new WindowCloseListener(
            () -> handleWindowClosed() );
        this.pickerButton = new JToggleButton();

        this.selectedIndex = 0;
        this.currentIndex = 0;
        this.timer = null;

        this.view = createView();

        KeyStroke key;

        key = KeyStroke.getKeyStroke( GRAB_KEY );

        grabAction.putValue( Action.ACCELERATOR_KEY, key );
        view.getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( key,
            "grabKeyStroke" );
        view.getActionMap().put( "grabKeyStroke", grabAction );

        grabAction.setEnabled( false );
    }

    /***************************************************************************
     * Creates a new component that is this view.
     * @return the newly created component.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( createPickerPanel(), BorderLayout.WEST );
        panel.add( colorChooser, BorderLayout.CENTER );

        colorChooser.getChooserPanels();

        return panel;
    }

    /***************************************************************************
     * Creates the component that displays the picker, zoom area, and swatches.
     * @return the newly created component.
     **************************************************************************/
    private Component createPickerPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        Action action;
        Icon icon;

        icon = ColorificIcons.loader.getIcon( ColorificIcons.COLOR_PICKER_024 );
        action = new ActionAdapter( ( e ) -> handlePickerButton(), "Pick Color",
            icon );
        pickerButton.setAction( action );
        pickerButton.setText( null );
        pickerButton.setToolTipText( "Pick Color from screen" );
        pickerButton.setFocusable( false );
        pickerButton.setMargin( new Insets( 2, 2, 2, 2 ) );

        colorLabel.setText( "#------" );
        colorLabel.setFont( new Font( "Monospaced", Font.PLAIN, 12 ) );

        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( pickerButton, constraints );

        constraints = new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 0, 8 ), 0, 0 );
        panel.add( zoomArea.getView(), constraints );

        constraints = new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 8, 8, 8 ), 0, 0 );
        panel.add( colorLabel, constraints );

        constraints = new GridBagConstraints( 0, 2, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( new JLabel( "Control+G to save current color" ),
            constraints );

        constraints = new GridBagConstraints( 0, 3, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( createSwatchPanel(), constraints );

        constraints = new GridBagConstraints( 0, 4, 2, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( Box.createHorizontalGlue(), constraints );

        return panel;
    }

    /***************************************************************************
     * Creates the component containing swatches.
     * @return the newly created component.
     **************************************************************************/
    private Component createSwatchPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        for( int i = 0; i < swatches.length; i++ )
        {
            SwatchView s = new SwatchView();
            int sidx = i;

            swatches[i] = s;

            int x = i % 4;
            int y = i / 4;
            JButton comp = swatches[i].getView();
            int t = 4;
            int l = 4;
            int b = 4;
            int r = 4;

            comp.setToolTipText( "Swatch " + ( i + 1 ) );
            comp.addActionListener( ( e ) -> handleSwatchSelected( s, sidx ) );

            constraints = new GridBagConstraints( x, y, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( t, l, b, r ), 0, 0 );
            panel.add( comp, constraints );
        }

        swatches[0].setSelected( true );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * Callback invoked when the {@code #pickerButton} is pressed.
     **************************************************************************/
    private void handlePickerButton()
    {
        if( pickerButton.isSelected() )
        {
            startPicking();
        }
        else
        {
            stopPicking();
        }
    }

    /***************************************************************************
     * Callback invoked when the user presses the action key to grab the color
     * at the pixel under the mouse cursor.
     **************************************************************************/
    private void handleGrab()
    {
        int nextSelected = currentIndex;

        if( selectedIndex == currentIndex )
        {
            nextSelected++;

            if( nextSelected < 0 )
            {
                nextSelected = swatches.length - 1;
            }

            currentIndex = nextSelected;
        }

        // LogUtils.printDebug( "Selected = %d, Current = %d, Next = %d",
        // selectedIndex, currentIndex, nextSelected );

        swatches[selectedIndex].setSelected( false );
        selectedIndex = nextSelected;
        swatches[selectedIndex].setSelected( true );
    }

    /***************************************************************************
     * Callback invoked when each screen update is invoked at
     * {@link #GRAB_DELAY} milliseconds.
     * @param bot the robot invoked at the moment the {@link #pickerButton} was
     * pressed.
     **************************************************************************/
    private void handleScreenUpdate( Robot bot )
    {
        PointerInfo pi = MouseInfo.getPointerInfo();

        if( pi == null )
        {
            return;
        }

        Point p = pi.getLocation();

        zoomArea.copyArea( p, bot, swatches[selectedIndex], colorLabel );
    }

    /***************************************************************************
     * Callback invoked when a swatch is selected.
     * @param swatch the swatch selected.
     * @param index the index of the swatch selected.
     **************************************************************************/
    private void handleSwatchSelected( SwatchView swatch, int index )
    {
        stopPicking();

        colorChooser.setColor( swatch.getColor() );

        swatches[selectedIndex].setSelected( false );
        swatch.setSelected( true );

        selectedIndex = index;
    }

    /***************************************************************************
     * Callback invoked when the component's window is closed.
     **************************************************************************/
    private void handleWindowClosed()
    {
        stopPicking();

        Window window = SwingUtils.getComponentsWindow( view );

        window.removeWindowListener( windowListener );
    }

    /***************************************************************************
     * Starts the update of the selected swatch when the mouse is moved.
     **************************************************************************/
    private void startPicking()
    {
        try
        {
            Robot bot = new Robot();

            timer = new Timer( GRAB_DELAY, ( e ) -> handleScreenUpdate( bot ) );

            Window window = SwingUtils.getComponentsWindow( view );

            window.removeWindowListener( windowListener );
            window.addWindowListener( windowListener );

            timer.start();

            grabAction.setEnabled( true );
        }
        catch( AWTException ex )
        {
            ex.printStackTrace();
            timer.stop();
        }
    }

    /***************************************************************************
     * Stops the update of the selected swatch when the mouse is moved.
     **************************************************************************/
    private void stopPicking()
    {
        if( timer != null )
        {
            timer.stop();
            timer = null;
            zoomArea.reset();
            grabAction.setEnabled( false );
            pickerButton.setSelected( false );
        }
    }
}
