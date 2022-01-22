package org.jutils.core.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JUtilsTabbedPaneUI extends BasicTabbedPaneUI
{
    /***************************************************************************
     * 
     **************************************************************************/
    public JUtilsTabbedPaneUI()
    {
        super();
    }

    /***************************************************************************
     * @param c
     * @return
     **************************************************************************/
    public static ComponentUI createUI( JComponent c )
    {
        return new JUtilsTabbedPaneUI();
    }
}
