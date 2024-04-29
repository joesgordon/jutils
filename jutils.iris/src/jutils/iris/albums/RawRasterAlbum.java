package jutils.iris.albums;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.io.IDataStream;
import jutils.iris.colors.BayerColorizer;
import jutils.iris.colors.IColorizer;
import jutils.iris.colors.MonoColorizer;
import jutils.iris.data.BayerOrder;
import jutils.iris.data.RawConfig;
import jutils.iris.rasters.ArgbRaster;
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
        this.rasterSize = config.getImageSize();
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
                    : new MonoIntRaster( config.width, config.height,
                        config.bitDepth );

            case BAYER_GRBG:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrder.GRBG );

            case BAYER_GBRG:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrder.GBRG );

            case BAYER_RGGB:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrder.RGGB );

            case BAYER_BGGR:
                return new BayerRaster( config.width, config.height,
                    config.bitDepth, BayerOrder.BGGR );

            case RGB:
                break;

            case ARGB:
                return new ArgbRaster( config.width, config.height );

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

        raster.setBufferData( pixels, config.endianness );

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
                return new BayerColorizer( BayerOrder.BGGR );

            case BAYER_GBRG:
                return new BayerColorizer( BayerOrder.GBRG );

            case BAYER_GRBG:
                return new BayerColorizer( BayerOrder.GRBG );

            case BAYER_RGGB:
                return new BayerColorizer( BayerOrder.RGGB );

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
