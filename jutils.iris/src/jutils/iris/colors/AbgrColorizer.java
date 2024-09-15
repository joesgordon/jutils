package jutils.iris.colors;

import jutils.iris.IrisUtils;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AbgrColorizer implements IColorizer
{
    /**  */
    public static final long ALPHA_MASK = IrisUtils.BYTE3_MASK;
    /**  */
    public static final long BLUE_MASK = IrisUtils.BYTE2_MASK;
    /**  */
    public static final long GREEN_MASK = IrisUtils.BYTE1_MASK;
    /**  */
    public static final long RED_MASK = IrisUtils.BYTE0_MASK;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void colorize( IRaster raster, int [] pixels )
    {
        for( int i = 0; i < pixels.length; i++ )
        {
            long abgr = raster.getPixel( i );

            long a = abgr & ALPHA_MASK;
            long b = abgr & BLUE_MASK;
            long g = abgr & GREEN_MASK;
            long r = abgr & RED_MASK;

            long argb = a | ( r << 16 ) | g | ( b >> 16 );

            pixels[i] = ( int )argb;
        }
    }
}
