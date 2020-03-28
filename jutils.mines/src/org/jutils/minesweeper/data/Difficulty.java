package org.jutils.minesweeper.data;

import org.jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Difficulty implements INamedItem
{
    /**  */
    EASY( 10, 10, "Easy" ),
    /**  */
    MEDIUM( 18, 40, "Medium" ),
    /**  */
    HARD( 24, 99, "Hard" );

    /**  */
    public final int size;
    /**  */
    public final int numFlags;
    /**  */
    public final String name;

    /***************************************************************************
     * @param size
     * @param numFlags
     * @param name
     **************************************************************************/
    private Difficulty( int size, int numFlags, String name )
    {
        this.size = size;
        this.numFlags = numFlags;
        this.name = name;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    int getSafeCount()
    {
        return size * size - numFlags;
    }
}
