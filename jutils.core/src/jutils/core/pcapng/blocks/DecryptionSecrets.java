package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

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
