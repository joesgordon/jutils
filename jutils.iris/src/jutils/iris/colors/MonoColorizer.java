package jutils.iris.colors;

import jutils.iris.data.RasterConfig;
import jutils.iris.rasters.IRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonoColorizer implements IColorizer
{
    /**  */
    private int [] seeds;
    /**  */
    private MonoColorOptions options;

    /***************************************************************************
     * 
     **************************************************************************/
    public MonoColorizer()
    {
        this.options = new MonoColorOptions();
        this.seeds = new int[0];

        setSeeds( options.colorModel );
    }

    /***************************************************************************
     * @param colorModel
     **************************************************************************/
    private void setSeeds( MonoColorModel colorModel )
    {
        switch( colorModel )
        {
            case GRAYSCALE:
                this.seeds = SeedColorModel.createGrayscaleSeeds();
                break;

            case BGR:
                this.seeds = SeedColorModel.createBlueGreenRedSeeds();
                break;

            case HOT:
                this.seeds = SeedColorModel.createHotSeeds();
                break;

            case COOL_WARM:
                this.seeds = SeedColorModel.createCoolWarmSeeds();
                break;

            case NIGHT_VISION:
                this.seeds = SeedColorModel.createNightVisionSeeds();
                break;

            default:
                break;
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void colorize( IRaster raster, int [] pixels )
    {
        RasterConfig config = raster.getConfig();
        long pixelMax = config.getMaxPixelValue();

        for( int i = 0; i < pixels.length; i++ )
        {
            long pixel = raster.getPixel( i );
            int rgb = getColorValue( pixel, pixelMax );

            // int row = i % config.width;
            // int col = i - ( row * config.width );
            //
            // int idx = col * config.height + row;

            pixels[i] = 0xFF000000 | rgb;
        }
    }

    /***************************************************************************
     * @param pixel
     * @param pixelMax
     * @return
     **************************************************************************/
    private int getColorValue( long pixel, long pixelMax )
    {
        long x0 = options.lowThreshold.value;
        long x1 = Math.min( pixelMax, options.highThreshold.value );

        int y1 = seeds.length - 1;

        if( pixel < x0 )
        {
            return options.lowThreshold.color.getRGB();
        }
        else if( pixel > x1 )
        {
            return options.highThreshold.color.getRGB();
        }

        double adjPixel = options.gain * pixel + options.offset;

        int index = ( int )( y1 * ( adjPixel - x0 ) / ( x1 - x0 ) );

        index = Math.min( index, y1 );
        index = Math.max( index, 0 );

        // if( pixel == y1 )
        // {
        // LogUtils.printDebug(
        // "[%d - %d] pixel %d (function %f %f) -> index %d = value %d",
        // config.lowThreshold.value, config.highThreshold.value,
        // pixel, function[0], function[1], index, seeds[index] );
        // }

        return seeds[index];
    }
}
