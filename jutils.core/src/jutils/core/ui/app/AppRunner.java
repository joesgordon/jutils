package jutils.core.ui.app;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBluer;

import jutils.core.OptionUtils;
import jutils.core.laf.SimpleLookAndFeel;
import jutils.core.ui.StandardUncaughtExceptionHandler;

/*******************************************************************************
 * Utility class for starting applications on the Swing event dispatch thread.
 ******************************************************************************/
public final class AppRunner
{
    /**  */
    public static final String JGOODIES_LAF = Options.PLASTICXP_NAME;
    /**  */
    public static final String METAL_LAF = MetalLookAndFeel.class.getName();
    /**  */
    public static final String SIMPLE_LAF = SimpleLookAndFeel.class.getName();
    /**  */
    public static String DEFAULT_LAF = UIManager.getCrossPlatformLookAndFeelClassName();
    // public static final String DEFAULT_LAF = SIMPLE_LAF;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private AppRunner()
    {
    }

    /***************************************************************************
     * Sets the look-and-feel used when applications are displayed using this
     * class.
     * @param laf the name of the look-and-feel to be the default.
     **************************************************************************/
    public static void setDefaultLaf( String laf )
    {
        DEFAULT_LAF = laf;
    }

    /***************************************************************************
     * Applies the default look-and-feel, {@link #DEFAULT_LAF}.
     * @throws IllegalStateException if there is a problem creating or applying
     * the look-and-feel.
     **************************************************************************/
    public static void applyDefaultLaf() throws IllegalStateException
    {
        applyLaf( DEFAULT_LAF );
    }

