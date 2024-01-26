package jutils.hexulator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.HexLongFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexLongDataField extends DefaultDataField
{
    /**  */
    private final HexLongFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexLongDataField()
    {
        this.fields = new HexLongFormField[1];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new HexLongFormField( "" );
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
        for( HexLongFormField field : fields )
        {
            field.setValue( stream.readLong() );
        }
    }
}
