package org.jutils.ui.event.updater;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ComboBoxUpdater<T> implements ItemListener
{
    /**  */
    private IUpdater<T> updater;

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public ComboBoxUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void itemStateChanged( ItemEvent event )
    {
        if( event.getStateChange() == ItemEvent.SELECTED )
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )event.getItem();

            updater.update( item );
        }
    }
}
