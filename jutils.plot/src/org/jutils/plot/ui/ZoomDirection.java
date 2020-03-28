package org.jutils.plot.ui;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ZoomDirection
{
    /** Defines a zoom in both the horizontal and vertical directions. */
    BOTH( true, true ),
    /** Defines a zoom in only the horizontal direction. */
    HORIZONTAL( true, false ),
    /** Defines a zoom in only the vertical direction. */
    VERTICAL( false, true );

    /** {@code true} if the zoom direction contains a horizontal component. */
    public final boolean zoomHorizontal;
    /** {@code true} if the zoom direction contains a vertical component. */
    public final boolean zoomVertical;

    /***************************************************************************
     * @param vert
     * @param horiz
     **************************************************************************/
    private ZoomDirection( boolean horiz, boolean vert )
    {
        this.zoomVertical = vert;
        this.zoomHorizontal = horiz;
    }

    /***************************************************************************
     * @param isHoriz
     * @param isVert
     * @return
     **************************************************************************/
    public static ZoomDirection get( boolean isHoriz, boolean isVert )
    {
        if( isHoriz && isVert )
        {
            return BOTH;
        }
        else if( isHoriz )
        {
            return HORIZONTAL;
        }
        else if( isVert )
        {
            return VERTICAL;
        }

        return BOTH;
    }

    /***************************************************************************
     * @param event
     * @return
     **************************************************************************/
    public static ZoomDirection get( ActionEvent event )
    {
        int mods = event.getModifiers();

        boolean ctrl = ( mods & InputEvent.CTRL_DOWN_MASK ) != 0;
        boolean shift = ( mods & InputEvent.SHIFT_DOWN_MASK ) != 0;

        return get( ctrl, shift );
    }

    /***************************************************************************
     * @param e
     * @return
     **************************************************************************/
    public static ZoomDirection get( MouseWheelEvent e )
    {
        int exmod = e.getModifiersEx();

        boolean ctrl = ( MouseWheelEvent.CTRL_DOWN_MASK &
            exmod ) == MouseWheelEvent.CTRL_DOWN_MASK;

        boolean shift = ( MouseWheelEvent.SHIFT_DOWN_MASK &
            exmod ) == MouseWheelEvent.SHIFT_DOWN_MASK;

        return get( ctrl, shift );
    }
}
