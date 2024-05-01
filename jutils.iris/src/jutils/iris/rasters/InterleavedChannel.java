package jutils.iris.rasters;

import jutils.core.utils.BitMasks;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InterleavedChannel implements IChannel
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
    private final long setMask;
    /**  */
    private final long clearMask;

    /***************************************************************************
     * @param raster
     * @param bitDepth
     * @param bit
     * @param name
     **************************************************************************/
    public InterleavedChannel( IRaster raster, int bitDepth, int bit,
        String name )
    {
        this.raster = raster;
        this.bitDepth = bitDepth;
        this.bit = bit;
        this.name = name;
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
        return raster.getPixelCount();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getHeight()
    {
        return raster.getHeight();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getWidth()
    {
        return raster.getWidth();
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue( int index ) throws ArrayIndexOutOfBoundsException
    {
        long sample = raster.getPixel( index );

        return sampleToChannel( sample );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValueAt( int row, int column )
        throws ArrayIndexOutOfBoundsException
    {
        long sample = raster.getPixelAt( column, row );

        return sampleToChannel( sample );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( int index, int value )
    {
        long sample = raster.getPixel( index );

        sample = channelToSample( sample, value );

        raster.setPixel( index, sample );
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
