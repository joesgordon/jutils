package jutils.core.ui.app;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScrollPaneFocusListener implements PropertyChangeListener
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        if( !( evt.getNewValue() instanceof JComponent ) )
        {
            return;
        }

        Object focusedObj = evt.getNewValue();

        if( focusedObj instanceof Component )
        {
            Component focused = ( Component )focusedObj;
            Container parent = focused.getParent();

            if( parent instanceof JComponent )
            {
                JComponent focusedParent = ( JComponent )parent;

                focusedParent.scrollRectToVisible( focused.getBounds() );
            }
        }
    }
}
