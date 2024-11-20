package jutils.hexulator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.io.parsers.Ip4AddressParser;
import jutils.core.net.IpAddress;
import jutils.core.ui.net.IpAddressField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressesDataField extends DefaultDataField
{
    /**  */
    private final IpAddressField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ip4AddressesDataField()
    {
        this.fields = new IpAddressField[2];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new IpAddressField( "", new Ip4AddressParser() );
            fields[i].getTextField().setColumns( TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, IpAddress data )
    {
        try
        {
            super.stream.seek( index * Integer.BYTES );
            super.stream.writeInt( data.getValue() );
            super.updater.update( super.data );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addTo( String name, JPanel panel, int row )
    {
        DefaultDataField.addTo( fields, name, panel, row );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    protected void setData( DataStream stream ) throws IOException
    {
        for( IpAddressField field : fields )
        {
            IpAddress ip = field.getValue();
            int value = stream.readInt();

            ip.setValue( value );

            field.setValue( ip );
        }
    }
}
