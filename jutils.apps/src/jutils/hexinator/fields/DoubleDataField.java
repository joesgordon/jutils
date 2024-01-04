package jutils.hexinator.fields;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import jutils.core.io.DataStream;
import jutils.core.ui.event.RightClickListener;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.fields.DoubleFormField;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DoubleDataField extends DefaultDataField
{
    /**  */
    private final DoubleFormField [] fields;
    /**  */
    private final JPopupMenu presets;

    /***************************************************************************
     * 
     **************************************************************************/
    public DoubleDataField()
    {
        this.fields = new DoubleFormField[1];
        this.presets = createPopup();

        for( int i = 0; i < fields.length; i++ )
        {
            int index = i;

            fields[i] = new DoubleFormField( "", null, TEXT_COLS );
            fields[i].setUpdater( ( d ) -> update( index, d ) );

            IUpdater<MouseEvent> rcu = ( e ) -> presets.show(
                fields[index].getView(), e.getX(), e.getY() );
            RightClickListener ml = new RightClickListener( rcu );

            fields[i].getTextField().addMouseListener( ml );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPopupMenu createPopup()
    {
        JPopupMenu popup = new JPopupMenu();

        addPreset( popup, "Pi", Math.PI );
        addPreset( popup, "E", Math.E );
        addPreset( popup, "-Infinity", Double.NEGATIVE_INFINITY );
        addPreset( popup, "Min-value", Double.MIN_VALUE );
        addPreset( popup, "Min-normal", Double.MIN_NORMAL );
        addPreset( popup, "NaN", Double.NaN );
        addPreset( popup, "Max-value", Double.MAX_VALUE );
        addPreset( popup, "+Infinity", Double.POSITIVE_INFINITY );

        return popup;
    }

    /***************************************************************************
     * @param popup
     * @param text
     * @param value
     **************************************************************************/
    private void addPreset( JPopupMenu popup, String text, double value )
    {
        JMenuItem item = new JMenuItem( text );
        ActionListener listener = ( e ) -> handleValueSelected( value );
        item.addActionListener( listener );
        popup.add( item );
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void handleValueSelected( double value )
    {
        try
        {
            stream.seek( 0L );
            stream.writeDouble( value );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
        setData( data );
        super.updater.update( data );
    }

    /***************************************************************************
     * @param index
     * @param data
     **************************************************************************/
    private void update( int index, double data )
    {
        try
        {
            super.stream.seek( index * Double.BYTES );
            super.stream.writeDouble( data );
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
        for( DoubleFormField field : fields )
        {
            field.setValue( stream.readDouble() );
        }
    }
}
