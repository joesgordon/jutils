package jutils.iris;

import java.awt.Color;
import java.io.File;

import jutils.core.data.IBitField;
import jutils.core.io.BitsReader;
import jutils.core.io.IOUtils;
import jutils.iris.data.RasterConfig;
import jutils.iris.rasters.IRaster;
import jutils.math.MathUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class IrisUtils
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "iris", "options.xml" );

    /**  */
    public static final Color LIGHT_CHECKER = new Color( 0xCCCCCC );
    /**  */
    public static final Color DARK_CHECKER = new Color( 0xBBBBBB );
    /**  */
    public static final Color BORDER_COLOR = new Color( 0x999999 );

    /**  */
    public static final long BYTE0_MASK = IBitField.BYTE_MASK;
    /**  */
    public static final long BYTE1_MASK = IBitField.BYTE_MASK << 8;
    /**  */
    public static final long BYTE2_MASK = IBitField.BYTE_MASK << 16;
    /**  */
    public static final long BYTE3_MASK = IBitField.BYTE_MASK << 24;

    /***************************************************************************
     * 
     **************************************************************************/
    private IrisUtils()
    {
    }

    /***************************************************************************
     * @param bitCount
     * @return
     **************************************************************************/
    public static int getMaxValue( int bitCount )
    {
        return ( int )BitsReader.MASKS[bitCount];
    }

    /***************************************************************************
     * @param r
     **************************************************************************/
    public static void setDiagonalGradient( IRaster r )
    {
        RasterConfig config = r.getConfig();
        int max = config.getMaxPixelValue();
        int w = config.width;
        int h = config.height;
        int cc = config.channelCount;

        float xscale = max / ( float )( w - 1 );
        float yscale = max / ( float )( h - 1 );

        for( int y = 0; y < h; y++ )
        {
            for( int x = 0; x < w; x++ )
            {
                int xf = ( int )Math.round( x * xscale );
                int yf = ( int )Math.round( y * yscale );

                int v = Math.max( xf, yf );

                for( int c = 0; c < cc; c++ )
                {
                    r.setChannelAt( x, y, c, v );
                }
            }
        }
    }

    /***************************************************************************
     * @param raster
     **************************************************************************/
    public static void setJuliaFractal( IRaster raster )
    {
        RasterConfig config = raster.getConfig();
        final int w = config.width;
        final int h = config.height;
        final double rad = 2.;
        final double xmin = -rad;
        final double ymin = -rad;
        final double width = 2 * rad;
        final double height = 2 * rad;

        final double ang = 160;
        final double rang = Math.toRadians( ang );
        final double om = Math.sqrt( .7 * .7 + .3 * .3 );
        final double ox = om * Math.cos( rang );// -.8;
        final double oy = om * Math.sin( rang );// .3;

        final int iters = 256;
        final int maxItersIdx = iters - 1;

        int maxt = 0;

        for( int c = 0; c < w; c++ )
        {
            for( int r = 0; r < h; r++ )
            {
                double x = xmin + c * ( width / ( w - 1 ) );
                double y = ymin + r * ( height / ( h - 1 ) );

                int t;
                for( t = 0; t < maxItersIdx; t++ )
                {
                    double zabs = MathUtils.rss( x, y );

                    if( zabs > rad )
                    {
                        break;
                    }

                    // z = z.times( z ).plus( c );

                    double nx = x * x - y * y;
                    double ny = 2 * x * y;

                    x = nx + ox;
                    y = ny + oy;
                }

                maxt = Math.max( maxt, t );

                raster.setPixelAt( c, r, t );
            }
        }

        double s = maxItersIdx / ( double )maxt;
        double cnt = config.getPixelCount();

        if( maxt < maxItersIdx )
        {
            for( int i = 0; i < cnt; i++ )
            {
                long v = ( long )( s * raster.getPixel( i ) );
                raster.setPixel( i, v );
            }
        }
    }
}
