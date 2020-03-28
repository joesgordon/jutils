package org.jutils.minesweeper.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GridSpace
{
    /**  */
    public final int index;
    /**  */
    public final int x;
    /**  */
    public final int y;

    /** */
    public boolean isTopBorder;
    /** */
    public boolean isLeftBorder;
    /** */
    public boolean isBottomBorder;
    /** */
    public boolean isRightBorder;

    /**
     * The number of adjacent mines; < 0 indicates this space is a mine.
     * @see #isMine()
     */
    public int numAdj;

    /**  */
    public SpaceStatus status;

    /***************************************************************************
     * @param index
     * @param x
     * @param y
     **************************************************************************/
    public GridSpace( int index, int x, int y )
    {
        this.index = index;
        this.x = x;
        this.y = y;

        this.numAdj = 0;

        this.status = SpaceStatus.TILE;
    }

    /***************************************************************************
     * @param enableQuestion
     **************************************************************************/
    public void toggleFlag( boolean enableQuestion )
    {
        switch( status )
        {
            case TILE:
                status = enableQuestion ? SpaceStatus.QUESTION
                    : SpaceStatus.FLAGGED;
                break;

            case QUESTION:
                status = SpaceStatus.FLAGGED;
                break;

            case FLAGGED:
                status = SpaceStatus.TILE;
                break;

            case REVEALED:
                break;

            default:
                break;
        }
    }

    /***************************************************************************
     * @return {@code true} if this space is a mine.
     **************************************************************************/
    public boolean isMine()
    {
        return numAdj < 0;
    }

    /***************************************************************************
     * @return {@code true} if this space is not a mine.
     **************************************************************************/
    public boolean isClear()
    {
        return numAdj > -1;
    }

    /***************************************************************************
     * @return {@code true} if this space is a revealed mine.
     **************************************************************************/
    public boolean isDetonated()
    {
        return isMine() && status == SpaceStatus.REVEALED;
    }

    /***************************************************************************
     * @param x
     * @param y
     * @return
     **************************************************************************/
    public boolean isAdjacent( int x, int y )
    {
        int dx = Math.abs( x - this.x );
        int dy = Math.abs( y - this.y );

        return dx < 2 && dy < 2;
    }

    /***************************************************************************
     * @param space
     **************************************************************************/
    public void setBorder( GridSpace space )
    {
        boolean border = false;

        if( ( status != SpaceStatus.REVEALED &&
            space.status == SpaceStatus.REVEALED ) ||
            ( status == SpaceStatus.REVEALED &&
                space.status != SpaceStatus.REVEALED ) )
        {
            border = true;
        }

        // LogUtils.printDebug( "Checking %d,%d against %d,%d: %s vs. %s => %s",
        // x,
        // y, space.x, space.y, status, space.status, border );

        if( space.y == y )
        {
            if( space.x == x - 1 )
            {
                // LogUtils.printDebug( "Setting left to " + border );
                isLeftBorder = border;
                space.isRightBorder = border;
            }
            else if( space.x == x + 1 )
            {
                // LogUtils.printDebug( "Setting Right to " + border );
                isRightBorder = border;
                space.isLeftBorder = border;
            }
        }
        else if( space.x == x )
        {
            if( space.y == y - 1 )
            {
                // LogUtils.printDebug( "Setting Top to " + border );
                isTopBorder = border;
                space.isBottomBorder = border;
            }
            if( space.y == y + 1 )
            {
                // LogUtils.printDebug( "Setting bottom to " + border );
                isBottomBorder = border;
                space.isTopBorder = border;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum SpaceStatus
    {
        /**  */
        TILE,
        /**  */
        QUESTION,
        /**  */
        FLAGGED,
        /**  */
        REVEALED;
    }
}
