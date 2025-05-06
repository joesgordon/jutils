package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.telemetry.ch10.SecondaryHeader;
import jutils.telemetry.ch10.SecondaryHeaderTimeFormat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SecondaryHeaderSerializer
    implements IDataSerializer<SecondaryHeader>
{
    /**  */
    private final SecondaryTimeSerializer timeSerializer;

    /**  */
    private SecondaryHeaderTimeFormat format;

    /***************************************************************************
     * 
     **************************************************************************/
    public SecondaryHeaderSerializer()
    {
        this( SecondaryHeaderTimeFormat.CHAPTER4 );
    }

    /***************************************************************************
     * @param format
     **************************************************************************/
    public SecondaryHeaderSerializer( SecondaryHeaderTimeFormat format )
    {
        this.timeSerializer = new SecondaryTimeSerializer();

        this.format = format;
    }

    /***************************************************************************
     * @param format
     **************************************************************************/
    public void setFormat( SecondaryHeaderTimeFormat format )
    {
        this.format = format;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public SecondaryHeaderTimeFormat getFormat()
    {
        return format;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public SecondaryHeader read( IDataStream stream )
        throws IOException, ValidationException
    {
        SecondaryHeader header = new SecondaryHeader();

        read( header, stream );

        return header;
    }

    /***************************************************************************
     * @param header
     * @param stream
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public void read( SecondaryHeader header, IDataStream stream )
        throws IOException, ValidationException
    {
        header.time = timeSerializer.read( stream, format );
        header.reserved = stream.readShort();
        header.checksum = stream.readShort();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( SecondaryHeader header, IDataStream stream )
        throws IOException
    {
        timeSerializer.write( header.time, stream, format );
        stream.writeShort( header.reserved );
        stream.writeShort( header.checksum );
    }
}
