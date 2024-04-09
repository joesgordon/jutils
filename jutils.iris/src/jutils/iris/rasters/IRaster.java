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
     * Gets a copy of the configuration that this raster was created with.
     * @return a copy of the image configuration.
     **************************************************************************/
    public RasterConfig getConfig();

    /***************************************************************************
     * Gets the value of the pixel at the provided index.
     * @param index the index of the pixel.
     * @return the value of the pixel.
     * @throws ArrayIndexOutOfBoundsException if the index < 0 or greater than
     * the number of pixels minus 1.
     * @see IRaster#getPixelCount()
     **************************************************************************/
    public long getPixel( int index ) throws ArrayIndexOutOfBoundsException;

    /***************************************************************************
     * Sets the value of the pixel at the provided index.
     * @param index the index of the pixel to be set.
     * @param value the value of the pixel to be set.
     **************************************************************************/
    public void setPixel( int index, long value );

    /***************************************************************************
     * Gets the value of the pixel at the provided coordinate
     * @param x the 0-relative column of the pixel.
     * @param y the 0-relative row of the pixel.
     * @return the value of the pixel.
     **************************************************************************/
    public long getPixelAt( int x, int y );

    /***************************************************************************
     * Sets the value of the pixel at the provided coordinate.
     * @param x the 0-relative column of the pixel.
     * @param y the 0-relative row of the pixel.
     * @param value the value of the pixel to be set.
     **************************************************************************/
    public void setPixelAt( int x, int y, long value );

    /***************************************************************************
     * Gets the value of the channel at the provided pixel and channel indexes.
     * @param pixelIndex the index of the pixel.
     * @param channelIndex the index of the channel.
     * @return the value of the channel.
     **************************************************************************/
    public int getChannel( int pixelIndex, int channelIndex );

    /***************************************************************************
     * Sets the value of the channel at the provided pixel and channel indexes.
     * @param pixelIndex the index of the pixel.
     * @param channelIndex the index of the channel.
     * @param value the value of the channel.
     **************************************************************************/
    public void setChannel( int pixelIndex, int channelIndex, int value );

    /***************************************************************************
     * Gets the value of the channel at the provided coordinates and channel
     * index.
     * @param x the 0-relative column of the pixel.
     * @param y the 0-relative row of the pixel.
     * @param channelIndex the index of the channel.
     * @return the value of the channel.
     **************************************************************************/
    public int getChannelAt( int x, int y, int channelIndex );

    /***************************************************************************
     * Sets the value of the channel at the provided coordinates and channel
     * index.
     * @param x the 0-relative column of the pixel.
     * @param y the 0-relative row of the pixel.
     * @param channelIndex the index of the channel.
     * @param value the value of the channel.
     **************************************************************************/
    public void setChannelAt( int x, int y, int channelIndex, int value );

    /***************************************************************************
     * Returns the raw buffer of bytes that represents this image.
     * @return the raw image buffer.
     **************************************************************************/
    public byte [] getBufferData();

    /***************************************************************************
     * Sets the raw buffer of bytes that represents this image.
     * @param buffer the raw image buffer.
     * @param order the byte order of the pixels in the buffer.
     **************************************************************************/
    public void setBufferData( byte [] buffer, ByteOrdering order );

    /***************************************************************************
     * Gets the number of pixels in this image.
     * @return the number of pixels in this image.
     **************************************************************************/
    public default int getPixelCount()
    {
        return getConfig().getPixelCount();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public default int getChannelCount()
    {
        return getConfig().channelCount;
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
}
