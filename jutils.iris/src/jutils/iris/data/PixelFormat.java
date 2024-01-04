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
    BAYER( 1, "Bayer", 1 ),
    /**  */
    RGB( 2, "RGB", 3 ),
    /**  */
    YUV( 3, "YUV", 3 ),
    /**  */
    YCBCR( 3, "YCbCr", 3 ),;

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
