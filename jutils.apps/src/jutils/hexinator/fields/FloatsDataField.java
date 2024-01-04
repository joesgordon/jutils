package jutils.hexinator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.FloatFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FloatsDataField extends DefaultDataField
{
    /**  */
    private final FloatFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public FloatsDataField()
    {
        this.fields = new FloatFormField[2];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new FloatFormField( "", null, TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, float data )
    {
        try
        {
            super.stream.seek( index * Float.BYTES );
            super.stream.writeFloat( data );
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
        for( FloatFormField field : fields )
        {
            field.setValue( stream.readFloat() );
        }
    }
}
