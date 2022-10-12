package org.jutils.core.ui.fields;

import java.awt.Point;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPopupMenu;

import org.jutils.core.OptionUtils;
import org.jutils.core.io.IParser;
import org.jutils.core.io.parsers.IpAddressParser;
import org.jutils.core.net.IpAddress;
import org.jutils.core.ui.event.RightClickListener;
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

        setValue( new IpAddress() );

        field.getTextField().addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e.getPoint() ) ) );
    }

    /***************************************************************************
     * @param point
     **************************************************************************/
    private void showMenu( Point point )
    {
        JPopupMenu menu = new JPopupMenu();

        menu.add( "127.0.0.1" ).addActionListener(
            ( e ) -> handlePreset( new byte[] { 0, 0, 0, 0 } ) );
        menu.add( "::1" ).addActionListener( ( e ) -> handlePreset(
            new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } ) );

        menu.add( "0.0.0.0" ).addActionListener(
            ( e ) -> handlePreset( new byte[] { 0, 0, 0, 0 } ) );
        menu.add( "::0" ).addActionListener( ( e ) -> handlePreset(
            new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } ) );

        menu.addSeparator();

        menu.add( "Ping" ).addActionListener( ( e ) -> handlePing() );

        menu.show( getView(), point.x, point.y );
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void handlePreset( byte [] value )
    {
        IpAddress address = new IpAddress();

        address.set( value );

        setValue( address );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handlePing()
    {
        if( field.getValidity().isValid )
        {
            IpAddress ip = getValue();
            InetAddress addr = ip.getInetAddress();

            try
            {
                if( addr.isReachable( 2000 ) )
                {
                    OptionUtils.showInfoMessage( getView(),
                        ip.toString() + " is reachable", "Success" );
                }
                else
                {
                    OptionUtils.showWarningMessage( getView(),
                        ip.toString() + " cannot be reached", "Failed" );
                }
            }
            catch( IOException ex )
            {
                OptionUtils.showErrorMessage( getView(), ex.getMessage(),
                    "Unable to Ping " + ip.toString() );
            }
        }
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
