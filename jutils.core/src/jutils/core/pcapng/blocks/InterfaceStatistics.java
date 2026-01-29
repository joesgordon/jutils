package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

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
