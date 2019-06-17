package org.jutils.chart.model;

import java.awt.Graphics2D;
import java.awt.Point;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IMarker
{
    /***************************************************************************
     * @param graphics
     * @param location
     **************************************************************************/
    public void draw( Graphics2D graphics, Point location );
}
