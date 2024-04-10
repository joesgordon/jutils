package jutils.iris.rasters;

import jutils.core.Utils;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.IPixelIndexer;
import jutils.iris.data.IndexingType;
import jutils.iris.data.RasterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Mono8Raster implements IRaster
{
    /**  */
    public final byte [] buffer;
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
        config.channels[0].name = "Mono8";
        config.channels[0].bitDepth = 8;
        config.packed = false;
        config.indexing = IndexingType.ROW_MAJOR;
        config.channelLoc = ChannelPlacement.INTERLEAVED;
        this.indexer = IPixelIndexer.createIndexer( config.indexing );

        this.buffer = new byte[config.getUnpackedSize()];
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
    public int getPixelIndex( int x, int y )
    {
        return indexer.getIndex( config.width, config.height, x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixel( int p )
    {
        try
        {
            return buffer[p] & 0xFFL;
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            RasterConfig c = config;

            String err = String.format(
                "Unable to access pixel @ %d in image of %d x %d = %d pixels",
                p, c.width, c.height, c.getPixelCount() );
            throw new IllegalStateException( err, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int p, long value )
    {
        this.buffer[p] = ( byte )value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixelAt( int x, int y )
    {
        try
        {
            return getPixel( getPixelIndex( x, y ) );
        }
        catch( IllegalStateException ex )
        {
            RasterConfig c = config;

            String err = String.format(
                "Unable to access pixel @ %d,%d in image of %d x %d = %d pixels",
                x, y, c.width, c.height, c.getPixelCount() );
            throw new IllegalStateException( err, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixelAt( int x, int y, long value )
    {
        setPixel( getPixelIndex( x, y ), value );
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
    public int getChannelAt( int x, int y, int c )
    {
        return ( int )getPixelAt( x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setChannelAt( int x, int y, int c, int value )
    {
        setPixelAt( x, y, value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getBufferData()
    {
        return this.buffer;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setBufferData( byte [] buffer, ByteOrdering order )
    {
        Utils.byteArrayCopy( buffer, 0, this.buffer, 0, this.buffer.length );
    }
}
