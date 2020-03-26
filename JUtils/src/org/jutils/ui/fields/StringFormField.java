package org.jutils.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.io.IParser;
import org.jutils.io.parsers.StringLengthParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validator.
 ******************************************************************************/
public class StringFormField implements IDataFormField<String>
{
    /**  */
    private final JTextField textField;
    /**  */
    private final ParserFormField<String> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringFormField( String name )
    {
        this( name, null, null );
    }

    /***************************************************************************
     * @param name
     * @param minLen
     * @param maxLen
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen )
    {
        this( name, minLen, maxLen, true );
    }

    /***************************************************************************
     * @param name
     * @param minLen
     * @param maxLen
     * @param trim
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen,
        boolean trim )
    {
        this( name, minLen, maxLen, trim, 20 );
    }

    /***************************************************************************
     * @param name
     * @param minLen
     * @param maxLen
     * @param trim
     * @param columns
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen,
        boolean trim, int columns )
    {
        IParser<String> parser = new StringLengthParser( minLen, maxLen, trim );
        IDescriptor<String> descriptor = ( d ) -> d == null ? "" : d;

        this.textField = new JTextField( columns );
        this.field = new ParserFormField<>( name, parser, textField, descriptor,
            textField );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
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
    public String getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.setEditable( editable );
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
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField;
    }
}
