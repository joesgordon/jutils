package jutils.hexulator.fields;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jutils.core.Utils;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.ui.event.updater.IUpdater;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class DefaultDataField implements IDataField
{
    /**  */
    protected static final int TEXT_COLS = 6;

    /**  */
    protected final byte [] data;
    /**  */
    private final ByteArrayStream byteStream;
    /**  */
    protected final DataStream stream;

    /**  */
    protected IUpdater<byte []> updater;

    /***************************************************************************
     * 
     **************************************************************************/
    public DefaultDataField()
    {
        this.data = new byte[8];
        this.byteStream = new ByteArrayStream( data, data.length, 0, false );
        this.stream = new DataStream( byteStream, ByteOrdering.BIG_ENDIAN );
        this.updater = ( d ) -> {
        };
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final void setData( byte [] data )
    {
        Utils.byteArrayCopy( data, 0, this.data, 0, data.length );

        try
        {
            stream.seek( 0L );
            setData( stream );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

    }

    /***************************************************************************
     * @param stream
     * @throws IOException
     **************************************************************************/
    protected abstract void setData( DataStream stream ) throws IOException;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setOrder( ByteOrdering order )
    {
        this.stream.setOrder( order );
        setData( data );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final void setUpdater( IUpdater<byte []> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * @param <T>
     * @param fields
     * @param name
     * @param panel
     * @param row
     **************************************************************************/
    protected static final <T extends IView<?>> void addTo( T [] fields,
        String name, JPanel panel, int row )
    {
        GridBagConstraints constraints;
        int xs = 8 / fields.length;

        constraints = new GridBagConstraints( 0, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE,
            new Insets( 2, 0, 0, 2 ), 0, 0 );
        panel.add( new JLabel( name ), constraints );

        for( int i = 0, c = 1; i < fields.length; i++, c += xs )
        {
            T field = fields[i];
            constraints = new GridBagConstraints( c, row, xs, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 0, 0 ), 0, 0 );
            panel.add( field.getView(), constraints );
        }
    }
}
