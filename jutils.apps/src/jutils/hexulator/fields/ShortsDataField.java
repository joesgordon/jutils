package jutils.hexulator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.ShortFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShortsDataField extends DefaultDataField
{
    /**  */
    private final ShortFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public ShortsDataField()
    {
        this.fields = new ShortFormField[4];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new ShortFormField( "" );
            fields[i].getTextField().setColumns( TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, short data )
    {
        try
        {
            super.stream.seek( index * Short.BYTES );
            super.stream.writeShort( data );
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
        for( ShortFormField field : fields )
        {
            field.setValue( stream.readShort() );
        }
    }
}
