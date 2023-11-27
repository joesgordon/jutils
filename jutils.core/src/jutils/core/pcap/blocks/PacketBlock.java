package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

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
