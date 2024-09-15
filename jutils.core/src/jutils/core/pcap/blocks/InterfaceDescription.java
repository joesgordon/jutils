package jutils.core.pcap.blocks;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataStream;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * An Interface Description Block (IDB) is the container for information
 * describing an interface on which packet data is captured.
 ******************************************************************************/
public class InterfaceDescription extends IBlock
{
    /**  */
    public LinkType linkType;
    /**  */
    public short reserved;
    /** the maximum number of octets captured from each packet. */
    public int snapLength;

    /***************************************************************************
     * 
     **************************************************************************/
    protected InterfaceDescription()
    {
        super( BlockType.INTERFACE_DESC );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Link Type", linkType.getDescription() );
        printer.printHexField( "Reserved", reserved );
        printer.printField( "Snap Length", snapLength );

        printer.printIterableField( "Options", options, options.size(), 1,
            ( o ) -> String.format( "%d, %d, %s", o.type, o.length,
                HexUtils.toHexString( o.value, " " ) ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class InterfaceDescriptionSerializer
        implements IBlockBodySerializer<InterfaceDescription>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public InterfaceDescription read( IDataStream stream, BlockType type,
            int length ) throws IOException
        {
            InterfaceDescription block = new InterfaceDescription();

            block.length = length;

            block.linkType = LinkType.fromValue( stream.readShort() & 0xFFFF );
            block.reserved = stream.readShort();
            block.snapLength = stream.readInt();

            int optionsLen = block.length - 5 * 4;

            readOptions( stream, block, optionsLen );

            return block;
        }
    }
}
