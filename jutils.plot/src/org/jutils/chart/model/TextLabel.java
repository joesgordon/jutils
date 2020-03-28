package org.jutils.chart.model;

import java.awt.Color;
import java.awt.Font;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextLabel
{
    /**  */
    public boolean visible;
    /**  */
    public String text;
    /**  */
    public Font font;
    /**  */
    public Color color;
    /**  */
    public HorizontalAlignment alignment;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextLabel()
    {
        this( new Font( "Helvetica", Font.PLAIN, 24 ) );
    }

    /***************************************************************************
     * @param font
     **************************************************************************/
    public TextLabel( Font font )
    {
        this.visible = true;
        this.text = "Title";
        this.font = font;
        this.color = Color.black;
        this.alignment = HorizontalAlignment.LEFT;
    }
}
