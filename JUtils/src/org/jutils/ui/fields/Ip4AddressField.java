package org.jutils.ui.fields;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import org.jutils.io.IParser;
import org.jutils.io.parsers.Ip4AddressParser;
import org.jutils.net.Ip4Address;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.ParserTextFormatter;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressField implements IDataFormField<Ip4Address>
{
    /**  */
    private final ParserFormField<Ip4Address> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public Ip4AddressField( String name )
    {
        this( name, new Ip4AddressParser() );
    }

    /***************************************************************************
     * @param name
     * @param parser
     **************************************************************************/
    public Ip4AddressField( String name, IParser<Ip4Address> parser )
    {
        JFormattedTextField textField = new JFormattedTextField(
            new ParserTextFormatter<>( parser ) );

        this.field = new ParserFormField<>( name, parser, textField );
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
    public Ip4Address getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Ip4Address value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Ip4Address> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Ip4Address> getUpdater()
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
}
