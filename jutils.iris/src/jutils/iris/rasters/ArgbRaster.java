package jutils.iris.rasters;

import java.awt.Point;
import java.io.IOException;

import jutils.core.Utils;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.utils.ByteOrdering;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ArgbRaster extends AbstractRaster
{
    /**  */
    public final byte [] buffer;
    /**  */
    public final int [] pixels;

    /***************************************************************************
     * @param width
     * @param height
     * @param depth
     **************************************************************************/
    public ArgbRaster( int width, int height )
    {
        super( width, height, IndexingType.ROW_MAJOR, ChannelStore.INTERLEAVED,
            false, ( r ) -> createChannels( r ) );

        this.buffer = new byte[super.unpackedSize];
        this.pixels = new int[super.pixelCount];

        super.channels[0] = new InterleavedChannel( this, 8, 24, "Alpha" );
        super.channels[1] = new InterleavedChannel( this, 8, 16, "Red" );
        super.channels[2] = new InterleavedChannel( this, 8, 8, "Green" );
        super.channels[3] = new InterleavedChannel( this, 8, 0, "Blue" );
    }

    /***************************************************************************
     * @param r
     * @return
     **************************************************************************/
    private static IChannel [] createChannels( IRaster r )
    {
        IChannel alpha = new InterleavedChannel( r, 8, 24, "Alpha" );
        IChannel red = new InterleavedChannel( r, 8, 16, "Red" );
        IChannel green = new InterleavedChannel( r, 8, 8, "Green" );
        IChannel blue = new InterleavedChannel( r, 8, 0, "Blue" );

        return new IChannel[] { alpha, red, blue, green };
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
        return pixels[p] & 0x00000000FFFFFFFFL;
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
        return getPixel( getPixelIndex( x, y ) );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setBufferData( byte [] buffer, ByteOrdering order )
    {
        Utils.byteArrayCopy( buffer, 0, this.buffer, 0, this.buffer.length );

        try( ByteArrayStream bas = new ByteArrayStream( buffer, buffer.length,
            0, false ); DataStream stream = new DataStream( bas, order ) )
        {
            for( int i = 0; i < pixels.length; i++ )
            {
                pixels[i] = stream.readInt();
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
