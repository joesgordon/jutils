package jutils.math.charts.props;

import java.awt.Color;
import java.awt.Font;

import jutils.core.laf.UIProperty;
import jutils.core.utils.Usable;
import jutils.math.charts.ChartUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartProperties
{
    /**  */
    public Color background;
    /**  */
    public Color axisColor;

    /**  */
    public final Usable<String> title;
    /**  */
    public final Font titleFont;
    /**  */
    public final Color titleColor;

    /**  */
    public final AxesProperties primaryAxes;
    /**  */
    public final AxesProperties secondaryAxes;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartProperties()
    {
        this.background = ChartUtils.BACKGROUND;
        this.axisColor = ChartUtils.AXIS;

        this.title = new Usable<String>( true, "Chart" );
        this.titleFont = UIProperty.LABEL_FONT.getFont();
        this.titleColor = ChartUtils.AXIS;

        this.primaryAxes = new AxesProperties();
        this.secondaryAxes = new AxesProperties();
    }
}
