package jutils.core.pcapng.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.LogUtils;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;
import jutils.core.pcapng.IBlock.IBlockBodySerializer;
import jutils.core.pcapng.blocks.EnhancedPacket.EnhancedPacketSerializer;
import jutils.core.pcapng.blocks.InterfaceDescription.InterfaceDescriptionSerializer;
import jutils.core.pcapng.blocks.UnknownBlock.UnknownBlockSerializer;

/*******************************************************************************
 * Defines the methods of reading and writing PCAP Next Generation Blocks.
 ******************************************************************************/
public class BlockSerializer implements IDataSerializer<IBlock>
{
    /**  */
    private final Map<BlockType, IBlockBodySerializer<?>> bodySerializers;
    /**  */
    private final UnknownBlockSerializer unknownSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlockSerializer()
    {
        this.bodySerializers = new HashMap<>();
        this.unknownSerializer = new UnknownBlockSerializer();

        bodySerializers.put( BlockType.INTERFACE_DESC,
            new InterfaceDescriptionSerializer() );
        bodySerializers.put( BlockType.ENHANCED_PACKET,
            new EnhancedPacketSerializer() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IBlock read( IDataStream stream )
        throws IOException, ValidationException
    {
        long pos = stream.getPosition();
        int id = stream.readInt();
        int length = stream.readInt();
        BlockType type = BlockType.fromValue( id );

        LogUtils.printDebug( "Got a %s packet (0x%08X) of length %d at %d",
            type.name, id, length, pos );

        if( BlockType.UNKNOWN == type )
        {
            LogUtils.printWarning( "Unknown ID: 0x%08X", id );
        }

        IBlockBodySerializer<?> serializer = bodySerializers.get( type );

        if( serializer == null )
        {
            serializer = unknownSerializer;
        }

        IBlock block = serializer.read( stream, type, length );

        int length2 = stream.readInt();

        if( length2 != length )
        {
            String err = String.format(
                "Length at beginning does not match length at end, %d != %d",
                length, length2 );
            throw new ValidationException( err );
        }

        return block;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( IBlock data, IDataStream stream ) throws IOException
    {
        throw new IllegalStateException( "Not implemented" );
        // TODO implement function
    }
}
