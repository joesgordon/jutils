package jutils.core.pcapng.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JComponent;

import jutils.core.data.DataItemPair;
import jutils.core.data.DataItemPair.DataItemPairSerializer;
import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.pcapng.IBlock;
import jutils.core.pcapng.io.BlockSerializer;
import jutils.core.ui.DataItemPairView;
import jutils.core.ui.model.IDataView;
import jutils.core.ui.model.ITableConfig;
import jutils.core.ui.model.IView;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcapngView implements IView<JComponent>
{
    /**  */
    private final ReferenceStreamView<DataItemPair<IBlock>> streamView;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcapngView()
    {
        ITableConfig<DataItemPair<IBlock>> tableConfig = new BlockTableConfig();
        IDataView<DataItemPair<IBlock>> itemView = new DataItemPairView<>(
            new BlockView() );
        DataItemPairSerializer<IBlock> serializer = new DataItemPairSerializer<IBlock>(
            new BlockSerializer() );

        this.streamView = new ReferenceStreamView<>( tableConfig, itemView,
            serializer );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return streamView.getView();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Iterator<DataItemPair<IBlock>> getItems()
    {
        return streamView.getItems();
    }

    /***************************************************************************
     * @param file
     * @throws IOException
     **************************************************************************/
    public void openFile( File file ) throws IOException
    {
        @SuppressWarnings( "resource")
        FileStream fileStream = new FileStream( file, true );
        @SuppressWarnings( "resource")
        DataStream dataStream = new DataStream( fileStream,
            ByteOrdering.LITTLE_ENDIAN );

        streamView.setStream( file, dataStream );
    }
}
