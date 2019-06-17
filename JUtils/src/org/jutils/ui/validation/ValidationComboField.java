package org.jutils.ui.validation;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDescriptor;
import org.jutils.ui.model.ItemComboBoxModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ValidationComboField<T> implements IValidationField
{
    /**  */
    private final ItemComboBoxModel<T> model;
    /**  */
    private final JComboBox<T> field;
    /**  */
    private final ValidationComboRenderer renderer;
    /**  */
    private final ValidityListenerList listenerList;
    /**  */
    private final IDescriptor<T> descriptor;

    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ValidationComboField( T [] items )
    {
        this( Arrays.asList( items ) );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ValidationComboField( List<T> items )
    {
        this( items, new ObjectDescriptor<T>() );
    }

    /***************************************************************************
     * @param items
     * @param descriptor
     **************************************************************************/
    public ValidationComboField( List<T> items, IDescriptor<T> descriptor )
    {
        this.model = new ItemComboBoxModel<>( items );
        this.field = new JComboBox<>( model );
        this.listenerList = new ValidityListenerList();
        this.renderer = new ValidationComboRenderer();
        this.descriptor = descriptor != null ? descriptor
            : new ObjectDescriptor<T>();

        this.validBackground = field.getBackground();
        this.invalidBackground = Color.red;

        field.setRenderer( renderer );
        field.setBackground( validBackground );
        field.addItemListener(
            new ComboBoxUpdater<T>( new ItemChangedListener<T>( this ) ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComboBox<T> getView()
    {
        return field;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelectedItem()
    {
        return model.getSelectedItem();
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void setSelectedItem( T item )
    {
        int index = model.indexOf( item );

        if( index > -1 )
        {
            model.setSelectedItem( item );
        }
        else
        {
            model.setSelectedItem( null );
        }
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        model.setItems( items );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getItems()
    {
        return model.getItems();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return listenerList.getValidity();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.addListener( vcl );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.removeListener( vcl );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( IParser<T> parser )
    {
        field.setEditable( parser != null );
        ValidationComboEditor editor = new ValidationComboEditor( parser );

        editor.field.addValidityChanged(
            new EditorValidityChangedListener( listenerList ) );
        field.setEditor( editor );
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addItemListener( ItemListener listener )
    {
        field.addItemListener( listener );
    }

    /***************************************************************************
     * @param renderer
     **************************************************************************/
    public void setRenderer( ListCellRenderer<Object> renderer )
    {
        this.renderer.userRenderer = renderer;
    }

    /***************************************************************************
     * @param editor
     **************************************************************************/
    public void setEditor( ComboBoxEditor editor )
    {
        field.setEditor( editor );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSelectedIndex()
    {
        return field.getSelectedIndex();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ItemChangedListener<T> implements IUpdater<T>
    {
        private ValidationComboField<T> field;

        public ItemChangedListener( ValidationComboField<T> field )
        {
            this.field = field;
        }

        @Override
        public void update( T data )
        {
            Object item = field.field.getSelectedItem();

            if( item == null )
            {
                field.field.setBackground( field.invalidBackground );
                field.listenerList.signalValidity( "No item chosen" );
            }
            else
            {
                field.field.setBackground( field.validBackground );
                field.listenerList.signalValidity();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ObjectDescriptor<T> implements IDescriptor<T>
    {
        @Override
        public String getDescription( T item )
        {
            if( item == null )
            {
                return "";
            }

            return item.toString();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ValidationComboRenderer implements ListCellRenderer<T>
    {
        public ListCellRenderer<Object> userRenderer;
        public final DefaultListCellRenderer defaultRenderer;

        public ValidationComboRenderer()
        {
            this.userRenderer = null;
            this.defaultRenderer = new DefaultListCellRenderer();
        }

        @Override
        public Component getListCellRendererComponent( JList<? extends T> list,
            T value, int index, boolean isSelected, boolean cellHasFocus )
        {
            Component c = null;

            if( userRenderer == null )
            {
                c = defaultRenderer.getListCellRendererComponent( list, value,
                    index, isSelected, cellHasFocus );
                defaultRenderer.setText( descriptor.getDescription( value ) );
            }
            else
            {
                c = userRenderer.getListCellRendererComponent( list, value,
                    index, isSelected, cellHasFocus );
            }

            return c;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ValidationComboEditor implements ComboBoxEditor
    {
        private final IParser<T> parser;
        public final ValidationTextField field;

        public ValidationComboEditor( IParser<T> parser )
        {
            this.parser = parser;
            this.field = new ValidationTextField();
            field.setValidator( ( t ) -> parser.parse( t ) );
        }

        @Override
        public Component getEditorComponent()
        {
            return field.getView();
        }

        @Override
        public void setItem( Object obj )
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )obj;
            field.setText( descriptor.getDescription( item ) );
        }

        @Override
        public T getItem()
        {
            try
            {
                T item = parser.parse( field.getText() );
                return item;
            }
            catch( ValidationException e )
            {
                return null;
            }
        }

        @Override
        public void selectAll()
        {
            field.getView().selectAll();
        }

        @Override
        public void addActionListener( ActionListener l )
        {
            field.getView().addActionListener( l );
        }

        @Override
        public void removeActionListener( ActionListener l )
        {
            field.getView().removeActionListener( l );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class EditorValidityChangedListener
        implements IValidityChangedListener
    {
        private ValidityListenerList list;

        public EditorValidityChangedListener( ValidityListenerList list )
        {
            this.list = list;
        }

        @Override
        public void signalValidity( Validity validity )
        {
            list.signalValidity( validity );
        }
    }
}
