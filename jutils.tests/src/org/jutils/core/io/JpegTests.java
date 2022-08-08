package org.jutils.core.io;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

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
        ByteArrayStream imgStream = new ByteArrayStream( numBytes );

        try
        {
            int bytesWritten = -1;

            FileStream fs = new FileStream( new File( "blah.jpg" ) );

            IStream outStream;

            outStream = fs;
            outStream = imgStream;

            outStream.seek( 0L );

            try( StreamOutput output = new StreamOutput( outStream ) )
            {
                if( !ImageIO.write( image, "JPEG", output ) )
                {
                    LogUtils.printError( "NOPE!!!!" );
                }

                bytesWritten = ( int )outStream.getPosition();
            }

            fs.close();

            fs = new FileStream( new File( "blah.jpg" ) );

            imgStream.seek( 0L );
            LogUtils.printDebug( "Bytes written: %d", bytesWritten );

            try( ByteArrayStream bas = new ByteArrayStream(
                imgStream.getBuffer(), bytesWritten, 0, false ) )
            {
                IStream inStream;

                inStream = fs;
                inStream = bas;

                inStream.seek( 0L );

                try( StreamInput input = new StreamInput( inStream ) )
                {
                    LogUtils.printDebug( "Reading Image: %d",
                        input.available() );
                    BufferedImage bi = ImageIO.read( input );
                    LogUtils.printDebug( "Read Image" );

                    if( bi == null )
                    {
                        LogUtils.printWarning( "Image was read as null" );
                    }
                }
            }
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }
}
