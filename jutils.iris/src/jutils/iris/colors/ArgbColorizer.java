package jutils.iris.colors;

import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ArgbColorizer implements IColorizer
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void colorize( IRaster raster, int [] pixels )
    {
        for( int i = 0; i < pixels.length; i++ )
        {
            long argb = raster.getPixel( i );

            pixels[i] = ( int )argb;
        }
    }
}
