package jutils.core.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SimpleScrollBarUI extends BasicScrollBarUI
{
    /***************************************************************************
     * 
     **************************************************************************/
    public SimpleScrollBarUI()
    {
        super();
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public static ComponentUI createUI( JComponent c )
    {
        return new SimpleScrollBarUI();
    }
}
