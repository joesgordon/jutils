package org.jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.core.io.parsers.ShortParser;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class ShortFormField implements IDataFormField<Short>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<Short> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public ShortFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public ShortFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public ShortFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public ShortFormField( String name, String units, Short min, Short max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param min
     * @param max
     * @param updater
     **************************************************************************/
    public ShortFormField( String name, String units, int columns, Short min,
        Short max )
    {
        IDescriptor<Short> descriptor = ( d ) -> toString( d );

        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name, new ShortParser( min, max ),
            textField, descriptor, textField, units );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Short getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Short value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Short> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Short> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
    }

    /***************************************************************************
     * 
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
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private String toString( Short value )
    {
        return value == null ? "" : Short.toString( value );
    }
}
