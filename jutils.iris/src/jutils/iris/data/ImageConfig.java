package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ImageConfig
{
    /**  */
    public int width;
    /**  */
    public int height;
    /**  */
    public final PixelConfig pixels;
    /**  */
    public IndexingType indexing;

    /***************************************************************************
     * 
     **************************************************************************/
    public ImageConfig()
    {
        this.width = 256;
        this.height = 256;
        this.pixels = new PixelConfig();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPixelCount()
    {
        return width * height;
    }
}
