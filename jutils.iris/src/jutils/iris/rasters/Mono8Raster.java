package jutils.iris.rasters;

import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.IPixelIndexer;
import jutils.iris.data.IRaster;
import jutils.iris.data.IndexingType;
import jutils.iris.data.RasterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Mono8Raster implements IRaster
{
    /**  */
    public final byte [] pixels;
    /**  */
    private final RasterConfig config;
    /**  */
    public IPixelIndexer indexer;

    /***************************************************************************
     * @param width
     * @param height
     **************************************************************************/
    public Mono8Raster( int width, int height )
    {
        this.config = new RasterConfig();

        config.width = width;
        config.height = height;
        config.channelCount = 1;
        config.bitDepth = 8;
        config.packed = false;
        config.indexing = IndexingType.ROW_MAJOR;
        config.channels = ChannelPlacement.INTERLEAVED;
        this.indexer = IPixelIndexer.createIndexer( config.indexing );

        this.pixels = new byte[config.getUnpackedSize()];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public RasterConfig getConfig()
    {
        return new RasterConfig( this.config );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixel( int p )
    {
        return pixels[p] & 0xFFL;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int p, long value )
    {
        this.pixels[p] = ( byte )value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixel( int x, int y )
    {
        int index = indexer.getIndex( config.width, config.height, x, y );
        return getPixel( index );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int x, int y, long value )
    {
        int index = indexer.getIndex( config.width, config.height, x, y );

        setPixel( index, value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getChannel( int p, int c )
    {
        return ( int )getPixel( p );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setChannel( int p, int c, int value )
    {
        setPixel( p, value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getChannel( int x, int y, int c )
    {
        return ( int )getPixel( x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setChannel( int x, int y, int c, int value )
    {
        setPixel( x, y, value );
    }
}
