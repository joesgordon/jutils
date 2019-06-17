package org.jutils.chart.ui;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ZoomDirection
{
    BOTH( true, true ),
    HORIZONTAL( false, true ),
    VERTICAL( true, false );

    /**  */
    public final boolean zoomVertical;
    /**  */
    public final boolean zoomHorizontal;

    /***************************************************************************
     * @param vert
     * @param horiz
     **************************************************************************/
    private ZoomDirection( boolean vert, boolean horiz )
    {
        this.zoomVertical = vert;
        this.zoomHorizontal = horiz;
    }

    /***************************************************************************
     * @param isHoriz
     * @param isVert
     * @return
     **************************************************************************/
    public static ZoomDirection get( boolean isVert, boolean isHoriz )
    {
        if( isHoriz && isVert )
        {
            return BOTH;
        }
        else if( isHoriz )
        {
            return HORIZONTAL;
        }
        else if( isVert )
        {
            return VERTICAL;
        }

        return BOTH;
    }
}
