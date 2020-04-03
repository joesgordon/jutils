package org.jutils.plot.model;

import java.awt.Font;

import org.jutils.core.utils.Usable;

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
    public Usable<Double> tickStart;
    /**  */
    public Usable<Double> tickEnd;
    /**  */
    public Usable<Double> tickInterval;

    /**  */
    private Interval currentBounds;
    /**  */
    private Interval autoBounds;
    /**  */
    private boolean useAutoBounds;

    /***************************************************************************
     * 
     **************************************************************************/
    public Axis()
    {
        this.title = new TextLabel();
        this.subtitle = new TextLabel();

        this.tickStart = new Usable<>( false, -5.0 );
        this.tickEnd = new Usable<>( false, 5.0 );

        this.tickInterval = new Usable<>( false, 1.0 );

        this.currentBounds = new Interval( -5.0, 5.0 );
        this.autoBounds = new Interval( -5.0, 5.0 );
        this.useAutoBounds = true;

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
        Interval bounds = null;

        if( useAutoBounds )
        {
            bounds = this.autoBounds;
        }
        else
        {
            bounds = this.currentBounds;
        }

        return bounds;
    }

    /***************************************************************************
     * @param bounds
     **************************************************************************/
    public void setBounds( Interval bounds )
    {
        this.useAutoBounds = false;
        this.currentBounds = bounds;
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
        this.useAutoBounds = true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoBounds()
    {
        return useAutoBounds;
    }
}
