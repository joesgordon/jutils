package org.jutils.plot.data;

import org.jutils.plot.model.IDataPoint;

/*******************************************************************************
 * 
 ******************************************************************************/
public class XYPoint implements IDataPoint
{
    /**  */
    public double x;
    /**  */
    public double y;
    /**  */
    public boolean hidden;
    /**  */
    public boolean selected;

    /***************************************************************************
     * 
     **************************************************************************/
    public XYPoint()
    {
        this( 0, 0 );
    }

    /***************************************************************************
     * @param x
     * @param y
     **************************************************************************/
    public XYPoint( double x, double y )
    {
        this.x = x;
        this.y = y;
        this.hidden = Double.isNaN( x ) || Double.isNaN( y );
        this.selected = false;
    }

    /***************************************************************************
     * @param xy
     **************************************************************************/
    public XYPoint( XYPoint xy )
    {
        this.x = xy.x;
        this.y = xy.y;
        this.hidden = xy.hidden;
        this.selected = false;
    }

    /***************************************************************************
     * @param xy
     **************************************************************************/
    public XYPoint( IDataPoint xy )
    {
        this.x = xy.getX();
        this.y = xy.getY();
        this.hidden = xy.isHidden();
        this.selected = xy.isSelected();
    }

    /***************************************************************************
     * @param xy1
     * @param xy2
     * @param result
     **************************************************************************/
    public static void min( XYPoint xy1, XYPoint xy2, XYPoint result )
    {
        result.x = Math.min( xy1.x, xy2.x );
        result.y = Math.min( xy1.y, xy2.y );
    }

    /***************************************************************************
     * @param xy1
     * @param xy2
     * @param result
     **************************************************************************/
    public static void max( XYPoint xy1, XYPoint xy2, XYPoint result )
    {
        result.x = Math.max( xy1.x, xy2.x );
        result.y = Math.max( xy1.y, xy2.y );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isNan()
    {
        return Double.isNaN( x ) || Double.isNaN( y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getX()
    {
        return x;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public double getY()
    {
        return y;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isSelected()
    {
        return selected;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isHidden()
    {
        return hidden;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelected( boolean selected )
    {
        this.selected = selected;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setHidden( boolean hidden )
    {
        this.hidden = hidden;
    }
}