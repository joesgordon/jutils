package jutils.plot.model;

import java.awt.Color;
import java.awt.Font;

import jutils.plot.data.QuadSide;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Legend
{
    /**  */
    public boolean visible;
    /**  */
    public final WidgetBorder border;
    /**  */
    public QuadSide side;
    /**  */
    public Color fill;
    /**  */
    public Font font;

    /***************************************************************************
     * 
     **************************************************************************/
    public Legend()
    {
        this.visible = false;
        this.border = new WidgetBorder();
        this.side = QuadSide.BOTTOM;
        this.fill = Color.white;
        this.font = new Font( "Helvetica", Font.PLAIN, 12 );
    }
}
