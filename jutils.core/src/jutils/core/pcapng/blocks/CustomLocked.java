package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

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
