package org.jutils.chart.model;

import java.awt.Color;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WidgetBorder
{
    /**  */
    public boolean visible;
    /**  */
    public Color color;
    /**  */
    public int thickness;

    /***************************************************************************
     * 
     **************************************************************************/
    public WidgetBorder()
    {
        this.visible = true;
        this.color = Color.black;
        this.thickness = 1;
    }
}
