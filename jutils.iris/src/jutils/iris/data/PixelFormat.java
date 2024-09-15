package jutils.iris.data;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum PixelFormat implements INamedValue
{
    /**  */
    MONOCHROME( 0, "Monochrome", 1 ),
    /**  */
    BAYER_GRBG( 1, "Bayer GRBG", 1 ),
    /**  */
    BAYER_GBRG( 2, "Bayer GBRG", 1 ),
    /**  */
    BAYER_RGGB( 3, "Bayer RGGB", 1 ),
    /**  */
    BAYER_BGGR( 4, "Bayer BGGR", 1 ),
    /**  */
    RGB( 5, "RGB", 3 ),
    /**  */
    ARGB( 6, "ARGB", 4 ),
    /**  */
    YUV( 7, "YUV", 3 ),
    /**  */
    YCBCR( 8, "YCbCr", 3 ),;

    /**  */
    public final int value;
    /**  */
    public final String name;
    /**  */
    public final int channelCount;

    /***************************************************************************
     * @param value
     * @param name
     * @param channels
     **************************************************************************/
    private PixelFormat( int value, String name, int channels )
    {
        this.value = value;
        this.name = name;
        this.channelCount = channels;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }
}
