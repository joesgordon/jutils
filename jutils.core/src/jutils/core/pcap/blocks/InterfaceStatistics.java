package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InterfaceStatistics extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public InterfaceStatistics()
    {
        super( BlockType.INTERFACE_STATS );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
