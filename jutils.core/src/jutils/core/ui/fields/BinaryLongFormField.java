package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.NumberParsingUtils;
import jutils.core.io.IParser;
import jutils.core.io.parsers.BinaryLongParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a hexadecimal long validater.
 ******************************************************************************/
public class BinaryLongFormField implements IDataFormField<Long>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<Long> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public BinaryLongFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public BinaryLongFormField( String name, String units )
    {
        this( name, units, 20 );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public BinaryLongFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public BinaryLongFormField( String name, String units, Long min, Long max )
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
    public BinaryLongFormField( String name, String units, int columns,
        Long min, Long max )
    {
        IDescriptor<Long> descriptor = ( d ) -> toString( d );
        IParser<Long> parser = new BinaryLongParser( min, max );

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
    public Long getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Long value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Long> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Long> getUpdater()
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
    private static String toString( Long value )
    {
        return value == null ? ""
            : NumberParsingUtils.toLeadingBinaryString( value );
    }
}
