package jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import jutils.core.io.IParser;
import jutils.core.io.parsers.StringLengthParser;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;

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
     * @param name the name of this field.
     **************************************************************************/
    public StringFormField( String name )
    {
        this( name, null, null );
    }

    /***************************************************************************
     * @param name name the name of this field.
     * @param minLen the minimum valid length of the string in this field.
     * @param maxLen the maximum valid length of the string in this field.
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen )
    {
        this( name, minLen, maxLen, true );
    }

    /***************************************************************************
     * @param name name the name of this field.
     * @param minLen the minimum valid length of the string in this field.
     * @param maxLen the maximum valid length of the string in this field.
     * @param trim indicates whether space characters entered at the beginning
     * or end are ignored.
     **************************************************************************/
    public StringFormField( String name, Integer minLen, Integer maxLen,
        boolean trim )
    {
        this( name, minLen, maxLen, trim, 20 );
    }

    /***************************************************************************
     * @param name name the name of this field.
     * @param minLen the minimum valid length of the string in this field.
     * @param maxLen the maximum valid length of the string in this field.
     * @param trim indicates whether space characters entered at the beginning
     * or end are ignored.
     * @param columns the number of columns for the {@link JTextField}.
     * @see JTextField#setColumns(int)
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
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField;
    }
}
