package jutils.iris.data;

import jutils.core.io.BitsReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterConfig
{
    /** The maximum number of channels supported. */
    public static final int MAX_CHANNELS = 4;

    /** The number of pixels in a row of this raster. */
    public int width;
    /** The number of pixels in a column of this raster. */
    public int height;
    /** The number of channels used in this raster. */
    public int channelCount;
    /** The configuration of the channels in this raster. */
    public ChannelConfig [] channels;
    /** Indicates if the raw raster pixels are packed. */
    public boolean packed;
    /** Defines how pixels are linearly indexed. (Row- vs. col-first) */
    public IndexingType indexing;
    /** Defines where channels are placed in the raw raster. */
    public ChannelPlacement channelLoc;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterConfig()
    {
        this.width = 512;
        this.height = 512;
        this.channelCount = 1;
        this.channels = new ChannelConfig[MAX_CHANNELS];
        this.packed = false;
        this.indexing = IndexingType.ROW_MAJOR;
        this.channelLoc = ChannelPlacement.INTERLEAVED;

        for( int i = 0; i < channels.length; i++ )
        {
            channels[i] = new ChannelConfig();
        }
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public RasterConfig( RasterConfig config )
    {
        this.width = config.width;
        this.height = config.height;
        this.channelCount = config.channelCount;
        this.channels = new ChannelConfig[4];
        this.packed = config.packed;
        this.indexing = config.indexing;
        this.channelLoc = config.channelLoc;

        for( int i = 0; i < channels.length; i++ )
        {
            channels[i] = new ChannelConfig( config.channels[i] );
        }
    }

    /***************************************************************************
     * Returns the number of pixels in this raster equal to {@link #width}
     * {@code x} {@link height}.
     * @return the number of pixels in this raster.
     **************************************************************************/
    public int getPixelCount()
    {
        return width * height;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getBitsPerPixel()
    {
        int bits = 0;

        if( channelLoc == ChannelPlacement.BAYER )
        {
            bits = channels[0].bitDepth;
        }
        else
        {
            for( int i = 0; i < channelCount; i++ )
            {
                bits += channels[i].bitDepth;
            }
        }

        return bits;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMaxPixelValue()
    {
        int bits = getBitsPerPixel();
        int value = ( int )BitsReader.MASKS[bits];
        return value;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getBytesPerPixel()
    {
        return ( getBitsPerPixel() + 7 ) / 8;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPackedSize()
    {
        return ( getPixelCount() * getBitsPerPixel() + 7 ) / 8;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getUnpackedSize()
    {
        return getPixelCount() * getBytesPerPixel();
    }
}
