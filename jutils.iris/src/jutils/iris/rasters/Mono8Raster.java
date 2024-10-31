package jutils.iris.rasters;

import java.awt.Point;

import jutils.core.Utils;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Mono8Raster extends AbstractRaster
{
    /**  */
    private final byte [] buffer;

    /***************************************************************************
     * @param width the width of the raster in pixels.
     * @param height the height of the raster in pixels
     **************************************************************************/
    public Mono8Raster( int width, int height )
    {
        super( width, height, IndexingType.ROW_MAJOR, ChannelStore.INTERLEAVED,
            false, ( r ) -> createChannels( r ) );

        this.buffer = new byte[super.unpackedSize];
    }

    /***************************************************************************
     * @param r
     * @return
     **************************************************************************/
    private static IChannel [] createChannels( IRaster r )
    {
        IChannel mono = new InterleavedChannel( r, 8, 0, "Mono8" );

        return new IChannel[] { mono };
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getPixelIndex( int x, int y )
    {
        return indexer.getIndex( super.width, super.height, x, y );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void getPixelLocation( int index, Point location )
    {
        indexer.getLocation( super.width, super.height, index, location );
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
            String err = String.format(
                "Unable to access pixel @ %d in image of %d x %d = %d pixels",
                p, super.width, super.height, super.pixelCount );
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
            String err = String.format(
                "Unable to access pixel @ %d,%d in image of %d x %d = %d pixels",
                x, y, super.width, super.height, super.pixelCount );
            throw new IllegalStateException( err, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixelAt( int x, int y, long value )
    {
        int index = getPixelIndex( x, y );

        setPixel( index, value );
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
