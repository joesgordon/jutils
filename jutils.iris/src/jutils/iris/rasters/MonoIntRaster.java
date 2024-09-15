package jutils.iris.rasters;

import java.awt.Point;
import java.io.IOException;

import jutils.core.Utils;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.utils.BitMasks;
import jutils.core.utils.ByteOrdering;
import jutils.iris.IrisUtils;
import jutils.iris.data.ChannelStore;
import jutils.iris.data.IndexingType;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MonoIntRaster extends AbstractRaster
{
    /**  */
    private final byte [] buffer;
    /**  */
    private final int [] pixels;

    /***************************************************************************
     * @param width
     * @param height
     * @param bitDepth
     **************************************************************************/
    public MonoIntRaster( int width, int height, int bitDepth )
    {
        super( width, height, IndexingType.ROW_MAJOR, ChannelStore.INTERLEAVED,
            false, ( r ) -> createChannels( r, bitDepth ) );

        this.buffer = new byte[super.unpackedSize];
        this.pixels = new int[super.pixelCount];
    }

    /***************************************************************************
     * @param r
     * @param bitDepth
     * @return
     **************************************************************************/
    private static IChannel [] createChannels( IRaster r, int bitDepth )
    {
        IChannel mono = new InterleavedChannel( r, bitDepth, 0,
            "Mono" + bitDepth );

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
        return pixels[p] & 0xFFFFFFFFL;
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

        int mask = ( int )BitMasks.getBitMask( bitDepth );

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
