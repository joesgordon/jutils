package org.jutils.chart.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum QuadSide
{
    TOP( false ),
    LEFT( true ),
    BOTTOM( false ),
    RIGHT( true );

    /**  */
    public final boolean isVertical;

    /***************************************************************************
     * @param isVertical
     **************************************************************************/
    private QuadSide( boolean isVertical )
    {
        this.isVertical = isVertical;
    }
}
