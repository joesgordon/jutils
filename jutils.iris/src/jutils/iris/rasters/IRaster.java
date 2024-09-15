package jutils.iris.rasters;

import java.awt.Point;

import jutils.core.utils.ByteOrdering;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * Defines a set of pixels on a 2-dimensional plane.
 ******************************************************************************/
public interface IRaster
{
    /**
     * Indicates there is no data present; 0d-9223372036854775808,
     * 0x8000000000000000.
     */
    public static final long PIXEL_MISSING = Long.MIN_VALUE;
    /** The maximum number of channels supported. */
    public static final int MAX_CHANNELS = 4;

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getWidth();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getHeight();

    /***************************************************************************
     * Gets the number of pixels in this image.
     * @return the number of pixels in this image.
     **************************************************************************/
    public int getPixelCount();

    /***************************************************************************
     * @return
     **************************************************************************/
    public IndexingType getIndexingMethod();

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getChannelCount();

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChannelStore getChannelPlacement();

    /***************************************************************************
     * @param index 0-relative position of the requested channel.
     * @return
     **************************************************************************/
    public IChannel getChannel( int index );

    /***************************************************************************
     * Gets the value of the pixel at the provided index.
     * @param index the index of the pixel.
     * @return the value of the pixel.
     * @throws IllegalArgumentException if the index < 0 or greater than the
     * number of pixels minus 1.
     * @see IRaster#getPixelCount()
     **************************************************************************/
    public long getPixel( int index ) throws IllegalArgumentException;

    /***************************************************************************
     * @param x the 0-relative column of the pixel.
     * @param y the 0-relative row of the pixel.
     * @return
     **************************************************************************/
    public int getPixelIndex( int x, int y );

    /***************************************************************************
     * @param index
     * @param location
     **************************************************************************/
    public void getPixelLocation( int index, Point location );

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
     * @throws IllegalArgumentException
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
}
