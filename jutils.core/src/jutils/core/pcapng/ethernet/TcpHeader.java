package jutils.core.pcapng.ethernet;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * <a
 * href="https://en.wikipedia.org/wiki/Transmission_Control_Protocol">Wiki</a>
 ******************************************************************************/
public class TcpHeader implements ITcpIpLayer
{
    /**  */
    public short sourcePort;
    /**  */
    public short destinationPort;
    /**  */
    public int sequenceNumber;
    /**  */
    public int acknowledgementNumber;
    /** 4 bits header len, 12 bits flags. */
    public short hdrWord;
    /**  */
    public short window;
    /**  */
    public short checksum;
    /**  */
    public short urgentPointer;
    /**  */
    public byte [] options;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpHeader()
    {
        this.options = new byte[0];
        // TODO Auto-generated constructor stub
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeaderWordCount()
    {
        return ( hdrWord >> 12 ) & 0xF;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getFlags()
    {
        return hdrWord & 0xFFF;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeaderLength()
    {
        return getHeaderWordCount() * 4;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeaderSkip()
    {
        return getHeaderLength() - 10;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Source Port", sourcePort );
        printer.printField( "Destination Port", destinationPort );
        printer.printField( "Sequence #", sequenceNumber );
        printer.printField( "ACK #", acknowledgementNumber );
        printer.printHexField( "Flags", hdrWord );
        printer.printField( "Window Size", window );
        printer.printHexField( "Checksum", checksum );
        printer.printField( "Urgent Pointer", urgentPointer );
        printer.printField( "Options", HexUtils.toHexString( options, " " ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return "TCP";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Class<? extends ITcpIpLayer> getNextLayerType()
    {
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class TcpHeaderSerializer
        implements IDataSerializer<TcpHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public TcpHeader read( IDataStream stream ) throws IOException
        {
            TcpHeader header = new TcpHeader();

            header.sourcePort = stream.readShort();
            header.destinationPort = stream.readShort();
            header.sequenceNumber = stream.readInt();
            header.acknowledgementNumber = stream.readInt();
            header.hdrWord = stream.readShort();
            header.window = stream.readShort();
            header.checksum = stream.readShort();
            header.urgentPointer = stream.readShort();

            int optionsLen = header.getHeaderLength() - 20;

            header.options = new byte[optionsLen];

            stream.readFully( header.options );

            return header;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( TcpHeader data, IDataStream stream )
            throws IOException
        {
            // TODO Auto-generated method stub
        }
    }
}
