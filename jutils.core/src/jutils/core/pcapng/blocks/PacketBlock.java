package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

/*******************************************************************************
 * The Packet Block is obsolete, and MUST NOT be used in new files. Use the
 * Enhanced Packet Block or Simple Packet Block instead.
 ******************************************************************************/
public class PacketBlock extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public PacketBlock()
    {
        super( BlockType.PACKET );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
