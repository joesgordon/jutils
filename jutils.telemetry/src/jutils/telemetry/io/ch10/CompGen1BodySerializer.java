package jutils.telemetry.io.ch10;

import java.io.IOException;
import java.nio.charset.Charset;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.core.io.IOUtils;
import jutils.telemetry.data.ch10.CompGen1Body;
import jutils.telemetry.data.ch10.CompGen1Word;
import jutils.telemetry.data.ch10.PacketHeader;
import jutils.telemetry.data.ch10.Rcc106Version;
import jutils.telemetry.data.ch10.TmatsFormat;
import jutils.telemetry.io.ch09.ascii.TmatsParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompGen1BodySerializer implements IBodySerializer<CompGen1Body>
{
    /**  */
    private static final Charset ASCII = IOUtils.getAsciiEncoding();

    /**  */
    private final TmatsParser tmatsParser;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompGen1BodySerializer()
    {
        this.tmatsParser = new TmatsParser();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public CompGen1Body read( IDataStream stream, PacketHeader header )
        throws IOException, ValidationException
    {
        CompGen1Body body = new CompGen1Body();

        int word = stream.readInt();

        body.format = TmatsFormat.fromValue(
            CompGen1Word.FORMAT.getField( word ) );
        body.setupRecordConfigChanged = CompGen1Word.SETUP_RECORD_CONFIG_CHANGE.getFlag(
            word );
        body.rccVersion = Rcc106Version.fromValue(
            CompGen1Word.RCC_VERSION.getField( word ) );
        body.reserved = CompGen1Word.RESERVED.getField( word );

        int len = header.dataLength - 4;
        byte [] tmatsData = new byte[len];

        stream.readFully( tmatsData );

        body.setup = new String( tmatsData, ASCII );
        body.tmats = tmatsParser.parse( body.setup );

        return body;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( CompGen1Body body, IDataStream stream,
        PacketHeader header ) throws IOException
    {
        // TODO Auto-generated method stub

    }

}
