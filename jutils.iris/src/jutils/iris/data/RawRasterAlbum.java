package jutils.iris.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.io.IDataStream;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.rasters.BayerRaster;
import jutils.iris.rasters.IRaster;
import jutils.iris.rasters.Mono8Raster;
import jutils.iris.rasters.MonoIntRaster;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RawRasterAlbum implements IRasterAlbum
{
    /**  */
    private final RawConfig config;
    /**  */
    private final IRaster raster;
    /**  */
    private final String name;
    /**  */
    private final IDataStream stream;
    /**  */
    private final int rasterSize;
    /**  */
    private final int frameSize;
    /**  */
    private final int count;

    /***************************************************************************
     * @param file
     * @param config
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    @SuppressWarnings( "resource")
    public RawRasterAlbum( File file, RawConfig config )
        throws FileNotFoundException, IOException
    {
        this( file.getAbsolutePath(),
            new DataStream( new FileStream( file, true ), config.endianness ),
            config );
    }

    /***************************************************************************
     * @param name
     * @param stream
     * @param config
     * @throws IOException
     **************************************************************************/
    public RawRasterAlbum( String name, IDataStream stream, RawConfig config )
        throws IOException
    {
        this.config = config;
        this.raster = createRaster( config );
        this.name = name;
        this.stream = stream;
        this.rasterSize = config.width * config.height * config.bitDepth;
        this.frameSize = rasterSize + config.imageHeaderLen;

        int frameCount = ( int )( ( stream.getLength() -
            config.fileHeaderLen ) / frameSize );

        if( config.imageCount.isUsed && config.imageCount.data < frameCount )
        {
            frameCount = config.imageCount.data;
        }
        this.count = frameCount;
    }

    /***************************************************************************
     * @param config
     * @return
     **************************************************************************/
    private static IRaster createRaster( RawConfig config )
    {
        switch( config.format )
        {
            case MONOCHROME:
                return config.bitDepth < 9
                    ? new Mono8Raster( config.width, config.height )
                    : new MonoIntRaster( config.width, config.height );

            case ARGB:
                break;

            case BAYER_BGGR:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrdering.BGGR );

            case BAYER_GBRG:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrdering.GBRG );

            case BAYER_GRBG:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrdering.GRBG );

            case BAYER_RGGB:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrdering.RGGB );

            case RGB:
                break;

            case YCBCR:
                break;

            case YUV:
                break;
        }

        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getRasterCount()
    {
        return count;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IRaster getRaster( int index )
    {
        long offset = config.getOffset( index );
        int imageSize = config.getImageSize();
        byte [] pixels = new byte[imageSize];

        try
        {
            stream.seek( offset );
            stream.readFully( pixels );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Unable to read image", ex );
        }

        raster.setPixelData( pixels );
        raster.readPixels( config.endianness );

        return raster;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IColorizer getColorizer()
    {
        switch( config.format )
        {
            case MONOCHROME:
                return new MonoColorizer();

            case ARGB:
                break;

            case BAYER_BGGR:
                break;

            case BAYER_GBRG:
                break;

            case BAYER_GRBG:
                break;

            case BAYER_RGGB:
                break;

            case RGB:
                break;

            case YCBCR:
                break;

            case YUV:
                break;
        }

        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
