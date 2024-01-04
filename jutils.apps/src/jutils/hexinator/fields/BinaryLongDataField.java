package jutils.hexinator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.BinaryLongFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BinaryLongDataField extends DefaultDataField
{
    /**  */
    private final BinaryLongFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public BinaryLongDataField()
    {
        this.fields = new BinaryLongFormField[1];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new BinaryLongFormField( "" );
            fields[i].getTextField().setColumns( TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, long data )
    {
        try
        {
            super.stream.seek( index * Long.BYTES );
            super.stream.writeLong( data );
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
        for( BinaryLongFormField field : fields )
        {
            field.setValue( stream.readLong() );
        }
    }
}
