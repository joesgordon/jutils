package org.jutils.core.laf;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleToolBarUI extends MetalToolBarUI
{
    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleToolBarUI()
    {
        super();
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public static ComponentUI createUI( JComponent c )
    {
        SimpleToolBarUI tbui = new SimpleToolBarUI();

        return tbui;
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    public void installUI( JComponent c )
    {
        super.installUI( c );

        JToolBar bar = ( JToolBar )c;

        super.setRolloverBorders( true );

        bar.setRollover( true );
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected Border createRolloverBorder()
    {
        return new SimpleButtonBorder();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    protected Border createNonRolloverBorder()
    {
        return new SimpleButtonBorder();
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void setBorderToNormal( Component c )
    {
    }

    /***************************************************************************
     * @{@inheritDoc}
     **************************************************************************/
    @Override
    protected void setBorderToRollover( Component c )
    {
    }
}
