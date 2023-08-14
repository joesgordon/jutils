package jutils.core.ui.net;

import java.awt.Point;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPopupMenu;

import jutils.core.io.IParser;
import jutils.core.io.parsers.EndPointParser;
import jutils.core.net.EndPoint;
import jutils.core.net.IpAddress;
import jutils.core.net.NetUtils;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.IDataFormField;
import jutils.core.ui.fields.ParserFormField;
import jutils.core.ui.model.ParserTextFormatter;
import jutils.core.ui.validation.IValidityChangedListener;
import jutils.core.ui.validation.Validity;
import jutils.core.utils.IGetter;

/***************************************************************************
 *
 **************************************************************************/
public class EndPointField implements IDataFormField<EndPoint>
{
    /**  */
    private final ParserFormField<EndPoint> field;
    /**  */
    private final IGetter<List<IpAddress>> presetBuilder;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public EndPointField( String name )
    {
        this( name, new EndPointParser() );
    }

    /***************************************************************************
     * @param name
     * @param parser
     **************************************************************************/
    public EndPointField( String name, IParser<EndPoint> parser )
    {
        this( name, parser, null );
    }

    /**
     * @param name
     * @param parser
     * @param presetBuilder
     */
    public EndPointField( String name, IParser<EndPoint> parser,
        IGetter<List<IpAddress>> presetBuilder )
    {
        JFormattedTextField textField = new JFormattedTextField(
            new ParserTextFormatter<>( parser ) );

        IGetter<List<IpAddress>> presets = IpAddressField.buildStdGetter(
            presetBuilder );

        this.field = new ParserFormField<>( name, parser, textField );
        this.presetBuilder = presets;

        setValue( new EndPoint() );

        field.getTextField().addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e.getPoint() ) ) );
    }

    /***************************************************************************
     * @param point
     **************************************************************************/
    private void showMenu( Point point )
    {
        JPopupMenu menu = new JPopupMenu();
        List<IpAddress> ips = presetBuilder.get();

        for( int i = 0; i < ips.size(); i++ )
        {
            IpAddress ip = ips.get( i );

            if( ip != null )
            {
                menu.add( ip.toString() ).addActionListener(
                    ( e ) -> handlePreset( ip ) );
            }
            else if( i != ( ips.size() - 1 ) )
            {
                menu.addSeparator();
            }
        }

        menu.addSeparator();

        menu.add( "Ping" ).addActionListener( ( e ) -> handlePing() );

        menu.show( getView(), point.x, point.y );
    }

    private void handlePreset( IpAddress ip )
    {
        EndPoint ep = getValue();
        ep.address.set( ip );
        setValue( ep );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void handlePing()
    {
        if( field.getValidity().isValid )
        {
            NetUtils.ping( getValue().address, 2000, getView() );
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
    public EndPoint getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( EndPoint value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<EndPoint> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<EndPoint> getUpdater()
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
