package org.jutils.chart.model;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chart
{
    /**  */
    public final ChartOptions options;
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;
    /**  */
    public final TextLabel topBottomLabel;
    /**  */
    public final Axis domainAxis;
    /**  */
    public final Axis rangeAxis;
    /**  */
    public final Axis secDomainAxis;
    /**  */
    public final Axis secRangeAxis;
    /**  */
    public final Legend legend;

    /**  */
    public final List<Series> series;

    /***************************************************************************
     * 
     **************************************************************************/
    public Chart()
    {
        this.options = new ChartOptions();
        this.title = new TextLabel();
        this.subtitle = new TextLabel();
        this.topBottomLabel = new TextLabel();
        this.domainAxis = new Axis();
        this.rangeAxis = new Axis();
        this.secDomainAxis = new Axis();
        this.secRangeAxis = new Axis();
        this.legend = new Legend();

        this.series = new ArrayList<>();

        topBottomLabel.font = topBottomLabel.font.deriveFont( 10.0f );
        topBottomLabel.visible = false;
        topBottomLabel.alignment = HorizontalAlignment.CENTER;

        title.alignment = HorizontalAlignment.CENTER;

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 12.0f );
        subtitle.visible = false;
    }
}
