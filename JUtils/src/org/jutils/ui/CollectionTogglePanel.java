package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.jutils.io.LogUtils;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * This is a panel that will display a list of toggle or radio buttons to the
 * user that come from a selection of type T
 * @param <T> The type of data to be displayed.
 ******************************************************************************/
public class CollectionTogglePanel<T> implements IView<JPanel>
{
    /**  */
    private final JPanel panel;

    /** Map of types to their corresponding buttons. */
    private final HashMap<T, UserValueStorage<T>> buttonMap;

    /** List of values that are displayed. */
    private final List<T> values;

    /**  */
    private final boolean useToggles;

    /***************************************************************************
     * Creates an empty panel.
     * @param useToggles
     **************************************************************************/
    public CollectionTogglePanel( boolean useToggles )
    {
        this( new ArrayList<T>(), useToggles );
    }

    /***************************************************************************
     * Creates a panel which displays the list of values as choices to the user.
     * @param vals The choices from which the user may choose only one.
     * @param useToggles
     **************************************************************************/
    public CollectionTogglePanel( List<T> vals, boolean useToggles )
    {
        this.useToggles = useToggles;

        this.panel = new JPanel();
        this.buttonMap = new HashMap<T, UserValueStorage<T>>();
        this.values = new ArrayList<>();

        createGui();

        setValues( vals );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return panel;
    }

