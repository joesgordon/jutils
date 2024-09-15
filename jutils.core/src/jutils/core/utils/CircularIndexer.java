package jutils.core.utils;

/*******************************************************************************
 * Defines a class that maps available indexes to location indexes. Available
 * indexes start at zero and end at {@link #getSize()} minus one. Location
 * indexes start at zero and end at {@link #getCapacity()} minus one.
 ******************************************************************************/
public class CircularIndexer
{
    /** The index of the first item. */
    private int first;
    /** The number of items. */
    private int size;
    /** The maximum number of items there can be. */
    private int capacity;
    /** {@code true} if a new item does not override an existing when full. */
    private boolean block;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircularIndexer()
    {
        this( 256 );
    }

    /***************************************************************************
     * @param capacity
     **************************************************************************/
    public CircularIndexer( int capacity )
    {
        this( capacity, false );
    }

    /***************************************************************************
     * @param capacity
     * @param block
     **************************************************************************/
    public CircularIndexer( int capacity, boolean block )
    {
        this.first = capacity - 1;
        this.size = 0;
        this.capacity = capacity;
        this.block = block;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isEmpty()
    {
        return size == 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getFirst()
    {
        return first;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSize()
    {
        return size;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getCapacity()
    {
        return capacity;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public int getIndex( int index )
    {
        return toLocation( index );
    }

    /***************************************************************************
     * @return the location of the last item or {@code -1} if there are no
     * items.
     **************************************************************************/
    public int getLast()
    {
        return size == 0 ? -1 : toLocation( size - 1 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int incrementFirst()
    {
        return ( first = toLocation( 1 ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int incrementLast()
    {
        if( size < capacity )
        {
            size += 1;
        }
        else if( !block )
        {
            first += 1;
        }

        return resolve( first + size - 1 );
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    private int toLocation( int index )
    {
        return resolve( first + index );
    }

    /***************************************************************************
     * @param i
     * @return
     **************************************************************************/
    private int resolve( int i )
    {
        if( i < capacity )
        {
            return i;
        }

        return i - capacity;
    }
}
