package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CustomLocked extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public CustomLocked()
    {
        super( BlockType.CUSTOM_LOCKED );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
