package jutils.iris.rasters;

import java.io.IOException;

import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.IDataStream;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.RasterConfig;

/*******************************************************************************
 * Defines a set of pixels on a 2-dimensional plane.
 ******************************************************************************/
public interface IRaster
{
    /** Indicates there is no data present; 0d -2147483648, 0x80000000. */
    public static final int CHANNEL_MISSING = Integer.MIN_VALUE;
    /** Indicates there is no data present; 0d -2147483648, 0x80000000. */
    public static final long PIXEL_MISSING = CHANNEL_MISSING;

    /***************************************************************************
     * @return a copy of the image configuration.
     **************************************************************************/
    public RasterConfig getConfig();

    /***************************************************************************
     * @param p
     * @return
     **************************************************************************/
    public long getPixel( int p );

    /***************************************************************************
     * @param p
     * @param value
     **************************************************************************/
    public void setPixel( int p, long value );

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public long getPixel( int x, int y );

    /***************************************************************************
     * @param x
     * @param y
     * @param value
     **************************************************************************/
    public void setPixel( int x, int y, long value );

    /***************************************************************************
     * @param p
     * @param c
     * @return
     **************************************************************************/
    public int getChannel( int p, int c );

    /***************************************************************************
     * @param p
     * @param c
     * @param value
     **************************************************************************/
    public void setChannel( int p, int c, int value );

    /***************************************************************************
     * @param x
     * @param y
     * @param c
     * @return
     **************************************************************************/
    public int getChannel( int x, int y, int c );

    /***************************************************************************
     * @param x
     * @param y
     * @param c
     * @param value
     **************************************************************************/
    public void setChannel( int x, int y, int c, int value );

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte [] getPixelData();

    /***************************************************************************
     * @param pixels
     **************************************************************************/
    public void setPixelData( byte [] pixels );

    /***************************************************************************
     * @param pixels
     * @param order
     **************************************************************************/
    public void readPixels( ByteOrdering order );

    /***************************************************************************
     * @param pixels
     * @param data
     * @param order
     **************************************************************************/
    public static void readPixels16( short [] pixels, byte [] data,
        ByteOrdering order )
    {
        try( ByteArrayStream bas = new ByteArrayStream( data, data.length, 0,
            false ); IDataStream stream = new DataStream( bas, order ) )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                pixels[i] = stream.readShort();
            }
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException(
                "Unable to copy bytes into raster", ex );
        }
    }

    /***************************************************************************
     * @param pixels
     * @param data
     * @param order
     **************************************************************************/
    public static void readPixels16( int [] pixels, byte [] data,
        ByteOrdering order )
    {
        try( ByteArrayStream bas = new ByteArrayStream( data, data.length, 0,
            false ); IDataStream stream = new DataStream( bas, order ) )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                pixels[i] = stream.readShort();
            }
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException(
                "Unable to copy bytes into raster", ex );
        }
    }

    /***************************************************************************
     * @param pixels
     * @param data
     * @param order
     **************************************************************************/
    public static void readPixels32( int [] pixels, byte [] data,
        ByteOrdering order )
    {
        try( ByteArrayStream bas = new ByteArrayStream( data, data.length, 0,
            false ); IDataStream stream = new DataStream( bas, order ) )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                pixels[i] = stream.readInt();
            }
        }
        catch( IOException ex )
        {
            throw new IllegalArgumentException(
                "Unable to copy bytes into raster", ex );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public default int getWidth()
    {
        return getConfig().width;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public default int getHeight()
    {
        return getConfig().height;
    }
}
