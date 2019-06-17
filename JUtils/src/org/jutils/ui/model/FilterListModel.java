package org.jutils.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FilterListModel<T> implements ListModel<T>
{
    /**  */
    private final ListModel<T> model;
    /**  */
    private final List<Integer> indexes;

    /**  */
    private final List<ListDataListener> dataListeners;

    /**  */
    private IListFilter<T> filter;

    /***************************************************************************
     * @param model
     **************************************************************************/
    public FilterListModel( ListModel<T> model )
    {
        this.model = model;
        this.indexes = new ArrayList<>();
        this.dataListeners = new ArrayList<>();

        this.filter = new PassFilter<>();

        model.addListDataListener( new ListListener<T>( this ) );
    }

    /***************************************************************************
     * @param filter
     **************************************************************************/
    public void setFilter( IListFilter<T> filter )
    {
        if( filter == null )
        {
            filter = new PassFilter<>();
        }

        this.filter = filter;

        updateIndexes();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateIndexes()
    {
        indexes.clear();

        int count = model.getSize();
        for( int i = 0; i < count; i++ )
        {
            T element = model.getElementAt( i );
            if( filter.accept( element ) )
            {
                indexes.add( i );
            }
        }

        fireContentsChanged( 0, indexes.size() );
    }

    /***************************************************************************
     * @param index0
     * @param index1
     **************************************************************************/
    private void fireContentsChanged( int index0, int index1 )
    {
        ListDataEvent e = new ListDataEvent( this,
            ListDataEvent.CONTENTS_CHANGED, index0, index1 );

        for( ListDataListener l : dataListeners )
        {
            l.contentsChanged( e );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getSize()
    {
        return indexes.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getElementAt( int index )
    {
        return model.getElementAt( indexes.get( index ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addListDataListener( ListDataListener l )
    {
        dataListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeListDataListener( ListDataListener l )
    {
        dataListeners.remove( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IListFilter<T>
    {
        public boolean accept( T item );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PassFilter<T> implements IListFilter<T>
    {
        @Override
        public boolean accept( T item )
        {
            return true;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ListListener<T> implements ListDataListener
    {
        private final FilterListModel<T> model;

        public ListListener( FilterListModel<T> model )
        {
            this.model = model;
        }

        @Override
        public void intervalAdded( ListDataEvent e )
        {
            model.updateIndexes();
        }

        @Override
        public void intervalRemoved( ListDataEvent e )
        {
            model.updateIndexes();
        }

        @Override
        public void contentsChanged( ListDataEvent e )
        {
            model.updateIndexes();
        }
    }
}
