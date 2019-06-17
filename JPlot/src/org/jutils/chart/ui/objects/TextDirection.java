package org.jutils.chart.ui.objects;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum TextDirection
{
    DOWN( 0.0 ),
    RIGHT( -Math.PI / 2 ),
    LEFT( Math.PI / 2 ),
    UP( Math.PI );

    /**  */
    public final double angle;

    /***************************************************************************
     * @param angle
     **************************************************************************/
    private TextDirection( double angle )
    {
        this.angle = angle;
    }
}
