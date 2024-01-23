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
    public int bitDepth;
    /**  */
    public boolean packed;
    /**  */
    public IndexingType indexing;
    /**  */
    public ChannelPlacement channels;

    /***************************************************************************
     * 
     **************************************************************************/
    public RasterConfig()
    {
        this.width = 256;
        this.height = 256;
        this.channelCount = 1;
        this.bitDepth = 8;
        this.packed = false;
        this.indexing = IndexingType.ROW_MAJOR;
        this.channels = ChannelPlacement.INTERLEAVED;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public RasterConfig( RasterConfig config )
    {
        this.width = config.width;
        this.height = config.height;
        this.channelCount = config.channelCount;
        this.bitDepth = config.bitDepth;
        this.packed = config.packed;
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
    private int getBitsPerPixel()
    {
        return bitDepth * channelCount;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private int getBytesPerPixel()
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMaxPixelValue()
    {
        return ( int )( ( 1L << bitDepth ) - 1 );
    }
}
