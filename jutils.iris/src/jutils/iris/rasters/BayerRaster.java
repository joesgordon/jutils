package jutils.iris.rasters;

import java.awt.Point;
import java.io.IOException;

import jutils.core.Utils;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.utils.BitMasks;
import jutils.core.utils.ByteOrdering;
import jutils.iris.IrisUtils;
import jutils.iris.data.BayerOrder;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BayerRaster extends AbstractRaster
{
    /**  */
    public final byte [] buffer;
    /**  */
    public final int [] pixels;

    // TODO add packing

    /***************************************************************************
     * @param width
     * @param height
     * @param bitDepth
     * @param order
     **************************************************************************/
    public BayerRaster( int width, int height, int bitDepth, BayerOrder order )
    {
        super( width, height, IndexingType.ROW_MAJOR, ChannelStore.BAYER, false,
            ( r ) -> createChannels( r, bitDepth, order ) );

        this.buffer = new byte[super.unpackedSize];
        this.pixels = new int[super.pixelCount];
    }

    /***************************************************************************
     * @param r
     * @param bitDepth
     * @param order
     * @return
     **************************************************************************/
    private static IChannel [] createChannels( IRaster r, int bitDepth,
        BayerOrder order )
    {
        IChannel bayer0 = new BayerChannel( r, bitDepth, 0,
            order.getChannelName( 0 ), 0 );
        IChannel bayer1 = new BayerChannel( r, bitDepth, 0,
            order.getChannelName( 1 ), 1 );
        IChannel bayer2 = new BayerChannel( r, bitDepth, 0,
            order.getChannelName( 2 ), 2 );
        IChannel bayer3 = new BayerChannel( r, bitDepth, 0,
            order.getChannelName( 3 ), 3 );

        return new IChannel[] { bayer0, bayer1, bayer2, bayer3 };
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
            return pixels[p] & 0xFFFFFFFFL;
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            String err = String.format(
                "Unable to access pixel @ %d in image of %d x %d = %d pixels",
                p, super.width, super.height, super.pixelCount );
            throw new IllegalArgumentException( err, ex );

        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setPixel( int p, long value )
    {
        this.pixels[p] = ( int )value;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixelAt( int x, int y )
    {
        int p = getPixelIndex( x, y );

        try
        {
            return getPixel( p );
        }
        catch( ArrayIndexOutOfBoundsException ex )
        {
            String err = String.format(
                "Unable to access pixel @ %d,%d = %d in image of %d x %d = %d pixels",
                x, y, p, super.width, super.height, super.pixelCount );
            throw new IllegalArgumentException( err, ex );
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
    public byte [] getBufferData()
    {
        return buffer;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IntReader
    {
        /**
         * @param s
         * @return
         * @throws IOException
         */
        int read( DataStream s ) throws IOException;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setBufferData( byte [] buffer, ByteOrdering order )
    {
        Utils.byteArrayCopy( buffer, 0, this.buffer, 0, this.buffer.length );

        IntReader reader;
        int bitDepth = super.channels[0].getBitDepth();
        int size = IrisUtils.getBytesPerPixel( bitDepth );

        switch( size )
        {
            case 1:
                reader = ( s ) -> s.read() & 0xFF;
                break;

            case 2:
                reader = ( s ) -> s.readShort() & 0xFFFF;
                break;

            case 4:
                reader = ( s ) -> s.readInt();
                break;

            default:
                throw new IllegalStateException( String.format(
                    "Unable to read %d-byte values into 4-byte pixels",
                    size ) );
        }

        int mask = ( int )BitMasks.getFieldMask( bitDepth );

        try( ByteArrayStream bas = new ByteArrayStream( buffer, buffer.length,
            0, false ); DataStream stream = new DataStream( bas, order ) )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                pixels[i] = reader.read( stream ) & mask;
            }
        }
        catch( IOException ex )
        {
            String err = String.format(
                "Unable to read %d pixels from %d bytes", pixels.length,
                buffer.length );

            throw new RuntimeException( err, ex );
        }
    }
}
