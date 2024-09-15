package jutils.core.io;

import java.io.IOException;
import java.nio.charset.Charset;

/*******************************************************************************
 *
 ******************************************************************************/
public class LengthStringSerializer implements IDataSerializer<String>
{
    /**  */
    private final Charset encoding;

    /***************************************************************************
     * 
     **************************************************************************/
    public LengthStringSerializer()
    {
        this( IOUtils.get8BitEncoding() );
    }

    /***************************************************************************
     * @param encoding
     **************************************************************************/
    public LengthStringSerializer( Charset encoding )
    {
        this.encoding = encoding;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String read( IDataStream stream ) throws IOException
    {
        int strLen = stream.readInt();
        byte [] strBytes = new byte[strLen];
        stream.readFully( strBytes );
        return new String( strBytes, encoding );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( String str, IDataStream stream ) throws IOException
    {
        byte [] bytes = str.getBytes( encoding );
        stream.writeInt( bytes.length );
        stream.write( bytes );
    }
}
