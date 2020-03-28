package org.jutils.core.utils;

import java.nio.ByteOrder;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * Defines methods of ordering bytes and maps these to a {@link ByteOrder}.
 ******************************************************************************/
public enum ByteOrdering implements INamedItem
{
    /**
     * Defines the ordering pattern that stores the least significant byte first
     * (i.e. 0x00000001 == 0d16777216).
     */
    LITTLE_ENDIAN( "Little Endian", ByteOrder.LITTLE_ENDIAN ),
    /**
     * Defines the ordering pattern that stores the most significant byte first
     * (i.e. 0x00000001 == 0d1).
     */
    BIG_ENDIAN( "Big Endian", ByteOrder.BIG_ENDIAN ),
    /** An alternate way of specifying {@link #LITTLE_ENDIAN}. */
    INTEL_ORDER( "Intel Order", ByteOrder.LITTLE_ENDIAN ),
    /** An alternate way of specifying {@link #BIG_ENDIAN}. */
    NETWORK_ORDER( "Network Order", ByteOrder.BIG_ENDIAN );

    /** The name of this order to be used for display/logging. */
    public final String name;
    /** The NIO order mapped to this order. */
    public final ByteOrder order;

    /***************************************************************************
     * Creates a new ordering using the provided name and NIO order.
     * @param name the name of this order.
     * @param order the NIO order mapped to this order.
     **************************************************************************/
    private ByteOrdering( String name, ByteOrder order )
    {
        this.name = name;
        this.order = order;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }
}
