package jutils.core.laf;

import javax.swing.JComponent;
import javax.swing.JToolBar;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LafUtils
{

    /***************************************************************************
     * Returns true if the specified widget is in a toolbar.
     * @param c
     * @return
     **************************************************************************/
    public static boolean isToolBarButton( JComponent c )
    {
        return ( c.getParent() instanceof JToolBar );
    }
}
