package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RasterConfig
{
    /**  */
    public int width;
    /**  */
    public int height;
    /**  */
    public int channelCount;
    /**  */
    public ChannelConfig [] channels;
    /**  */
    public boolean packed;
    /**  */
    public IndexingType indexing;
    /**  */
    public ChannelPlacement channelLoc;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterConfig()
    {
        this.width = 512;
        this.height = 512;
        this.channelCount = 1;
        this.channels = new ChannelConfig[4];
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
     * @return
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
        return channels[0].bitDepth * channelCount;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMaxPixelValue()
    {
        return channels[0].getMaxPixelValue();
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
