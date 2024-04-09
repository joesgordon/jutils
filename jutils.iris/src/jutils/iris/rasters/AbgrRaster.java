package jutils.iris.rasters;

import jutils.core.Utils;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.IPixelIndexer;
import jutils.iris.data.IndexingType;
import jutils.iris.data.RasterConfig;

public class AbgrRaster implements IRaster
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
    public AbgrRaster( int width, int height, int depth )
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
    public long getPixelAt( int x, int y )
    {
        int index = indexer.getIndex( config.width, config.height, y, x );
        return getPixel( index );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixelAt( int x, int y, long value )
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
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setBufferData( byte [] pixelData, ByteOrdering order )
    {
        Utils.byteArrayCopy( pixelData, 0, this.pixelData, 0,
            this.pixelData.length );
    }
}
