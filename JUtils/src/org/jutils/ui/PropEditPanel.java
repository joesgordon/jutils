package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;

import org.jutils.ui.fields.ColorField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PropEditPanel
{
    /**  */
    private JPanel propsPanel;
    /**  */
    private List<JComponent> valueFields;
    /**  */
    private JScrollPane propsScrollPane;
    /**  */
    private Map<Object, Object> props;

    /**  */
    private IRendererFactory renderers;
    /**  */
    private DefaultRendererFactory defaultRenderers;
    /**  */
    @SuppressWarnings( "rawtypes")
    private DefaultRenderer defaultRenderer;

    /***************************************************************************
     * 
     **************************************************************************/
    @SuppressWarnings( "rawtypes")
    public PropEditPanel()
    {
        renderers = null;
        defaultRenderers = new DefaultRendererFactory();
        defaultRenderer = new DefaultRenderer();

        propsScrollPane = new JScrollPane();
        propsScrollPane.getVerticalScrollBar().setUnitIncrement( 10 );

        valueFields = new ArrayList<JComponent>();
        propsPanel = new JPanel( new GridBagLayout() );

        propsScrollPane.setViewportView( propsPanel );

        setProperties( null );

        defaultRenderers.assignRenderer( Boolean.class, new BooleanRenderer() );
        defaultRenderers.assignRenderer( Color.class, new ColorRenderer() );
        defaultRenderers.assignRenderer( ColorUIResource.class,
            new ColorUIResourceRenderer() );
        defaultRenderers.assignRenderer( String.class, new StringRenderer() );
    }

    /***************************************************************************
     * @param displays
     **************************************************************************/
    public void setKeyRenderers( IRendererFactory factory )
    {
        renderers = factory;
    }

    /***************************************************************************
     * @param p
     **************************************************************************/
    public void setProperties( Map<Object, Object> p )
    {
        props = p;

        propsPanel.removeAll();

        if( p != null )
        {
            int row = 0;
            List<Entry<Object, Object>> entries = new ArrayList<Entry<Object, Object>>(
                p.entrySet() );
            GridBagConstraints constraints;

            Collections.sort( entries, new EntryComparator() );

            for( Entry<Object, Object> entry : entries )
            {
                Object key = entry.getKey();
                Object val = p.get( key );
                if( val == null || !( val instanceof Color ) )
                {
                    continue;
                }
                // else if( ( ( Color )val ).getRGB() != 0xFFb8cfe5 )
                // {
                // continue;
                // }
                // else if( !key.toString().toLowerCase().contains( "separator"
                // ) )
                // {
                // continue;
                // }

                String keyText;
                boolean nullKey;

                keyText = key == null ? "null" : key.toString();
                nullKey = key == null;

                JLabel keyField = new JLabel( keyText );
                JComponent valField = getComponent( p, entry );

                valueFields.add( valField );

                if( nullKey )
                {
                    keyField.setOpaque( true );
                    keyField.setBackground( Color.red );
                }

                constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets( 2, 2, 2, 2 ), 0, 0 );
                propsPanel.add( keyField, constraints );

                constraints = new GridBagConstraints( 1, row, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH,
                    new Insets( 2, 2, 2, 2 ), 0, 0 );
                propsPanel.add( valField, constraints );

                row++;
            }

            constraints = new GridBagConstraints( 0, row, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 );
            propsPanel.add( Box.createVerticalStrut( 0 ), constraints );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Map<Object, Object> getProperties()
    {
        return props;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Container getView()
    {
        return propsScrollPane;
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        for( JComponent field : valueFields )
        {
            field.setEnabled( enabled );
        }
    }

    @SuppressWarnings( "unchecked")
    private JComponent getComponent( Map<Object, Object> p,
        Entry<Object, Object> entry )
    {
        JComponent comp = null;

        comp = getComponentByValue( p, entry );
        if( comp == null )
        {
            comp = getComponentByKey( p, entry );
            if( comp == null )
            {
                comp = defaultRenderer.createWidget( props, entry.getKey(),
                    p.get( entry.getKey() ) );
            }
        }

        return comp;
    }

    @SuppressWarnings( { "rawtypes", "unchecked" })
    private JComponent getComponentByValue( Map<Object, Object> p,
        Entry<Object, Object> entry )
    {
        JComponent comp = null;
        Object key = entry.getKey();
        Object value = p.get( key );

        if( value != null )
        {
            IRenderer renderer = renderers == null ? null
                : renderers.getRenderer( value.getClass() );

            if( renderer == null )
            {
                renderer = defaultRenderers.getRenderer( value.getClass() );
                if( renderer == null )
                {
                    renderer = defaultRenderer;
                }
            }

            if( renderer != null )
            {
                comp = renderer.createWidget( props, entry.getKey(),
                    p.get( entry.getKey() ) );
            }
        }

        return comp;
    }

    @SuppressWarnings( { "rawtypes", "unchecked" })
    private JComponent getComponentByKey( Map<Object, Object> p,
        Entry<Object, Object> entry )
    {
        JComponent comp = null;
        IRenderer renderer = renderers == null ? null
            : renderers.getRenderer( entry.getKey() );

        if( renderer == null )
        {
            renderer = defaultRenderers.getRenderer( entry.getKey() );
            if( renderer == null )
            {
                renderer = defaultRenderer;
            }
        }

        if( renderer != null )
        {
            comp = renderer.createWidget( props, entry.getKey(),
                p.get( entry.getKey() ) );
        }

        return comp;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IRenderer<T>
    {
        public JComponent createWidget( Map<Object, Object> props, Object key,
            T value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IRendererFactory
    {
        public IRenderer<Object> getRenderer( Object key );

        public <T> IRenderer<T> getRenderer( Class<T> c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class DefaultRendererFactory implements IRendererFactory
    {
        /**  */
        private final Map<Object, IRenderer<Object>> keyRenderers;
        /**  */
        private final Map<Class<?>, IRenderer<?>> valueRenderers;

        public DefaultRendererFactory()
        {
            keyRenderers = new HashMap<Object, IRenderer<Object>>();
            valueRenderers = new HashMap<Class<?>, IRenderer<?>>();
        }

        @Override
        public IRenderer<Object> getRenderer( Object key )
        {
            return keyRenderers.get( key );
        }

        public void assignRenderer( Object key, IRenderer<Object> renderer )
        {
            keyRenderers.put( key, renderer );
        }

        @Override
        @SuppressWarnings( "unchecked")
        public <T> IRenderer<T> getRenderer( Class<T> c )
        {
            return ( IRenderer<T> )valueRenderers.get( c );
        }

        public <T> void assignRenderer( Class<T> c, IRenderer<T> renderer )
        {
            valueRenderers.put( c, renderer );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorUIResourceRenderer
        implements IRenderer<ColorUIResource>
    {
        private ColorRenderer renderer = new ColorRenderer();

        @Override
        public JComponent createWidget( Map<Object, Object> props, Object key,
            ColorUIResource value )
        {
            return renderer.createWidget( props, key, value );
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ColorRenderer implements IRenderer<Color>
    {
        @Override
        public JComponent createWidget( Map<Object, Object> props, Object key,
            Color c )
        {
            ColorField button = new ColorField( "Color" );

            button.setValue( c );

            button.setUpdater( ( c1 ) -> props.put( key, c1 ) );

            return button.getView();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class BooleanRenderer implements IRenderer<Boolean>
    {
        @Override
        public JComponent createWidget( Map<Object, Object> props, Object key,
            Boolean t )
        {
            JCheckBox checkBox = new JCheckBox();

            checkBox.setSelected( t.booleanValue() );
            checkBox.setEnabled( false );
            checkBox.addActionListener( new CheckBoxListener( props, key ) );

            return checkBox;
        }

        private static class CheckBoxListener implements ActionListener
        {
            private Map<Object, Object> props;
            private Object key;

            public CheckBoxListener( Map<Object, Object> props, Object key )
            {
                this.props = props;
                this.key = key;
            }

            @Override
            public void actionPerformed( ActionEvent e )
            {
                JCheckBox jcb = ( JCheckBox )e.getSource();

                props.put( key, jcb.isSelected() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DefaultRenderer<T> implements IRenderer<T>
    {
        @Override
        public JComponent createWidget( Map<Object, Object> props, Object key,
            T value )
        {
            String text;
            JTextField valField;

            valField = new JTextField();

            if( value == null )
            {
                valField.setToolTipText( "Value is null" );
            }
            else
            {
                text = value.toString();

                if( text == null )
                {
                    valField.setToolTipText( "Text is null" );
                }
                else
                {
                    valField.setText( text );
                    valField.setToolTipText( value.getClass().toString() );
                }
            }
            valField.setEditable( false );

            return valField;
        }
    }

    public static class StringRenderer extends DefaultRenderer<String>
    {
        @Override
        public JComponent createWidget( Map<Object, Object> props, Object key,
            String value )
        {
            JTextField field = ( JTextField )super.createWidget( props, key,
                value );

            field.getDocument().addDocumentListener(
                new ValueDocumentListener( field, props, key ) );
            field.setEditable( true );

            return field;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueDocumentListener implements DocumentListener
    {
        private final Object key;
        private final JTextField field;
        private final Map<Object, Object> props;

        public ValueDocumentListener( JTextField field, Map<Object, Object> map,
            Object key )
        {
            this.field = field;
            this.key = key;
            this.props = map;
        }

        private void updateValue()
        {
            props.put( key, field.getText() );
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            updateValue();
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            updateValue();
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {

            updateValue();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EntryComparator
        implements Comparator<Entry<Object, Object>>
    {
        @Override
        public int compare( Entry<Object, Object> thisEntry,
            Entry<Object, Object> thatEntry )
        {
            return thisEntry.getKey().toString().compareTo(
                thatEntry.getKey().toString() );
        }
    }
}
