package jutils.math.charts.props;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import jutils.core.laf.UIProperty;
import jutils.math.charts.ChartUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TicksProperties
{
    /**  */
    public boolean visible;
    /**  */
    public Color tickColor;
    /**  */
    public Font textFont;

    /**  */
    public boolean gridlinesVisible;
    /**  */
    public Color gridlinesColor;

    /**  */
    public boolean autoCalculate;
    /**  */
    public final List<Double> tickValues;

    /***************************************************************************
     * 
     **************************************************************************/
    public TicksProperties()
    {
        this.visible = true;
        this.tickColor = ChartUtils.AXIS;
        this.textFont = UIProperty.LABEL_FONT.getFont();

        this.gridlinesVisible = true;
        this.gridlinesColor = tickColor;

        this.autoCalculate = true;
        this.tickValues = new ArrayList<>();
    }
}
