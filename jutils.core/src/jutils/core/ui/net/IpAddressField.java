package jutils.core.ui.net;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import jutils.core.io.IParser;
import jutils.core.io.parsers.IpAddressParser;
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

/*******************************************************************************
 * 
 ******************************************************************************/
public class IpAddressField implements IDataFormField<IpAddress>
{
    /**  */
    private final ParserFormField<IpAddress> field;
    /**  */
    private final IGetter<List<IpAddress>> presetBuilder;

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
        this( name, parser, null );
    }

    /***************************************************************************
     * @param name
     * @param parser
     * @param presetBuilder
     **************************************************************************/
    public IpAddressField( String name, IParser<IpAddress> parser,
        IGetter<List<IpAddress>> presetBuilder )
    {
        JFormattedTextField textField = new JFormattedTextField(
            new ParserTextFormatter<>( parser ) );

        IGetter<List<IpAddress>> presets = buildStdGetter( presetBuilder );

        this.field = new ParserFormField<>( name, parser, textField );
        this.presetBuilder = presets;

        setValue( new IpAddress() );

        field.getTextField().addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e.getPoint() ) ) );
    }

    /***************************************************************************
     * @param presetBuilder
     * @return
     **************************************************************************/
    static IGetter<List<IpAddress>> buildStdGetter(
        IGetter<List<IpAddress>> presetBuilder )
    {
        IGetter<List<IpAddress>> presets = presetBuilder;
        presets = presets == null ? () -> buildStdPresets() : () -> {
            List<IpAddress> all = buildStdPresets();
            all.add( null );
            all.addAll( presetBuilder.get() );
            return all;
        };

        return presets;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    static List<IpAddress> buildStdPresets()
    {
        List<IpAddress> presets = new ArrayList<>();

        presets.add( new IpAddress( 127, 0, 0, 1 ) );
        presets.add( new IpAddress(
            new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } ) );
        presets.add( new IpAddress( 0, 0, 0, 0 ) );
        presets.add( new IpAddress(
            new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } ) );

        return presets;
    }

    /***************************************************************************
     * @param point
     **************************************************************************/
    protected void showMenu( Point point )
    {
        JPopupMenu menu = new JPopupMenu();
        List<IpAddress> ips = presetBuilder.get();

        for( int i = 0; i < ips.size(); i++ )
        {
            IpAddress ip = ips.get( i );

            if( ip != null )
            {
                menu.add( ip.toString() ).addActionListener(
                    ( e ) -> setValue( ip ) );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private void handlePing()
    {
        if( field.getValidity().isValid )
        {
            NetUtils.ping( getValue(), 2000, getView() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextComponent getTextField()
    {
        return field.getTextField();
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
