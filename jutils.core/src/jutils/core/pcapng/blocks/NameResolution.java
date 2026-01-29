package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NameResolution extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public NameResolution()
    {
        super( BlockType.NAME_RESOLUTION );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
