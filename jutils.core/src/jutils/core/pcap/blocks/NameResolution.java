package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

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
