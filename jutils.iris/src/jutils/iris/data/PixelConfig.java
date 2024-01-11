package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PixelConfig
{
    /**  */
    public PixelFormat format;
    /**  */
    public int channelCount;
    /**  */
    public int bitDepth;

    /***************************************************************************
     * 
     **************************************************************************/
    public PixelConfig()
    {
        this.format = PixelFormat.MONOCHROME;
        this.channelCount = format.channelCount;
        this.bitDepth = 8;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getBitsPerPixel()
    {
        return bitDepth * channelCount;
    }
}
