package jutils.math.charts.props;

import java.awt.Color;
import java.awt.Font;

import jutils.core.laf.UIProperty;
import jutils.math.charts.ChartUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartProperties
{
    /**  */
    public final TextProperties title;
    /**  */
    public final TextProperties subtitle;
    /**  */
    public final TextProperties topBottomLabel;

    /**  */
    public Color background;
    /**  */
    public Color axisColor;

    /**  */
    public final Font titleFont;
    /**  */
    public final Color titleColor;

    /**  */
    public final AxesProperties primaryAxes;
    /**  */
    public final AxesProperties secondaryAxes;

    /**  */
    public final LegendProperties legend;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartProperties()
    {
        this.title = new TextProperties();
        this.subtitle = new TextProperties();
        this.topBottomLabel = new TextProperties();

        this.background = ChartUtils.BACKGROUND;
        this.axisColor = ChartUtils.AXIS;

        this.titleFont = UIProperty.LABEL_FONT.getFont();
        this.titleColor = ChartUtils.AXIS;

        this.primaryAxes = new AxesProperties();
        this.secondaryAxes = new AxesProperties();

        this.legend = new LegendProperties();
    }
}
