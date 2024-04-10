package jutils.iris.colors;

import jutils.core.io.LogUtils;
import jutils.iris.IrisUtils;
import jutils.iris.data.BayerOrder;
import jutils.iris.data.RasterConfig;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BayerColorizer implements IColorizer
{
    /**  */
    private final BayerOrder order;
    /**  */
    private final BayerDemosaicAlgorithm algorithm;

    /***************************************************************************
     * @param order
     **************************************************************************/
    public BayerColorizer( BayerOrder order )
    {
        this.order = order;
        this.algorithm = BayerDemosaicAlgorithm.NEAREST;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void colorize( IRaster raster, int [] pixels )
    {
        switch( algorithm )
        {
            case NEAREST:
                colorizeNearest( raster, pixels );
                break;

            case BILINEAR:
                colorizeBilinear( raster, pixels );
                break;

            case BICUBIC:
                colorizeBicubic( raster, pixels );
                break;

            case SPLINE:
                colorizeSpline( raster, pixels );
                break;

            case LANCZOS:
                colorizeLanczos( raster, pixels );
                break;
        }
    }

    /***************************************************************************
     * @param raster
     * @param pixels
     **************************************************************************/
    private void colorizeNearest( IRaster raster, int [] pixels )
    {
        RasterConfig config = raster.getConfig();

        float maxValue = config.getMaxPixelValue();

        int [] erec = { 0, 0, 0, 0, 0, 0 };
        int [] eroc = { 0, 0, 0, 0, 0, 0 };
        int [] orec = { 0, 0, 0, 0, 0, 0 };
        int [] oroc = { 0, 0, 0, 0, 0, 0 };

        switch( order )
        {
            case GRBG:
                erec = new int[] { 1, 0, 0, 0, 0, 1 };
                eroc = new int[] { 0, -1, -1, 0, 0, 1 };
                orec = new int[] { 1, 1, 0, -1, 0, 0 };
                oroc = new int[] { 0, 0, -1, -1, 0, 0 };
                break;

            case GBRG:
                erec = new int[] { 0, 0, 1, 1, 0, 0 };
                eroc = new int[] { -1, -1, 0, 1, 0, 0 };
                orec = new int[] { 0, 1, 1, 0, 0, -1 };
                oroc = new int[] { -1, 0, 0, 0, 0, -1 };
                break;

            case RGGB:
                erec = new int[] { 0, 1, 1, 0, 0, 1 };
                eroc = new int[] { -1, 0, 0, 0, 0, 1 };
                orec = new int[] { 0, 0, 1, -1, 0, 0 };
                oroc = new int[] { -1, -1, 0, -1, 0, 0 };
                break;

            case BGGR:
                erec = new int[] { 1, 1, 0, 1, 0, 0 };
                eroc = new int[] { 0, 0, -1, 1, 0, 0 };
                orec = new int[] { 1, 0, 0, 0, 0, -1 };
                oroc = new int[] { 0, -1, -1, 0, 0, -1 };
                break;
        }

        for( int p = 0; p < pixels.length; p++ )
        {
            int y = p / config.width;
            int x = p - ( y * config.width );

            boolean isEvenRow = ( y % 2 ) == 0;
            boolean isEvenCol = ( x % 2 ) == 0;

            long r;
            long g;
            long b;

            if( isEvenRow )
            {
                if( isEvenCol )
                {
                    r = raster.getPixelAt( x + erec[0], y + erec[3] );
                    g = raster.getPixelAt( x + erec[1], y + erec[4] );
                    b = raster.getPixelAt( x + erec[2], y + erec[5] );
                }
                else
                {
                    r = raster.getPixelAt( x + eroc[0], y + eroc[3] );
                    g = raster.getPixelAt( x + eroc[1], y + eroc[4] );
                    b = raster.getPixelAt( x + eroc[2], y + eroc[5] );
                }
            }
            else
            {
                if( isEvenCol )
                {
                    r = raster.getPixelAt( x + orec[0], y + orec[3] );
                    g = raster.getPixelAt( x + orec[1], y + orec[4] );
                    b = raster.getPixelAt( x + orec[2], y + orec[5] );
                }
                else
                {
                    r = raster.getPixelAt( x + oroc[0], y + oroc[3] );
                    g = raster.getPixelAt( x + oroc[1], y + oroc[4] );
                    b = raster.getPixelAt( x + oroc[2], y + oroc[5] );
                }
            }

            r = Math.round( 255f * r / maxValue );
            b = Math.round( 255f * b / maxValue );
            g = Math.round( 255f * g / maxValue );

            pixels[p] = ( int )( IrisUtils.BYTE3_MASK | ( r << 16 ) |
                ( g << 8 ) | b );

            if( p == 513 )
            {
                LogUtils.printDebug( "%d,%d -> %d: %02X,%02X,%02X => %04X", x,
                    y, p, r, g, b, pixels[p] );
                LogUtils.printDebug( "here" );
            }
        }
    }

    /***************************************************************************
     * @param raster
     * @param pixels
     **************************************************************************/
    private void colorizeBilinear( IRaster raster, int [] pixels )
    {
        // TODO Auto-generated method stub
        colorizeNearest( raster, pixels );
    }

    /***************************************************************************
     * @param raster
     * @param pixels
     **************************************************************************/
    private void colorizeBicubic( IRaster raster, int [] pixels )
    {
        // TODO Auto-generated method stub
        colorizeBilinear( raster, pixels );
    }

    /***************************************************************************
     * @param raster
     * @param pixels
     **************************************************************************/
    private void colorizeSpline( IRaster raster, int [] pixels )
    {
        // TODO Auto-generated method stub
        colorizeBilinear( raster, pixels );
    }

    private void colorizeLanczos( IRaster raster, int [] pixels )
    {
        // TODO Auto-generated method stub
        colorizeBilinear( raster, pixels );
    }
}
