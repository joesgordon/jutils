package org.jutils.core.laf;

import java.awt.Graphics;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BorderlessTabbedPaneUI extends JUtilsTabbedPaneUI
{
    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void paintContentBorderBottomEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void paintContentBorderRightEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void paintContentBorderLeftEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }
}
