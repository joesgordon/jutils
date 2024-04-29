package jutils.iris.rasters;

import java.io.IOException;

import jutils.core.Utils;
import jutils.core.io.BitsReader;
import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
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
    public final byte [] buffer;
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

        this.buffer = new byte[config.getUnpackedSize()];
        this.pixels = new int[config.getPixelCount()];
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
        int size = config.getBytesPerPixel();
        int bitDepth = config.channels[0].bitDepth;

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

        int mask = ( int )BitsReader.MASKS[bitDepth];

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
