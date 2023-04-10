package org.jutils.core.data;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * Defines a set of functions for identifying and processing bit flags of words.
 ******************************************************************************/
public interface IBitFlag extends INamedItem
{
    /***************************************************************************
     * Returns the 0-relative bit this flag refers to. Bits 0 and 3 are set in
     * the value {@code 0x09}.
     * @return the bit this flag is refers to.
     **************************************************************************/
    public int getBit();

    /***************************************************************************
     * The mask created by shifting the value 0x01, {@link #getBit()} number of
     * times to the left (i.e. {@code 1 << getBit()}.
     * @return the mask for the provided bit.
     * @see #createMask(int)
     **************************************************************************/
    public long getMask();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName();
}
