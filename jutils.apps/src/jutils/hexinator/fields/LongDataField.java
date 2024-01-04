package jutils.hexinator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.LongFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LongDataField extends DefaultDataField
{
    /**  */
    private final LongFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public LongDataField()
    {
        this.fields = new LongFormField[1];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new LongFormField( "" );
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
        for( LongFormField field : fields )
        {
            field.setValue( stream.readLong() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Long getValue()
    {
        return fields[0].getValue();
    }
}
