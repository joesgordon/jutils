package jutils.core.pcap.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jutils.core.ValidationException;
import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.LogUtils;
import jutils.core.pcap.BlockType;
import jutils.core.pcap.IBlock;
import jutils.core.pcap.IBlock.IBlockBodySerializer;
import jutils.core.pcap.blocks.EnhancedPacket.EnhancedPacketSerializer;
import jutils.core.pcap.blocks.UnknownBlock.UnknownBlockSerializer;

/*******************************************************************************
 * Defines the methods of reading and writing PCAP Next Generation Blocks.
 ******************************************************************************/
public class BlockSerializer implements IDataSerializer<IBlock>
{
    /**  */
    private final Map<BlockType, IBlockBodySerializer> bodySerializers;
    /**  */
    private final UnknownBlockSerializer unknownSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public BlockSerializer()
    {
        this.bodySerializers = new HashMap<>();
        this.unknownSerializer = new UnknownBlockSerializer();

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
        int id = stream.readInt();
        int length = stream.readInt();
        BlockType bt = BlockType.fromValue( id );

        IBlockBodySerializer serializer = bodySerializers.get( bt );

        if( serializer == null )
        {
            serializer = unknownSerializer;
        }

        IBlock block = serializer.read( stream, id, length );

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

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        if( args.length == 1 )
        {
            File f = new File( args[0] );

            try( FileStream fs = new FileStream( f );
                 DataStream ds = new DataStream( fs ) )
            {
                BlockSerializer bs = new BlockSerializer();

                while( ds.getAvailable() > 0 )
                {
                    IBlock block = bs.read( ds );

                    LogUtils.printDebug( "Read %X", block.id );
                }
            }
            catch( IOException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            catch( ValidationException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }
}