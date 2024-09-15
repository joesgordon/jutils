package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimplePacket extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public SimplePacket()
    {
        super( BlockType.SIMPLE_PACKET );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
