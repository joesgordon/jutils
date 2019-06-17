package org.jutils.chart.model;

import java.awt.Graphics2D;
import java.awt.Point;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ILine
{
    /***************************************************************************
     * @param graphics
     * @param from
     * @param to
     **************************************************************************/
    public void draw( Graphics2D graphics, Point from, Point to );
}
