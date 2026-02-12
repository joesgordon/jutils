package jutils.core.pcap;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.iana.LinkType;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.swap.ByteSwapper;
import jutils.core.utils.ByteOrdering;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileHeader implements ITierPrinter
{
    /**  */
    public PcapMagic magicNumber;
    /**  */
    public int majorVersion;
    /**  */
    public int minorVersion;
    /**  */
    public int reserved1;
    /**  */
    public int reserved2;
    /**  */
    public int snapLength;
    /**  */
    public short fcsWord;
    /**  */
    public LinkType link;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileHeader()
    {
        this.magicNumber = PcapMagic.MAGIC_NANOS;
        this.majorVersion = 2;
        this.minorVersion = 4;
        this.reserved1 = 0;
        this.reserved2 = 0;
        this.snapLength = 0;
        this.fcsWord = 0;
        this.link = LinkType.ETHERNET;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Magic Number", magicNumber.getDescription() );
        printer.printField( "Major Version", majorVersion );
        printer.printField( "Minor Version", minorVersion );
        printer.printField( "Reserved 1", reserved1 );
        printer.printField( "Reserved 2", reserved2 );
        printer.printField( "Snap Length", snapLength );
        printer.printField( "FCS Word", fcsWord );
        printer.printField( "Link", link.getDescription() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class FileHeaderSerializer
        implements IDataSerializer<FileHeader>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public FileHeader read( IDataStream stream )
            throws IOException, ValidationException
        {
            FileHeader header = new FileHeader();

            read( header, stream );

            return header;
        }

        /**
         * @param header
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
        public void read( FileHeader header, IDataStream stream )
            throws IOException, ValidationException
        {
            stream.setOrder( ByteOrdering.BIG_ENDIAN );

            int magicVal = stream.readInt();
            PcapMagic magicNum = PcapMagic.fromValue( magicVal );

            if( magicNum == null )
            {
                magicVal = ByteSwapper.swap( magicVal );
                magicNum = PcapMagic.fromValue( magicVal );

                if( magicNum == null )
                {
                    String err = String.format(
                        "Unknown magic number for PCAP files: 0x%08X",
                        magicVal );
                    throw new ValidationException( err );
                }

                stream.setOrder( ByteOrdering.LITTLE_ENDIAN );
            }

            header.magicNumber = magicNum;
            header.majorVersion = stream.readShort() & 0xFFFF;
            header.minorVersion = stream.readShort() & 0xFFFF;
            header.reserved1 = stream.readInt();
            header.reserved2 = stream.readInt();
            header.snapLength = stream.readInt();
            header.fcsWord = stream.readShort();
            header.link = LinkType.fromValue( stream.readShort() );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( FileHeader header, IDataStream stream )
            throws IOException
        {
            stream.writeInt( header.magicNumber.value );
            stream.writeShort( ( short )header.majorVersion );
            stream.writeShort( ( short )header.minorVersion );
            stream.writeInt( header.reserved1 );
            stream.writeInt( header.reserved2 );
            stream.writeInt( header.snapLength );
            stream.writeShort( header.fcsWord );
            stream.writeShort( ( short )header.link.value );
        }
    }
}
