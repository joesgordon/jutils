package org.jutils.core.ui.app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jutils.core.OptionUtils;
import org.jutils.core.laf.SimpleLookAndFeel;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBluer;

/*******************************************************************************
 * 
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
    // public static final String DEFAULT_LAF = SIMPLE_LAF;
    public static String DEFAULT_LAF = null;

    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private AppRunner()
    {
    }

    /***************************************************************************
     * @throws IllegalStateException
     **************************************************************************/
    public static void setDefaultLaf() throws IllegalStateException
    {
        setLaf( null );
    }

    /***************************************************************************
     * @param lafName
     * @throws IllegalStateException
     **************************************************************************/
    public static void setLaf( String lafName ) throws IllegalStateException
    {
        if( lafName == null )
        {
            lafName = UIManager.getCrossPlatformLookAndFeelClassName();
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
     * @param app
     **************************************************************************/
    public static void invokeLater( IApplication app )
    {
        SwingUtilities.invokeLater( () -> runApp( app ) );
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
     * @param uiCreateAndShow
     **************************************************************************/
    public static void invokeAndWait( Runnable uiCreateAndShow )
    {
        invokeAndWait( new SimpleApp( uiCreateAndShow ) );
    }

    /***************************************************************************
     * @param app
     **************************************************************************/
    private static void runApp( IApplication app )
    {
        try
        {
            AppRunner.setLaf( app.getLookAndFeelName() );

            app.createAndShowUi();
        }
        catch( IllegalStateException ex )
        {
            ex.printStackTrace();
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ScrollPaneFocusListener
        implements PropertyChangeListener
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void propertyChange( PropertyChangeEvent evt )
        {
            if( !( evt.getNewValue() instanceof JComponent ) )
            {
                return;
            }

            Object focusedObj = evt.getNewValue();

            if( focusedObj instanceof Component )
            {
                Component focused = ( Component )focusedObj;
                Container parent = focused.getParent();

                if( parent instanceof JComponent )
                {
                    JComponent focusedParent = ( JComponent )parent;

                    focusedParent.scrollRectToVisible( focused.getBounds() );
                }
            }
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
}
