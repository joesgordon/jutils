package jutils.iris.rasters;

/*******************************************************************************
 * Defines a set of channel values indexable either linearly or via a
 * 2-dimensional plane.
 ******************************************************************************/
public interface IChannel
{
    /** Indicates there is no data present; 0d-2147483648, 0x80000000. */
    public static final int CHANNEL_MISSING = Integer.MIN_VALUE;

    /***************************************************************************
     * Returns the name of this channel.
     * @return the name of this channel.
     **************************************************************************/
    public String getName();

    /***************************************************************************
     * Returns the number of bits that make up this channel.
     * @return the number of bits that make up this channel.
     **************************************************************************/
    public int getBitDepth();

    /***************************************************************************
     * Returns the number of values in this channel.
     * @return the number of values in this channel.
     **************************************************************************/
    public int getSize();

    /***************************************************************************
     * Returns the height of this channel.
     * @return the height of this channel.
     **************************************************************************/
    public int getHeight();

    /***************************************************************************
     * Returns the width of this channel.
     * @return the width of this channel.
     **************************************************************************/
    public int getWidth();

    /***************************************************************************
     * Returns the channel value at the provided index or
     * {@link #CHANNEL_MISSING} if the value is missing/undefined.
     * @param index 0-relative position of the value to be returned.
     * @return the value at the provided index.
     * @throws ArrayIndexOutOfBoundsException if the row or column is out of
     * range: ({@code index < 0 || index >= getSize()})
     **************************************************************************/
    public int getValue( int index ) throws ArrayIndexOutOfBoundsException;

    /***************************************************************************
     * Returns the channel value at the provided row/column indexes or
     * {@link #CHANNEL_MISSING} if the value is missing/undefined.
     * @param x 0-relative x-position of the value to be returned.
     * @param y 0-relative y-position of the value to be returned.
     * @return
     * @throws IllegalArgumentException if the index is out of range:
     * ({@code row < 0 || column < 0 || row >= getHeight() || column >= getWidth()})
     * @see #getHeight()
     * @see #getWidth()
     **************************************************************************/
    public int getValueAt( int x, int y ) throws IllegalArgumentException;

    /***************************************************************************
     * Sets the value of the channel at the provided index.
     * @param index 0-relative position of the value to be set.
     * @param value the value of the channel to be set.
     * @throws ArrayIndexOutOfBoundsException if the row or column is out of
     * range: ({@code index < 0 || index >= getSize()})
     **************************************************************************/
    public void setValue( int index, int value );

    /***************************************************************************
     * Sets the value of the channel at the provided row/column indexes.
     * @param x 0-relative x-position of the value to be set.
     * @param y 0-relative y-position of the value to be set.
     * @param value the value of the channel to be set.
     * @throws ArrayIndexOutOfBoundsException if the index is out of range:
     * ({@code row < 0 || column < 0 || row >= getHeight() || column >= getWidth()})
     * @see #getHeight()
     * @see #getWidth()
     **************************************************************************/
    public void setValueAt( int x, int y, int value );

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getPixelMask();
}
