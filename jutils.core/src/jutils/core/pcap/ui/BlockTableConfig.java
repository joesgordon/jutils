package jutils.core.pcap.ui;

import jutils.core.data.DataItemPair;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;
import jutils.core.ui.model.DefaultTableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockTableConfig
    extends DefaultTableItemsConfig<DataItemPair<IBlock>>
{
    /***************************************************************************
     * 
     **************************************************************************/
    public BlockTableConfig()
    {
        super.addCol( "Type", String.class,
            ( d ) -> BlockType.fromValue( d.item.id ).getDescription() );
        super.addCol( "Size", Integer.class, ( d ) -> d.data.length );
        super.addCol( "Length", Integer.class, ( d ) -> d.item.length );
    }
}
