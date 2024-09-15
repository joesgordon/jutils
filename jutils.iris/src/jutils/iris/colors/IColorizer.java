package jutils.iris.colors;

import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * Defines a method to represent raw values as a color image.
 ******************************************************************************/
public interface IColorizer
{
    /**  */
    public static final int TRANSPARENT_ARGB = 0xFF000000;

    /***************************************************************************
     * @param raster the raw values to be colorized.
     * @param pixels the argb pixels whose color represents the raster; must be
     * of length greater than or equal to {@link RasterConfig#getPixelCount()}.
     * @throws ArrayIndexOutOfBoundsException if pixels is not of sufficient
     * size.
     **************************************************************************/
    public void colorize( IRaster raster, int [] pixels );
}
