package org.jutils.ui.event.updater;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ListUpdater<T> implements ListSelectionListener
{
    /**  */
    private IUpdater<List<T>> itemsUpdater;

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public ListUpdater( IUpdater<List<T>> updater )
    {
        this.itemsUpdater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void valueChanged( ListSelectionEvent e )
    {
        if( !e.getValueIsAdjusting() )
        {
            @SuppressWarnings( "unchecked")
            JList<T> list = ( JList<T> )e.getSource();

            List<T> items = new ArrayList<>();

            int [] indexes = list.getSelectedIndices();

            for( int i : indexes )
            {
                items.add( list.getModel().getElementAt( i ) );
            }

            itemsUpdater.update( items );
        }
    }

}
