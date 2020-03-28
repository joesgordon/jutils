package org.jutils.chart.model;

import java.awt.Font;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Axis
{
    /**  */
    public final TextLabel title;
    /**  */
    public final TextLabel subtitle;
    /**  */
    public boolean isUsed;
    /**  */
    public boolean autoTicks;
    /**  */
    public boolean dockZero;
    /**  */
    public double tickStart;
    /**  */
    public double tickEnd;
    /**  */
    public double tickWidth;
    /**  */
    private Interval bounds;
    /**  */
    private Interval autoBounds;
    /**  */
    private boolean calcBounds;

    /***************************************************************************
     * 
     **************************************************************************/
    public Axis()
    {
        this.title = new TextLabel();
        this.subtitle = new TextLabel();
        this.autoTicks = true;
        this.tickStart = -5.0;
        this.tickEnd = 5.0;
        this.tickWidth = 1.0;
        this.dockZero = false;
        this.bounds = new Interval( -5.0, 5.0 );
        this.autoBounds = new Interval( -5.0, 5.0 );
        this.calcBounds = true;

        title.alignment = HorizontalAlignment.CENTER;
        title.font = title.font.deriveFont( 14.0f ).deriveFont( Font.BOLD );
        title.visible = false;
        title.text = "primary";

        subtitle.alignment = HorizontalAlignment.CENTER;
        subtitle.font = subtitle.font.deriveFont( 10.0f );
        subtitle.visible = false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Interval getBounds()
    {
        return calcBounds ? autoBounds : bounds;
    }

    /***************************************************************************
     * @param bounds
     **************************************************************************/
    public void setBounds( Interval bounds )
    {
        this.calcBounds = false;
        this.bounds = bounds;
    }

    /***************************************************************************
     * @param bounds
     **************************************************************************/
    public void setAutoBounds( Interval bounds )
    {
        this.isUsed = bounds != null;
        this.autoBounds = bounds;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void restoreAuto()
    {
        this.calcBounds = true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoBounds()
    {
        return calcBounds;
    }
}
