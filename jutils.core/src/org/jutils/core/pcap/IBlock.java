package org.jutils.core.pcap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jutils.core.ValidationException;
import org.jutils.core.io.DataStream;
import org.jutils.core.io.FileStream;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.parsers.IpAddressParser;
import org.jutils.core.net.EndPoint;
import org.jutils.core.net.IpAddress;
import org.jutils.core.net.NetMessage;
import org.jutils.core.net.NetMessageSerializer;
import org.jutils.core.ui.hex.HexUtils;
import org.jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class IBlock
{
    /**  */
    public int id;
    /**  */
    public int length;
    /**  */
    public int length2;

    /***************************************************************************
     * @param id
     **************************************************************************/
    protected IBlock( BlockType id )
    {
        this.id = id.value;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        IpAddressParser ipParser = new IpAddressParser();
        if( args.length == 3 )
        {
            File pcapFile = new File( args[0] );
            IpAddress localAddress;
            IpAddress remoteAddress;

            try
            {
                localAddress = ipParser.parse( args[1] );
                remoteAddress = ipParser.parse( args[2] );
            }
            catch( ValidationException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
                return;
            }

            File netmsgsFile = IOUtils.replaceExtension( pcapFile, "netmsgs" );

            try
            {
                export( pcapFile, netmsgsFile, localAddress, remoteAddress );
            }
            catch( FileNotFoundException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            catch( IOException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * @param pcapFile
     * @param netmsgsFile
     * @param localIp
     * @param remoteIp
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static void export( File pcapFile, File netmsgsFile,
        IpAddress localIp, IpAddress remoteIp )
        throws FileNotFoundException, IOException
    {
        BlockSerializer blockSerializer = new BlockSerializer();
        NetMessageSerializer netMsgSerializer = new NetMessageSerializer();

        try( FileStream inFs = new FileStream( pcapFile );
             DataStream inDs = new DataStream( inFs,
                 ByteOrdering.LITTLE_ENDIAN );
             FileStream outFs = new FileStream( netmsgsFile );
             DataStream outDs = new DataStream( outFs ) )
        {
            outFs.setLength( 0 );

            EndPoint source = new EndPoint();
            EndPoint destination = new EndPoint();
            int msgCount = 0;
            while( inDs.getAvailable() > 8 )
            {
                // if( msgCount == 701 )
                // {
                // LogUtils.printDebug( "Reading block" );
                // }

                IBlock block = blockSerializer.read( inDs );
                BlockType bt = BlockType.fromValue( block.id );
                String str = "";

                if( bt == BlockType.ENHANCED_PACKET )
                {
                    EnhancedPacket ep = ( EnhancedPacket )block;
                    Protocol proto = ep.ipv4.getProtocol();
                    long pos = outFs.getPosition();

                    msgCount++;

                    if( proto == Protocol.TCP )
                    {
                        source.address.set( ep.ipv4.source );
                        source.port = ep.tcp.sourcePort & 0xFFFF;

                        destination.address.set( ep.ipv4.destination );
                        destination.port = ep.tcp.destinationPort & 0xFFFF;

                        boolean srcLocal = source.address.equals( localIp );
                        boolean srcRemote = source.address.equals( remoteIp );

                        boolean dstLocal = destination.address.equals(
                            localIp );
                        boolean dstRemote = destination.address.equals(
                            remoteIp );

                        boolean srcMatch = srcLocal || srcRemote;
                        boolean dstMatch = dstLocal || dstRemote;

                        if( srcMatch && dstMatch )
                        {
                            EndPoint local = srcLocal ? source : destination;
                            EndPoint remote = srcRemote ? source : destination;
                            NetMessage netMsg = new NetMessage( dstLocal, local,
                                remote, ep.data );

                            netMsgSerializer.write( netMsg, outDs );

                            String bytesStr = HexUtils.toHexString( ep.data,
                                " " );

                            str = String.format( "%s %d [%d @ %d/%X]: %s",
                                proto.name, msgCount, ep.data.length, pos, pos,
                                bytesStr );
                        }
                    }
                    else
                    {
                        str = String.format( "%s %d [%d]", proto.name, msgCount,
                            ep.data.length );
                    }
                }
                else if( bt == BlockType.SECTION_HEADER )
                {
                    LogUtils.printDebug( "Section Header" );
                }
                else if( bt == BlockType.INTERFACE_DESC )
                {
                    LogUtils.printDebug( "Interface Description" );
                }

                String typeStr = bt == null ? "UNK" : bt.name;

                LogUtils.printDebug( "%s (0x%08X) of length %d/%d: %s", typeStr,
                    block.id, block.length, block.length2, str );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IBlockBodySerializer
    {
        /**
         * @param stream
         * @param id
         * @param length
         * @return
         * @throws IOException
         */
        public IBlock read( IDataStream stream, int id, int length )
            throws IOException;
    }
}
