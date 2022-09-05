package org.jutils.core.ui.fields;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import org.jutils.core.io.IParser;
import org.jutils.core.io.parsers.IpAddressParser;
import org.jutils.core.net.IpAddress;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.model.ParserTextFormatter;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IpAddressField implements IDataFormField<IpAddress>
{
    /**  */
    private final ParserFormField<IpAddress> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public IpAddressField( String name )
    {
        this( name, new IpAddressParser() );
    }

    /***************************************************************************
     * @param name
     * @param parser
     **************************************************************************/
    public IpAddressField( String name, IParser<IpAddress> parser )
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
    public IpAddress getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( IpAddress value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<IpAddress> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<IpAddress> getUpdater()
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
