package jutils.core.pcap.blocks;

import java.io.EOFException;
import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataStream;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UnknownBlock extends IBlock
{
    /**  */
    public byte [] body;

    /***************************************************************************
     * @param id
     * @param length
     **************************************************************************/
    public UnknownBlock( BlockType id, int length )
    {
        super( id );

        this.length = length;

        this.body = new byte[0];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class UnknownBlockSerializer
        implements IBlockBodySerializer
    {
        /**
         * {@inheritDoc}
         * @throws IOException
         * @throws EOFException
         */
        @Override
        public IBlock read( IDataStream stream, int id, int length )
            throws IOException
        {
            BlockType bt = BlockType.fromValue( id );
            UnknownBlock block = new UnknownBlock( bt, length );

            block.body = new byte[length - 12];

            stream.readFully( block.body );

            return block;
        }
    }
}
