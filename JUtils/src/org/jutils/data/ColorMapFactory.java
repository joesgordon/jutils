package org.jutils.data;

import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.util.HashMap;
import java.util.Map;

/*******************************************************************************
 * Defines methods of generating different types of color maps.
 ******************************************************************************/
public class ColorMapFactory
{
    /** Set of map creators associated with their type. */
    private final Map<ColorMapType, IMapCreator> maps;

    /***************************************************************************
     * Creates a new factory.
     **************************************************************************/
    public ColorMapFactory()
    {
        this.maps = new HashMap<>();

        IMapCreator imc;

        imc = ( l, u ) -> createQuadraticMap();
        maps.put( ColorMapType.QUADRATIC, imc );

        imc = ( l, u ) -> createLinearStepMap();
        maps.put( ColorMapType.LINEAR_STEP, imc );

        imc = ( l, u ) -> createGrayscaleMap( l, u, 8 );
        maps.put( ColorMapType.GRAYSCALE, imc );

        imc = ( l, u ) -> createMatlabHotMap( l, u );
        maps.put( ColorMapType.MATLAB_HOT, imc );

        imc = ( l, u ) -> createMatlabDefault( l, u );
        maps.put( ColorMapType.MATLAB_DEFAULT, imc );

        imc = ( l, u ) -> createNightVision( l, u );
        maps.put( ColorMapType.NIGHT_VISION, imc );
    }

    /***************************************************************************
     * Creates a new color map with of the provided type with the provided lower
     * and upper bounds.
     * @param map the type of map to be created.
     * @param lower values <= this bound will have the lowest value of the map.
     * @param upper values >= this bound will have the highest value of the map.
     * @return the generated map.
     **************************************************************************/
    public IndexColorModel get( ColorMapType map, int lower, int upper )
    {
        IMapCreator imc = maps.get( map );

        if( imc == null )
        {
            return null;
        }

        return imc.createMap( lower, upper );
    }

    /***************************************************************************
     * @param lower
     * @param upper
     * @param bits
     * @return
     **************************************************************************/
    public static IndexColorModel createGrayscaleMap( int lower, int upper,
        int bits )
    {
        int count = 1 << bits;
        int lastIndex = count - 1;
        int [] cmap = new int[count];
        int cnt = upper - lower + 1;
        double grayIncr = lastIndex / ( double )cnt;

        // The gray ramp will be between lower and upper
        double gray = 0.0;
        int i = 0;

        for( ; i < lower; i++ )
        {
            cmap[i] = 0;
        }

        for( ; i <= upper; i++ )
        {
            int g = ( int )gray;
            cmap[i] = ( g << 16 ) | ( g << 8 ) | g;
            gray += grayIncr;
        }

        for( ; i < cmap.length; i++ )
        {
            cmap[i] = -1;
        }

        return new IndexColorModel( bits, count, cmap, 0, false, -1,
            DataBuffer.TYPE_BYTE );
    }

    /***************************************************************************
     * @param lower
     * @param upper
     * @return
     **************************************************************************/
    public static IndexColorModel createMatlabHotMap( int lower, int upper )
    {
        int [] seed = new int[] { 720896, 1376256, 2097152, 2818048, 3473408,
            4194304, 4849664, 5570560, 6291456, 6946816, 7667712, 8388608,
            9043968, 9764864, 10420224, 11141120, 11862016, 12517376, 13238272,
            13959168, 14614528, 15335424, 15990784, 16711680, 16714496,
            16717056, 16719872, 16722688, 16725248, 16728064, 16730624,
            16733440, 16736256, 16738816, 16741632, 16744448, 16747008,
            16749824, 16752384, 16755200, 16758016, 16760576, 16763392,
            16766208, 16768768, 16771584, 16774144, 16776960, 16776976,
            16776992, 16777008, 16777024, 16777040, 16777056, 16777072,
            16777088, 16777103, 16777119, 16777135, 16777151, 16777167,
            16777183, 16777199, 16777215 };

        return createColorModelFromSeed( seed, lower, upper );
    }

    /***************************************************************************
     * @param lower
     * @param upper
     * @return
     **************************************************************************/
    public static IndexColorModel createMatlabDefault( int lower, int upper )
    {
        int [] seed = new int[] { 143, 159, 175, 191, 207, 223, 239, 255, 4351,
            8447, 12543, 16639, 20735, 24831, 28927, 33023, 36863, 40959, 45055,
            49151, 53247, 57343, 61439, 65535, 1114095, 2162655, 3211215,
            4259775, 5308335, 6356895, 7405455, 8454016, 9437040, 10485600,
            11534160, 12582720, 13631280, 14679840, 15728400, 16776960,
            16772864, 16768768, 16764672, 16760576, 16756480, 16752384,
            16748288, 16744448, 16740352, 16736256, 16732160, 16728064,
            16723968, 16719872, 16715776, 16711680, 15663104, 14614528,
            13565952, 12517376, 11468800, 10420224, 9371648, 8388608 };

        return createColorModelFromSeed( seed, lower, upper );
    }

