package jutils.iris.rasters;

import java.awt.Point;

import jutils.core.utils.BitMasks;
import jutils.iris.data.IPixelIndexer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BayerChannel implements IChannel
{
    /**  */
    private final IRaster raster;
    /**  */
    private final int bitDepth;
    /**  */
    private final int bit;
    /**  */
    private final String name;
    /**  */
    private final int channelIndex;

    /**  */
    private final int width;
    /**  */
    private final int height;
    /**  */
    private final int size;
    /**  */
    private final IPixelIndexer indexer;
    /**  */
    private final long setMask;
    /**  */
    private final long clearMask;

    /***************************************************************************
     * @param raster
     * @param bitDepth
     * @param bit
     * @param name
     * @param channelIndex
     **************************************************************************/
    public BayerChannel( IRaster raster, int bitDepth, int bit, String name,
        int channelIndex )
    {
        this.raster = raster;
        this.bitDepth = bitDepth;
        this.bit = bit;
        this.name = name;
        this.channelIndex = channelIndex;

        this.width = raster.getWidth() / 2;
        this.height = raster.getHeight() / 2;
        this.size = width * height;
        this.indexer = IPixelIndexer.createIndexer(
            raster.getIndexingMethod() );
        this.setMask = BitMasks.getFieldMask( bitDepth );
        this.clearMask = ~setMask;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getBitDepth()
    {
        return bitDepth;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getSize()
    {
        return size;
        // return raster.getPixelCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getHeight()
    {
        return height;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getWidth()
    {
        return width;
    }

    /***************************************************************************
     * @param sample
     * @return
     **************************************************************************/
    public int sampleToChannel( long sample )
    {
        long value = sample & setMask;

        value = value >> bit;

        return ( int )value;
    }

    /***************************************************************************
     * @param sample
     * @param channel
     * @return
     **************************************************************************/
    public long channelToSample( long sample, int channel )
    {
        long channelValue = channel << bit;
        long sampleValue = sample;

        sampleValue &= clearMask;
        channelValue &= setMask;

        sampleValue = sample | channelValue;

        return sampleValue;
    }

    private Point getLocation( int index )
    {
        Point p = new Point();

        indexer.getLocation( width, height, index, p );

        return p;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue( int index ) throws ArrayIndexOutOfBoundsException
    {
        Point p = getLocation( index );

        return getValueAt( p.y, p.x );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValueAt( int row, int column )
    {
        int r = row;
        int c = column;

        boolean isSecPair = channelIndex < 2;
        boolean isChOdd = ( channelIndex & 1 ) == 1;

        r = r * 2 + ( isSecPair ? 0 : 1 );
        c = c * 2 + ( isChOdd ? 1 : 0 );

        try
        {
            long sample = raster.getPixelAt( c, r );

            return sampleToChannel( sample );
        }
        catch( IllegalArgumentException ex )
        {
            String err = String.format(
                "Unable to access channel %d @ channel location %d,%d = pixel location %d, %d in channel of %d x %d = %d values = image of %d x %d = %d pixels",
                channelIndex, column, row, c, r, width, height, getSize(),
                raster.getWidth(), raster.getHeight(), raster.getPixelCount() );
            throw new IllegalArgumentException( err, ex );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( int index, int value )
    {
        Point p = getLocation( index );

        setValueAt( p.x, p.y, value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValueAt( int x, int y, int value )
    {
        long sample = raster.getPixelAt( x, y );

        sample = channelToSample( sample, value );

        raster.setPixelAt( x, y, sample );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getPixelMask()
    {
        return setMask;
    }
}
