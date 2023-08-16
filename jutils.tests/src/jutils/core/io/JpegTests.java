package jutils.core.io;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JpegTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void rwTest()
    {
        Dimension size = new Dimension( 400, 400 );
        int numBytes = size.width * size.height;
        BufferedImage image = new BufferedImage( 240, 240,
            BufferedImage.TYPE_INT_RGB );

        byte [] imgBytes = null;

        try( ByteArrayStream imgStream = new ByteArrayStream( numBytes );
             StreamOutput output = new StreamOutput( imgStream ) )
        {
            imgStream.seek( 0L );

            if( !ImageIO.write( image, "JPEG", output ) )
            {
                fail( "Unable to write image" );
            }

            imgBytes = imgStream.toByteArray();
        }
        catch( IOException ex )
        {
            fail( ex.getMessage() );
            throw new RuntimeException( "Fail didn't stop processing" );
        }

        // LogUtils.printDebug( "Bytes written: %d", imgBytes.length );

        try( ByteArrayStream imgStream = new ByteArrayStream( imgBytes,
            imgBytes.length, 0, false );
             StreamInput input = new StreamInput( imgStream ) )
        {
            // LogUtils.printDebug( "Reading Image: %d", input.available() );
            BufferedImage bi = ImageIO.read( input );
            // LogUtils.printDebug( "Read Image" );

            if( bi == null )
            {
                fail( "Image was read as null" );
            }
        }
        catch( IOException ex )
        {
            fail( ex.getMessage() );
        }
    }
}
