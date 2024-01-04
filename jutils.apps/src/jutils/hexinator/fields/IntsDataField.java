package jutils.hexinator.fields;

import java.io.IOException;

import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.ui.fields.IntegerFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IntsDataField extends DefaultDataField
{
    /**  */
    private final IntegerFormField [] fields;

    /***************************************************************************
     * 
     **************************************************************************/
    public IntsDataField()
    {
        this.fields = new IntegerFormField[2];

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new IntegerFormField( "" );
            fields[i].getTextField().setColumns( TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );
        }
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, int data )
    {
        try
        {
            super.stream.seek( index * Integer.BYTES );
            super.stream.writeInt( data );
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
        for( IntegerFormField field : fields )
        {
            field.setValue( stream.readInt() );
        }
    }
}
