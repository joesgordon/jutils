package org.jutils.plot.ui;

import java.awt.Graphics2D;

import org.jutils.plot.ui.objects.PlotContext;

/**
 * 
 */
public interface IPlotDrawer
{
    /**
     * @param context
     * @param graphics
     */
    public void draw( PlotContext context, Graphics2D graphics );
}
