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
public class MonoIntRaster implements IRaster
{
    /**  */
    public final byte [] pixelData;
    /**  */
    public final int [] pixels;
    /**  */
    private final RasterConfig config;
    /**  */
    public IPixelIndexer indexer;

    /***************************************************************************
     * @param width
     * @param height
     * @param depth
     **************************************************************************/
    public MonoIntRaster( int width, int height, int depth )
    {
        this.config = new RasterConfig();

        config.width = width;
        config.height = height;
        config.channelCount = 1;
        config.channels[0].name = "Mono";
        config.channels[0].bitDepth = depth;
        config.packed = false;
        config.indexing = IndexingType.ROW_MAJOR;
        config.channelLoc = ChannelPlacement.INTERLEAVED;
        this.indexer = IPixelIndexer.createIndexer( config.indexing );

        this.pixelData = new byte[config.getUnpackedSize()];
        this.pixels = new int[config.getPixelCount()];
    }

    /***************************************************************************
     * @param bitDepth
     **************************************************************************/
    public void setBitDepth( int bitDepth )
    {
        if( bitDepth < 32 )
        {
            config.channels[0].bitDepth = bitDepth;
        }
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
        return pixels[p] & 0xFFFFFFFFL;
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
        int index = indexer.getIndex( config.width, config.height, y, x );
        return getPixel( index );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int x, int y, long value )
    {
        int index = indexer.getIndex( config.width, config.height, y, x );

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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public byte [] getPixelData()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixelData( byte [] pixelData )
    {
        Utils.byteArrayCopy( pixelData, 0, this.pixelData, 0,
            this.pixelData.length );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void readPixels( ByteOrdering order )
    {
    }
}
