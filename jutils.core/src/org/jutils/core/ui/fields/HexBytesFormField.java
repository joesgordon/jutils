package org.jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.core.io.parsers.HexBytesParser;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.hex.HexUtils;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that validates strings as hexadecimal byte [].
 ******************************************************************************/
public class HexBytesFormField implements IDataFormField<byte []>
{
    /** The name of the field. */
    private final String name;
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<byte []> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public HexBytesFormField( String name )
    {
        this( name, 20 );
    }

    /***************************************************************************
     * @param name
     * @param columns
     * @param updater
     **************************************************************************/
    public HexBytesFormField( String name, int columns )
    {
        this.name = name;
        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name, new HexBytesParser(),
            textField, ( d ) -> toString( d ) );
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
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( byte [] value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<byte []> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<byte []> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField;
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
     * @param value
     * @return
     **************************************************************************/
    private String toString( byte [] value )
    {
        return value == null ? "" : HexUtils.toHexString( value );
    }
}