    /***************************************************************************
     * Add the action listener to all the generated buttons.
     * @param l The listener to be added.
     **************************************************************************/
    public void addActionListener( ActionListener l )
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            button.addActionListener( l );
        }
    }

    /***************************************************************************
     * Removes the action listener from all the generated buttons.
     * @param l The listener to be removed.
     **************************************************************************/
    public void removeActionListener( ActionListener l )
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            button.removeActionListener( l );
        }
    }

    /***************************************************************************
     * Sets the choices to be displayed.
     * @param vals The choices to be displayed.
     **************************************************************************/
    public void setValues( List<T> vals )
    {
        values.clear();
        values.addAll( vals );

        createGui();
    }

    /***************************************************************************
     * Sets the item that is to appear chosen.
     * @param value The choice.
     **************************************************************************/
    public void setValue( T value )
    {
        buttonMap.get( value ).setSelected( true );
    }

    /***************************************************************************
     * Returns the chosen item.
     * @return The chosen item.
     **************************************************************************/
    public T getValue()
    {
        Collection<UserValueStorage<T>> buttons = buttonMap.values();

        for( UserValueStorage<T> button : buttons )
        {
            if( button.isSelected() )
            {
                return button.getValue();
            }
        }

        return null;
    }

    /***************************************************************************
     * Removes the old buttons, if appropriate, and generates the new buttons.
     **************************************************************************/
    private void createGui()
    {
        ButtonGroup group = new ButtonGroup();
        panel.removeAll();
        panel.setLayout( new GridBagLayout() );

        for( int i = 0; i < values.size(); i++ )
        {
            T val = values.get( i );
            AbstractButton button = createButton( val, group );

            if( i == 0 )
            {
                button.setSelected( true );
            }

            panel.add( button,
                new GridBagConstraints( 0, i, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets( 2, 0, 2, 0 ), 0, 0 ) );
        }
    }

    /***************************************************************************
     * Creates a button with the given value.
     * @param val The choice to appear as a button.
     * @param group
     * @return The choice button.
     **************************************************************************/
    private AbstractButton createButton( T val, ButtonGroup group )
    {
        JToggleButton button = null;
        UserValueStorage<T> uvs = null;

        if( useToggles )
        {
            uvs = new ValueButton<T>( new JToggleButton() );
        }
        else
        {
            uvs = new ValueButton<T>( new JRadioButton() );
        }

        uvs.setValue( val );
        button = uvs.getView();
        button.setText( val.toString() );
        group.add( button );
        buttonMap.put( val, uvs );

        return button;
    }

    /***************************************************************************
     * Creates a frame to display this panel with default values. Intent is for
     * debugging purposes only.
     * @param args Ignored arguments to this application.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameRunner.invokeLater( new CollectionTogglePanelApp(), false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class CollectionTogglePanelApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            CollectionToggleFrameView view = new CollectionToggleFrameView();

            return view.getView();
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class CollectionToggleFrameView
        implements IView<JFrame>
    {
        /**  */
        private final StandardFrameView frame;
        /**  */
        private final JLabel label;

        /**
         * 
         */
        public CollectionToggleFrameView()
        {
            this.frame = new StandardFrameView();

            JPanel panel = new JPanel();
            label = new JLabel();

            CollectionTogglePanel<TestEnum> radioPanel;
            CollectionTogglePanel<TestEnum> togglePanel;

            radioPanel = new CollectionTogglePanel<TestEnum>( false );
            togglePanel = new CollectionTogglePanel<TestEnum>( true );

            radioPanel.setValues( Arrays.asList( TestEnum.values() ) );
            radioPanel.getView().setBorder(
                BorderFactory.createTitledBorder( "Collection Panel 0" ) );
            radioPanel.addActionListener(
                ( e ) -> toggleOtherPanel( togglePanel, radioPanel ) );

            togglePanel.setValues( Arrays.asList( TestEnum.values() ) );
            togglePanel.getView().setBorder(
                BorderFactory.createTitledBorder( "Collection Panel 1" ) );
            togglePanel.addActionListener(
                ( e ) -> toggleOtherPanel( radioPanel, togglePanel ) );

            label.setText( radioPanel.getValue() != null
                ? radioPanel.getValue().toString() : "NULL" );

            panel.setLayout( new GridBagLayout() );
            panel.add( radioPanel.getView(),
                new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets( 4, 4, 4, 4 ), 0, 0 ) );
            panel.add( togglePanel.getView(),
                new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets( 4, 4, 4, 4 ), 0, 0 ) );
            panel.add( label,
                new GridBagConstraints( 0, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets( 4, 4, 4, 4 ), 0, 0 ) );

            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setContent( panel );
        }

        /**
         * @param dst
         * @param src
         */
        private void toggleOtherPanel( CollectionTogglePanel<TestEnum> dst,
            CollectionTogglePanel<TestEnum> src )
        {
            TestEnum te = src.getValue();
            String str = te != null ? te.toString() : "NULL";
            String cls = te != null ? te.getClass().getName() : "";
            label.setText( str );
            LogUtils.printDebug( "Clicked [" + cls + "]: " + str );
            dst.setValue( te );
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

    /***************************************************************************
     * An enumeration for use with the test application.
     **************************************************************************/
    private static enum TestEnum
    {
        /**  */
        FIRST,
        /**  */
        SECOND,
        /**  */
        THIRD,
        /**  */
        FORTH,
        /**  */
        FIFTH_SIXTH_SEVENTH;

        @Override
        public String toString()
        {
            switch( this )
            {
                case FIRST:
                    return "The first one!";
                case SECOND:
                    return "The second one!";
                case THIRD:
                    return "The third one!";
                case FORTH:
                    return "The forth one!";
                case FIFTH_SIXTH_SEVENTH:
                    return "The fifth through the seventh ones, inclusive!";
                default:
                    return "I got nothin'!";
            }
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static interface UserValueStorage<T> extends IView<JToggleButton>
    {
        /**
         * @param value
         */
        public void setValue( T value );

        /**
         * @return
         */
        public T getValue();

        /**
         * @return
         */
        public boolean isSelected();

        /**
         * @param selected
         */
        public void setSelected( boolean selected );

        /**
         * @param l
         */
        public void addActionListener( ActionListener l );

        /**
         * @param l
         */
        public void removeActionListener( ActionListener l );
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static final class ValueButton<T> implements UserValueStorage<T>
    {
        /**  */
        private final JToggleButton button;
        /**  */
        private T userValue;

        /**
         * @param button
         */
        public ValueButton( JToggleButton button )
        {
            this.button = button;

            this.userValue = null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setValue( T value )
        {
            userValue = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public T getValue()
        {
            return userValue;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public JToggleButton getView()
        {
            return button;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isSelected()
        {
            return button.isSelected();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setSelected( boolean selected )
        {
            button.setSelected( selected );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addActionListener( ActionListener l )
        {
            button.addActionListener( l );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeActionListener( ActionListener l )
        {
            button.removeActionListener( l );
        }
    }
}
