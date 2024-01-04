package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PixelConfig
{
    /**  */
    public PixelFormat format;
    /**  */
    public int formatType;
    /**  */
    public int bitDepth;
    /**  */
    public boolean packed;

    /***************************************************************************
     * 
     **************************************************************************/
    public PixelConfig()
    {
        this.format = PixelFormat.MONOCHROME;
        this.formatType = MonochromeType.DEFAULT.value;
        this.bitDepth = 8;
        this.packed = false;
    }
}
