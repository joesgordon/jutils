package jutils.plot.ui;

import java.awt.Graphics2D;

import jutils.plot.ui.objects.PlotContext;

/*******************************************************************************
 * Defines a method for drawing on the provided graphics layer of a plot.
 ******************************************************************************/
public interface IPlotDrawer
{
    /***************************************************************************
     * Callback to draw on the provided graphics layer.
     * @param context
     * @param graphics
     **************************************************************************/
    public void draw( PlotContext context, Graphics2D graphics );
}
