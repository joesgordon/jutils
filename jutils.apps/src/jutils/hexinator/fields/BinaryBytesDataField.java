package jutils.hexinator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.BinaryByteFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BinaryBytesDataField extends DefaultDataField
{
    /**  */
    private final BinaryByteFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public BinaryBytesDataField()
    {
        this.fields = new BinaryByteFormField[8];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new BinaryByteFormField( "" );
            fields[i].getTextField().setColumns( TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, byte data )
    {
        try
        {
            super.stream.seek( index * Byte.BYTES );
            super.stream.write( data );
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
        for( BinaryByteFormField field : fields )
        {
            field.setValue( stream.read() );
        }
    }
}
