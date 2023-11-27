package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * An Interface Description Block (IDB) is the container for information
 * describing an interface on which packet data is captured.
 ******************************************************************************/
public class InterfaceDescription extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    protected InterfaceDescription()
    {
        super( BlockType.INTERFACE_DESC );
        // TODO Auto-generated constructor stub
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