    /***************************************************************************
     * @param seed
     * @param start
     * @param end
     * @return
     **************************************************************************/
    private static IndexColorModel createColorModelFromSeed( int [] seed,
        int start, int end )
    {
        int [] cmap = new int[256];
        int sidx = 0;
        int midx = 0;

        // ---------------------------------------------------------------------
        // Copy seed 0 until start.
        // ---------------------------------------------------------------------
        for( ; midx < start; midx++ )
        {
            cmap[midx] = seed[sidx];
        }

        // ---------------------------------------------------------------------
        // Span start to end with
        // ---------------------------------------------------------------------
        double dw = ( seed.length - 2 ) / ( double )( end - start );
        for( ; midx <= end; midx++ )
        {
            sidx = ( int )Math.round( dw * ( midx - start ) );

            cmap[midx] = seed[sidx];
        }

        // ---------------------------------------------------------------------
        // Copy last seed until end.
        // ---------------------------------------------------------------------
        sidx = seed.length - 1;
        for( ; midx < cmap.length; midx++ )
        {
            cmap[midx] = seed[sidx];
        }

        return new IndexColorModel( 8, 256, cmap, 0, false, -1,
            DataBuffer.TYPE_BYTE );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IndexColorModel createQuadraticMap()
    {
        byte [] r = new byte[256];
        byte [] g = new byte[256];
        byte [] b = new byte[256];

        double rd, gd, bd;
        double s = 0.02;

        for( int d = 0; d < r.length; d++ )
        {
            rd = -s * Math.pow( ( d - 224.0 ), 2.0 ) + 255.0;
            gd = -s * Math.pow( ( d - 128.0 ), 2.0 ) + 255.0;
            bd = -s * Math.pow( ( d - 32.0 ), 2.0 ) + 255.0;

            rd = Math.max( rd, 0 );
            gd = Math.max( gd, 0 );
            bd = Math.max( bd, 0 );

            r[d] = ( byte )( rd );
            g[d] = ( byte )( gd );
            b[d] = ( byte )( bd );
        }

        return new IndexColorModel( 8, 256, r, g, b );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IndexColorModel createLinearStepMap()
    {
        byte [] r = new byte[256];
        byte [] g = new byte[256];
        byte [] b = new byte[256];

        linearize( b, 0, 64, 0, 255 );
        linearize( b, 64, 128, 255, 0 );
        linearize( b, 128, 256, 0, 255 );

        linearize( g, 0, 128, 0, 255 );
        linearize( g, 128, 192, 255, 0 );
        linearize( g, 192, 256, 0, 255 );

        linearize( r, 0, 256, 0, 255 );

        return new IndexColorModel( 8, 256, r, g, b );
    }

    /***************************************************************************
     * @param lower
     * @param upper
     * @return
     **************************************************************************/
    private static IndexColorModel createNightVision( int lower, int upper )
    {
        int [] seed = new int[256];

        int g = 0;
        int r = 0;
        int b = 0;

        double delta = 256.0 / seed.length * 2.0;
        for( int i = 0; i < 128; i++ )
        {
            g = ( int )Math.round( i * delta );
            seed[i] = ( g << 8 );
        }
        for( int i = 0; i < 128; i++ )
        {
            r = ( int )Math.round( i * delta );
            b = r;
            seed[i + 128] = ( r << 16 ) | ( 255 << 8 ) | b;
        }

        return createColorModelFromSeed( seed, lower, upper );
    }

    /***************************************************************************
     * @param c
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     **************************************************************************/
    private static void linearize( byte [] c, int x1, int x2, int y1, int y2 )
    {
        double v;
        double m = ( y2 - y1 ) / ( double )( x2 - x1 );
        double b = interp( x1, y1, x2, y2, 0.0 );

        for( int d = x1; d < x2; d++ )
        {
            v = m * d + b;

            c[d] = ( byte )( v );
        }
    }

    /***************************************************************************
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @return
     **************************************************************************/
    public static double interp( double x1, double y1, double x2, double y2,
        double x )
    {
        return y1 + ( x - x1 ) * ( y2 - y1 ) / ( x2 - x1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IMapCreator
    {
        /**
         * @param lower
         * @param upper
         * @return
         */
        IndexColorModel createMap( int lower, int upper );
    }
}
