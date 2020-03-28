package org.jutils.chart.ui;

import java.awt.*;

/*******************************************************************************
 * Defines a object that may be drawn on a chart.
 ******************************************************************************/
public interface IChartWidget
{
    /***************************************************************************
     * Calculates the size needed to draw this object with no clipping or loss
     * of data.
     * @param canvasSize the available size for drawing.
     * @return the calculated width/height.
     **************************************************************************/
    public Dimension calculateSize( Dimension canvasSize );

    /***************************************************************************
     * Draws the object to the provided graphics at the x/y and width/height
     * provided.
     * @param graphics the context on which the object is drawn.
     * @param location the position of the top left corner of this object.
     * @param size the width and height of this object.
     **************************************************************************/
    public void draw( Graphics2D graphics, Point location, Dimension size );
}
