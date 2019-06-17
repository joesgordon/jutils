package org.jutils.chart.model;

import java.awt.Color;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MarkerStyle
{
    /**  */
    public boolean visible;
    /**  */
    public MarkerType type;
    /**  */
    public int weight;
    /**  */
    public Color color;

    /***************************************************************************
     * 
     **************************************************************************/
    public MarkerStyle()
    {
        this.visible = true;
        this.type = MarkerType.CIRCLE;
        this.weight = 4;
        this.color = Color.RED;
    }
}
