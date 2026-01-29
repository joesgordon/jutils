package jutils.core.pcapng.blocks;

import java.io.EOFException;
import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataStream;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

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
        printer.printFieldValues( "Block Data", body );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class UnknownBlockSerializer
        implements IBlockBodySerializer<UnknownBlock>
    {
        /**
         * {@inheritDoc}
         * @throws IOException
         * @throws EOFException
         */
        @Override
        public UnknownBlock read( IDataStream stream, BlockType type,
            int length ) throws IOException
        {
            UnknownBlock block = new UnknownBlock( type, length );

            block.body = new byte[length - 12];

            stream.readFully( block.body );

            return block;
        }
    }
}