    /***************************************************************************
     * Applies the provided look-and-feel, or {@link #DEFAULT_LAF} if
     * {@code null}.
     * @param lafName the name of the look-and-feel to be applied.
     * @throws IllegalStateException if there is a problem creating or applying
     * the look-and-feel.
     **************************************************************************/
    public static void applyLaf( String lafName ) throws IllegalStateException
    {
        if( lafName == null )
        {
            lafName = DEFAULT_LAF;
        }

        if( lafName.equals( JGOODIES_LAF ) )
        {
            setJGoodiesLaf();
            // lafName = JUtilsLookAndFeel.class.getName();
        }

        try
        {
            UIManager.setLookAndFeel( lafName );
        }
        catch( ClassNotFoundException ex )
        {
            throw new IllegalStateException(
                "Cannot find the class for the provided Look-and-Feel: " +
                    lafName,
                ex );
        }
        catch( InstantiationException ex )
        {
            throw new IllegalStateException(
                "Cannot create the provided Look-and-Feel: " + lafName, ex );
        }
        catch( IllegalAccessException ex )
        {
            throw new IllegalStateException(
                "Cannot access the provided Look-and-Feel: " + lafName, ex );
        }
        catch( UnsupportedLookAndFeelException ex )
        {
            throw new IllegalStateException(
                "Cannot support the provided Look-and-Feel: " + lafName, ex );
        }

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
            "focusOwner", new ScrollPaneFocusListener() );
    }

    /***************************************************************************
     * @param frame
     * @param validate
     **************************************************************************/
    public static void prepareAndShowFrame( JFrame frame, boolean validate )
    {
        // ---------------------------------------------------------------------
        // Validate frames that have preset sizes. Pack frames that have
        // useful preferred size info, e.g. from their layout.
        // ---------------------------------------------------------------------
        if( validate )
        {
            frame.validate();
        }
        else
        {
            frame.pack();
        }

        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    /***************************************************************************
     * @param <T>
     * @param message
     * @param title
     * @param options
     * @param defaultValue
     * @return
     **************************************************************************/
    public static <T> T invokeOptions( String message, String title,
        T [] options, T defaultValue )
    {
        OptionsApp<T> app = new OptionsApp<>( message, title, options,
            defaultValue );

        invokeAndWait( app );

        return app.getAnswer();
    }

    /***************************************************************************
     * @param message
     * @param title
     **************************************************************************/
    public static void invokeError( String message, String title )
    {
        invokeLater(
            () -> OptionUtils.showErrorMessage( null, message, title ) );
    }

    /***************************************************************************
     * @param uiCreateAndShow
     **************************************************************************/
    public static void invokeLater( Runnable uiCreateAndShow )
    {
        invokeLater( new SimpleApp( uiCreateAndShow ) );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void invokeLater( IApplication app )
    {
        SwingUtilities.invokeLater( () -> runApp( app ) );
    }

    /***************************************************************************
     * @param uiCreateAndShow
     **************************************************************************/
    public static void invokeAndWait( Runnable uiCreateAndShow )
    {
        invokeAndWait( new SimpleApp( uiCreateAndShow ) );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    public static void invokeAndWait( IApplication app )
    {
        try
        {
            SwingUtilities.invokeAndWait( () -> runApp( app ) );
        }
        catch( InvocationTargetException ex )
        {
        }
        catch( InterruptedException ex )
        {
        }
    }

    /***************************************************************************
     * Validates the frame from the provided creator and starts it on the swing
     * event queue using the default look-n-feel set by
     * {@link AppRunner#applyDefaultLaf()}.
     * @param frameCreator the callback to create the frame.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator )
    {
        invokeLater( new SimpleFrameApp( frameCreator ) );
    }

    /***************************************************************************
     * Validates the frame from the provided app and starts it on the swing
     * event queue using the default look-n-feel set by
     * {@link AppRunner#applyDefaultLaf()}.
     * @param app the application to be started.
     **************************************************************************/
    public static void invokeLater( IFrameApp app )
    {
        invokeLater( app, true );
    }

    /***************************************************************************
     * Starts the frame from the provided creator on the swing event queue using
     * the default look-n-feel set by {@link AppRunner#applyDefaultLaf()}.
     * @param frameCreator the callback to create the frame.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator,
        boolean validate )
    {
        invokeLater( new SimpleFrameApp( frameCreator ), validate,
            AppRunner.DEFAULT_LAF );
    }

    /***************************************************************************
     * Starts the frame from the provided app on the swing event queue using the
     * default look-n-feel set by {@link AppRunner#applyDefaultLaf()}.
     * @param app the application to be started.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate )
    {
        invokeLater( app, validate, AppRunner.DEFAULT_LAF );
    }

    /***************************************************************************
     * Starts the frame from the provided creator on the swing event queue using
     * the provided look-n-feel.
     * @param frameCreator the callback to create the frame.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     * @param lookAndFeel the name of the look-n-feel to use.
     **************************************************************************/
    public static void invokeLater( IFrameCreator frameCreator,
        boolean validate, String lookAndFeel )
    {
        invokeLater( new SimpleFrameApp( frameCreator ), validate,
            lookAndFeel );
    }

    /***************************************************************************
     * Starts the frame from the provided app on the swing event queue using the
     * provided look-n-feel.
     * @param app the application to be started.
     * @param validate indicates the application's frame should be validated if
     * {@code true}; packs the frame if {@code false}.
     * @param lookAndFeel the name of the look-n-feel to use.
     **************************************************************************/
    public static void invokeLater( IFrameApp app, boolean validate,
        String lookAndFeel )
    {
        IApplication fApp = new FrameApplication( app, validate, lookAndFeel );

        AppRunner.invokeLater( fApp );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static void setJGoodiesLaf()
    {
        Color c;
        c = new Color( 0x3A6EA7 );

        UIManager.put( "activeCaptionBorder", c );
        UIManager.put( "desktop", c );
        UIManager.put( "Button.focus", c );
        UIManager.put( "CheckBox.focus", c );
        UIManager.put( "Desktop.background", c );
        UIManager.put( "ProgressBar.foreground", c );
        UIManager.put( "RadioButton.focus", c );
        UIManager.put( "ScrollBar.thumb", c );
        UIManager.put( "Slider.focus", c );
        UIManager.put( "TabbedPane.focus", c );
        UIManager.put( "Slider.foreground", c );
        UIManager.put( "Table.dropLineColor", c );
        UIManager.put( "ToggleButton.focus", c );
        UIManager.put( "Tree.selectionBorderColor", c );

        c = new Color( 0x0A315A );

        UIManager.put( "CheckBox.check", c );
        UIManager.put( "CheckBoxMenuItem.acceleratorSelectionBackground", c );
        UIManager.put( "CheckBoxMenuItem.selectionBackground", c );
        UIManager.put( "EditorPane.selectionBackground", c );
        UIManager.put( "FormattedTextField.selectionBackground", c );
        UIManager.put( "List.selectionBackground", c );
        UIManager.put( "Menu.acceleratorForeground", c );
        UIManager.put( "MenuItem.acceleratorSelectionBackground", c );
        UIManager.put( "MenuItem.selectionBackground", c );
        UIManager.put( "PasswordField.selectionBackground", c );
        UIManager.put( "RadioButton.check", c );
        UIManager.put( "RadioButtonMenuItem.acceleratorSelectionBackground",
            c );
        UIManager.put( "RadioButtonMenuItem.selectionBackground", c );
        UIManager.put( "ScrollBar.thumbShadow", c );
        UIManager.put( "SimpleInternalFrame.activeTitleBackground", c );
        UIManager.put( "Table.dropLineShortColor", c );
        UIManager.put( "Table.selectionBackground", c );
        UIManager.put( "TextArea.selectionBackground", c );
        UIManager.put( "TextField.selectionBackground", c );
        UIManager.put( "TextPane.selectionBackground", c );
        UIManager.put( "TitledBorder.titleColor", c );
        UIManager.put( "ToolBar.dockingForeground", c );
        UIManager.put( "Tree.selectionBackground", c );
        UIManager.put( "textHighlight", c );

        PlasticLookAndFeel.setPlasticTheme( new DesertBluer() );
        Options.setSelectOnFocusGainEnabled( true );

        UIManager.put( "TabbedPaneUI",
            BasicTabbedPaneUI.class.getCanonicalName() );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    private static void runApp( IApplication app )
    {
        try
        {
            AppRunner.applyLaf( app.getLookAndFeelName() );

            app.createAndShowUi();
        }
        catch( IllegalStateException ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * Creates a JFrame to be displayed.
     **************************************************************************/
    public static interface IFrameCreator
    {
        /**
         * Creates the frame and all other UI (tray icons, etc.)
         * @return the frame for this application.
         */
        public JFrame createFrame();
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class OptionsApp<T> implements IApplication
    {
        /**  */
        private final String message;
        /**  */
        private final String title;
        /**  */
        private final T [] selections;
        /**  */
        private final T defaultValue;

        /**  */
        private T answer;

        /**
         * @param message
         * @param title
         * @param selections
         * @param defaultValue
         */
        public OptionsApp( String message, String title, T [] selections,
            T defaultValue )
        {
            this.message = message;
            this.title = title;
            this.selections = selections;
            this.defaultValue = defaultValue;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void createAndShowUi()
        {
            answer = OptionUtils.showOptionMessage( null, message, title,
                selections, defaultValue );
        }

        /**
         * @return
         */
        public T getAnswer()
        {
            return answer;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SimpleApp implements IApplication
    {
        /**  */
        private final Runnable callback;

        /**
         * @param callback
         */
        public SimpleApp( Runnable callback )
        {
            this.callback = callback;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLookAndFeelName()
        {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void createAndShowUi()
        {
            callback.run();
        }
    }

    /***************************************************************************
     * Defines an {@link IFrameApp} that uses an {@code IFrameCreator} to create
     * the frame and does nothing to finalize the GUI.
     **************************************************************************/
    private final static class SimpleFrameApp implements IFrameApp
    {
        /** The base creator */
        private IFrameCreator frameCreator;

        /**
         * Creates a new basic app with the provided creator.
         * @param frameCreator the base creator.
         */
        public SimpleFrameApp( IFrameCreator frameCreator )
        {
            this.frameCreator = frameCreator;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JFrame createFrame()
        {
            return frameCreator.createFrame();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FrameApplication implements IApplication
    {
        /**  */
        private final IFrameApp frameApp;
        /**  */
        private final boolean validate;
        /**  */
        private final String lookAndFeel;

        /**
         * @param frameApp
         * @param validate
         * @param lookAndFeel
         */
        public FrameApplication( IFrameApp frameApp, boolean validate,
            String lookAndFeel )
        {
            this.frameApp = frameApp;
            this.validate = validate;
            this.lookAndFeel = lookAndFeel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLookAndFeelName()
        {
            return lookAndFeel;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void createAndShowUi()
        {
            createAndShowFrame();
        }

        /**
         * @return
         */
        public JFrame createAndShowFrame()
        {
            JFrame frame = frameApp.createFrame();

            UncaughtExceptionHandler h;
            h = new StandardUncaughtExceptionHandler( frame );
            Thread.setDefaultUncaughtExceptionHandler( h );

            prepareAndShowFrame( frame, validate );

            frameApp.finalizeGui();

            return frame;
        }
    }
}
