package org.jutils.plot.model;

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
    /**  */
    public final List<FillSet> fills;

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
        this.fills = new ArrayList<>();

        topBottomLabel.font = topBottomLabel.font.deriveFont( 10.0f );
        topBottomLabel.visible = false;
        topBottomLabel.alignment = HorizontalAlignment.CENTER;

        title.alignment = HorizontalAlignment.CENTER;

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 12.0f );
        subtitle.visible = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        title.text = "Title";
        title.visible = false;
        domainAxis.title.text = "Domain";
        domainAxis.title.visible = false;
        rangeAxis.title.text = "Range";
        rangeAxis.title.visible = false;
        secDomainAxis.title.text = "Secondary Domain";
        secDomainAxis.title.visible = false;
        secRangeAxis.title.text = "Secondary Range";
        secRangeAxis.title.visible = false;
        legend.visible = false;

        series.clear();
    }

    /***************************************************************************
     * @param s1
     * @param s2
     **************************************************************************/
    public void fill( Series s1, Series s2 )
    {
        FillSet set = new FillSet( s1, s2 );

        fills.add( set );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static final class FillSet
    {
        /**  */
        public final Series s1;
        /**  */
        public final Series s2;

        /**  */
        public double x1;
        /**  */
        public double x2;

        /**
         * @param s1
         * @param s2
         */
        public FillSet( Series s1, Series s2 )
        {
            this.s1 = s1;
            this.s2 = s2;

            Interval d1 = s1.calcDomainSpan();
            Interval d2 = s2.calcDomainSpan();

            this.x1 = Math.min( d1.min, d2.min );
            this.x2 = Math.min( d1.max, d2.max );
        }
    }
}
