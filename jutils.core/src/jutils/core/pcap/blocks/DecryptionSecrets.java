package jutils.core.pcap.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DecryptionSecrets extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public DecryptionSecrets()
    {
        super( BlockType.DECRYPTION_SECRETS );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }
}
