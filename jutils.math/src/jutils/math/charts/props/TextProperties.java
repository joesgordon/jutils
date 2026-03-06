package jutils.math.charts.props;

import java.awt.Color;
import java.awt.Font;

import jutils.core.laf.UIProperty;
import jutils.math.charts.ChartUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TextProperties
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
    public HorizontalAlignment halign;
    /**  */
    public VerticalAlignment valign;

    /***************************************************************************
     * 
     **************************************************************************/
    public TextProperties()
    {
        this.visible = true;
        this.text = "";
        this.font = UIProperty.LABEL_FONT.getFont();
        this.color = ChartUtils.FOREGROUND;
        this.halign = HorizontalAlignment.CENTER;
        this.valign = VerticalAlignment.MIDDLE;
    }
}
