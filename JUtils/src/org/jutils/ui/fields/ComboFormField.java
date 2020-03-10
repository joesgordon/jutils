package org.jutils.ui.fields;

import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.ValidationComboField;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ComboFormField<T> implements IDataFormField<T>
{
    /**  */
    private final ValidationComboField<T> field;
    /**  */
    private final String name;

    /**  */
    private IUpdater<T> updater;
    /**  */
    private boolean isSetting;

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboFormField( String name, T [] items )
    {
        this( name, items, null );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboFormField( String name, T [] items, IDescriptor<T> descriptor )
    {
        this( name, Arrays.asList( items ), descriptor );
    }

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboFormField( String name, List<T> items )
    {
        this( name, items, null );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboFormField( String name, List<T> items,
        IDescriptor<T> descriptor )
    {
        this.field = new ValidationComboField<>( items, descriptor );
        this.name = name;

        this.updater = null;
        this.isSetting = false;

        field.addItemListener(
            new ComboBoxUpdater<>( ( d ) -> handleDataChanged() ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handleDataChanged()
    {
        if( updater != null && !isSetting )
        {
            updater.update( getValue() );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComboBox<T> getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public T getValue()
    {
        return field.getSelectedItem();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        isSetting = true;
        field.setSelectedItem( value );
        isSetting = false;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.getView().setEnabled( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param r
     **************************************************************************/
    public void setRenderer( ListCellRenderer<Object> r )
    {
        field.setRenderer( r );
    }

    /***************************************************************************
     * @param editor
     **************************************************************************/
    public void setEditor( ComboBoxEditor editor )
    {
        field.setEditor( editor );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setValues( List<T> items )
    {
        field.setItems( items );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getValues()
    {
        return field.getItems();
    }

    /***************************************************************************
     * @param parser
     **************************************************************************/
    public void setUserEditable( IParser<T> parser )
    {
        if( parser != null )
        {
            parser = new ParserListener<T>( parser, this );
        }

        field.setEditable( parser );
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
    private static class ParserListener<T> implements IParser<T>
    {
        private final IParser<T> parser;
        private final ComboFormField<T> field;

        public ParserListener( IParser<T> parser, ComboFormField<T> field )
        {
            this.parser = parser;
            this.field = field;
        }

        @Override
        public T parse( String str ) throws ValidationException
        {
            T item = parser.parse( str );

            if( field.updater != null )
            {
                field.updater.update( item );
            }

            return item;
        }
    }
}
