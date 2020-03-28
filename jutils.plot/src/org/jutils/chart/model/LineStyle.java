package org.jutils.chart.model;

import java.awt.Color;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LineStyle
{
    /**  */
    public boolean visible;
    /**  */
    public LineType type;
    /**  */
    public int weight;
    /**  */
    public Color color;

    /***************************************************************************
     * 
     **************************************************************************/
    public LineStyle()
    {
        this.visible = true;
        this.type = LineType.SOLID;
        this.weight = 1;
        this.color = Color.blue;
    }
}
