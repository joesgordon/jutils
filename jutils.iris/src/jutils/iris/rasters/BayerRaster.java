package jutils.iris.rasters;

import jutils.core.utils.ByteOrdering;
import jutils.iris.data.BayerOrdering;
import jutils.iris.data.ChannelPlacement;
import jutils.iris.data.IPixelIndexer;
import jutils.iris.data.IndexingType;
import jutils.iris.data.RasterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BayerRaster implements IRaster
{
    /**  */
    public final byte [] pixelData;

    /**  */
    public final int [] pixels;

    /**  */
    private RasterConfig config;
    /**  */
    private BayerOrdering bayerOrder;
    /**  */
    private IPixelIndexer indexer;

    /***************************************************************************
     * @param width
     * @param height
     * @param bitDepth
     * @param bayerOrder
     **************************************************************************/
    public BayerRaster( int width, int height, int bitDepth,
        BayerOrdering bayerOrder )
    {
        this.config = new RasterConfig();
        this.bayerOrder = bayerOrder;

        config.width = width;
        config.height = height;
        config.channelCount = 1;
        config.packed = false;
        config.indexing = IndexingType.ROW_MAJOR;
        config.channelLoc = ChannelPlacement.INTERLEAVED;
        this.indexer = IPixelIndexer.createIndexer( config.indexing );

        config.channels[0].bitDepth = bitDepth;
        config.channels[1].bitDepth = bitDepth;
        config.channels[2].bitDepth = bitDepth;
        config.channels[3].bitDepth = bitDepth;

        switch( bayerOrder )
        {
            case GBRG:
                config.channels[0].name = "Green 1";
                config.channels[1].name = "Blue";
                config.channels[2].name = "Red";
                config.channels[3].name = "Green 2";

            case GRBG:
                config.channels[0].name = "Green 1";
                config.channels[1].name = "Red";
                config.channels[2].name = "Blue";
                config.channels[3].name = "Green 2";

            case RGGB:
                config.channels[0].name = "Red";
                config.channels[1].name = "Green 1";
                config.channels[2].name = "Green 2";
                config.channels[3].name = "Blue";

            case BGGR:
                config.channels[0].name = "Blue";
                config.channels[1].name = "Green 1";
                config.channels[2].name = "Green 2";
                config.channels[3].name = "Red";
        }

        this.pixelData = new byte[config.getPackedSize()];
        this.pixels = new int[config.getPixelCount()];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public RasterConfig getConfig()
    {
        return new RasterConfig( config );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixel( int p )
    {
        return pixelData[p];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int p, long value )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixel( int x, int y )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int x, int y, long value )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getChannel( int p, int c )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setChannel( int p, int c, int value )
    {
        // TODO Auto-generated method stub

    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getChannel( int x, int y, int c )
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setChannel( int x, int y, int c, int value )
    {
        // TODO Auto-generated method stub
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
    public void setPixelData( byte [] pixels )
    {
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void readPixels( ByteOrdering order )
    {
    }
}
