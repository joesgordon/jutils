package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

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
