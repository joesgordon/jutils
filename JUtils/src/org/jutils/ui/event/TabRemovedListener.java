package org.jutils.ui.event;

import java.awt.Component;
import java.util.EventListener;

/*******************************************************************************
 *
 ******************************************************************************/
public interface TabRemovedListener extends EventListener
{
    /***************************************************************************
     * @param comp Component
     * @param index int
     **************************************************************************/
    void tabRemoved( Component comp, int index );
}
