package jutils.core.pcapng.ui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jutils.core.io.DataStream;
import jutils.core.io.IDataSerializer;
import jutils.core.io.ReferenceItemStream;
import jutils.core.io.ReferenceStream;
import jutils.core.net.NetMessage;
import jutils.core.ui.PaginatedTableView;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.model.IView;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ReferenceStreamView<T> implements IView<JComponent>
{
    /**  */
    private final JComponent view;

    /** The paginated table of {@link NetMessage}s. */
    private final PaginatedTableView<T> table;

    /**  */
    private final IDataSerializer<T> serializer;

    /***************************************************************************
     * @param tableConfig
     * @param itemView
     * @param serializer
     * @param serializer
     **************************************************************************/
    public ReferenceStreamView( ITableConfig<T> tableConfig,
        IDataView<T> itemView, IDataSerializer<T> serializer )
    {
        this.serializer = serializer;

        ReferenceStream<T> refStream = null;
        try
        {
            @SuppressWarnings( "resource")
            ReferenceStream<T> refs = new ReferenceStream<T>( serializer );
            refStream = refs;
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

        ReferenceItemStream<T> itemsStream = new ReferenceItemStream<>(
            refStream );

        this.table = new PaginatedTableView<>( tableConfig, itemsStream,
            itemView );
        this.view = createView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JPanel panel = new JPanel( new BorderLayout() );

        panel.add( table.getView(), BorderLayout.CENTER );

        return panel;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Iterator<T> getItems()
    {
        return table.getItemIterator();
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void setFile( File file ) throws IOException
    {
        @SuppressWarnings( "resource")
        ReferenceStream<T> refStream = new ReferenceStream<>( serializer,
            file );

        table.setItems( new ReferenceItemStream<>( refStream ) );
    }

    /***************************************************************************
     * @param file
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void setStream( File file, DataStream stream ) throws IOException
    {
        @SuppressWarnings( "resource")
        ReferenceStream<T> refStream = new ReferenceStream<>( serializer, file,
            stream );

        table.setItems( new ReferenceItemStream<>( refStream ) );
    }
}
