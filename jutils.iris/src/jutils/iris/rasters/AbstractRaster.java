package jutils.iris.rasters;

import java.util.function.Function;

import jutils.iris.IrisUtils;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IPixelIndexer;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class AbstractRaster implements IRaster
{
    /**  */
    protected final int width;
    /**  */
    protected final int height;
    /**  */
    protected final IndexingType indexingMethod;
    /**  */
    protected final ChannelStore channelPlacement;

    /**  */
    protected final int pixelCount;
    /**  */
    protected final IChannel [] channels;
    /**  */
    protected final int packedSize;
    /**  */
    protected final int unpackedSize;
    /**  */
    protected final IPixelIndexer indexer;

    /***************************************************************************
     * @param width
     * @param height
     * @param indexingMethod
     * @param channelPlacement
     * @param packed
     * @param channelBuilder
     **************************************************************************/
    protected AbstractRaster( int width, int height,
        IndexingType indexingMethod, ChannelStore channelPlacement,
        boolean packed, Function<IRaster, IChannel []> channelBuilder )
    {
        this.width = width;
        this.height = height;
        this.indexingMethod = indexingMethod;
        this.channelPlacement = channelPlacement;

        this.pixelCount = width * height;

        this.channels = channelBuilder.apply( this );

        int bitDepth = 0;

        if( channelPlacement == ChannelStore.BAYER )
        {
            bitDepth = channels[0].getBitDepth();
        }
        else
        {
            bitDepth = 0;

            for( IChannel c : channels )
            {
                bitDepth += c.getBitDepth();
            }
        }

        if( packed )
        {
            this.packedSize = IrisUtils.getPackedSize( bitDepth, pixelCount );
            this.unpackedSize = IrisUtils.getUnpackedSize( bitDepth,
                pixelCount );
        }
        else
        {
            this.packedSize = IrisUtils.getUnpackedSize( bitDepth, pixelCount );
            this.unpackedSize = packedSize;
        }

        this.indexer = IPixelIndexer.createIndexer( indexingMethod );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final int getWidth()
    {
        return width;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final int getHeight()
    {
        return height;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final int getPixelCount()
    {
        return pixelCount;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final int getChannelCount()
    {
        return channels.length;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final IndexingType getIndexingMethod()
    {
        return indexingMethod;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final ChannelStore getChannelPlacement()
    {
        return channelPlacement;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public final IChannel getChannel( int index )
    {
        return channels[index];
    }
}
