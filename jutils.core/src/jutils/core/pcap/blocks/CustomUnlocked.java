package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CustomUnlocked extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public CustomUnlocked()
    {
        super( BlockType.CUSTOM_UNLOCKED );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
