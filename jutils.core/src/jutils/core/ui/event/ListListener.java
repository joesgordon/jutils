package jutils.core.ui.event;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ListListener implements ListDataListener
{
    /**  */
    private final IListListener listener;

    /***************************************************************************
     * @param listener invoked on any update to the list.
     **************************************************************************/
    public ListListener( IListListener listener )
    {
        this.listener = listener;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void intervalAdded( ListDataEvent e )
    {
        listener.onUpdate( e.getType(), e.getIndex0(), e.getIndex1() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void intervalRemoved( ListDataEvent e )
    {
        listener.onUpdate( e.getType(), e.getIndex0(), e.getIndex1() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void contentsChanged( ListDataEvent e )
    {
        listener.onUpdate( e.getType(), e.getIndex0(), e.getIndex1() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IListListener
    {
        /**
         * @param type see {@link ListDataEvent#getType()}.
         * @param index0 see {@link ListDataEvent#getIndex0()}.
         * @param index1 see {@link ListDataEvent#getIndex1()}.
         */
        public void onUpdate( int type, int index0, int index1 );
    }
}
