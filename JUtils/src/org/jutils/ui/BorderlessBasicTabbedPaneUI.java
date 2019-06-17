package org.jutils.ui;

import java.awt.Graphics;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class BorderlessBasicTabbedPaneUI extends BasicTabbedPaneUI
{
    @Override
    protected void paintContentBorderBottomEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }

    @Override
    protected void paintContentBorderRightEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }

    @Override
    protected void paintContentBorderLeftEdge( Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h )
    {
    }
}
