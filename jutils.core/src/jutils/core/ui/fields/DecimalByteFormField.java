package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.io.IParser;
import jutils.core.io.parsers.DecimalByteParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a hexadecimal byte validater.
 ******************************************************************************/
public class DecimalByteFormField implements IDataFormField<Byte>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<Byte> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DecimalByteFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public DecimalByteFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public DecimalByteFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public DecimalByteFormField( String name, String units, Byte min, Byte max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param min
     * @param max
     **************************************************************************/
    public DecimalByteFormField( String name, String units, int columns,
        Byte min, Byte max )
    {
        IDescriptor<Byte> descriptor = ( d ) -> toString( d );
        IParser<Byte> parser = new DecimalByteParser( min, max );

        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name, parser, textField, descriptor,
            textField, units );
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
    public Byte getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Byte value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Byte> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Byte> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
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
    private static String toString( Byte value )
    {
        return value == null ? "" : "" + value;
    }
}
