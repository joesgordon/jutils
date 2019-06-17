package org.jutils.ui.model;

import java.util.*;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jutils.ListUtils;
import org.jutils.io.IStringWriter;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemComboBoxModel<T> implements List<T>, MutableComboBoxModel<T>
{
    /**  */
    private final List<ListDataListener> ldListeners;
    /**  */
    private final boolean autoAdd;
    /**  */
    private final List<T> items;
    /**  */
    private int selectedIndex;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemComboBoxModel()
    {
        this( new ArrayList<T>(), true );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ItemComboBoxModel( T [] items )
    {
        this( Collections.unmodifiableList( Arrays.asList( items ) ) );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ItemComboBoxModel( List<T> items )
    {
        this( items, true );
    }

    /***************************************************************************
     * @param items
     * @param autoAdd
     **************************************************************************/
    public ItemComboBoxModel( List<T> items, boolean autoAdd )
    {
        this.items = new ArrayList<T>( items );
        this.autoAdd = autoAdd;

        this.ldListeners = new ArrayList<ListDataListener>();
        this.selectedIndex = -1;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemComboBoxModel( Iterable<T> items )
    {
        this( ListUtils.asList( items ) );
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalChanged( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.CONTENTS_CHANGED, idx0, idx1 );

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.contentsChanged( event );
        }
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalAdded( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.INTERVAL_ADDED, idx0, idx1 );

        if( event.getIndex0() <= selectedIndex )
        {
            selectedIndex += ( event.getIndex1() - event.getIndex0() + 1 );
        }

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.intervalAdded( event );
        }
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalRemoved( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.INTERVAL_REMOVED, idx0, idx1 );

        if( event.getIndex1() < selectedIndex )
        {
            selectedIndex -= ( event.getIndex1() - event.getIndex0() + 1 );
        }
        else if( event.getIndex0() <= selectedIndex &&
            selectedIndex <= event.getIndex1() )
        {
            if( event.getIndex0() < getSize() )
            {
                selectedIndex = event.getIndex0();
            }
            else
            {
                selectedIndex = getSize() - 1;
            }
        }

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.intervalRemoved( event );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getSelectedItem()
    {
        return selectedIndex > -1 ? items.get( selectedIndex ) : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelectedItem( Object anItem )
    {
        try
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )anItem;

            if( item != null && item.toString().length() > 0 )
            {
                selectedIndex = items.indexOf( item );

                if( selectedIndex < 0 && autoAdd )
                {
                    items.add( item );
                    selectedIndex = items.indexOf( item );
                }
            }
            else
            {
                selectedIndex = -1;
            }
            fireIntervalChanged( -1, -1 );
        }
        catch( ClassCastException ex )
        {
            ex.printStackTrace();
            throw new IllegalArgumentException(
                "Item is not of type 'T': " + anItem.toString(), ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addListDataListener( ListDataListener l )
    {
        ldListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeListDataListener( ListDataListener l )
    {
        if( !ldListeners.remove( l ) )
        {
            throw new IllegalArgumentException(
                "Listener not found: " + l.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getElementAt( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getSize()
    {
        return items.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean add( T e )
    {
        int index = items.size();
        boolean result = items.add( e );

        if( result )
        {
            fireIntervalAdded( index, index );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void add( int index, T element )
    {
        items.add( index, element );

        fireIntervalAdded( index, index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( Collection<? extends T> c )
    {
        int begin = items.size();
        boolean result = items.addAll( c );
        int end = begin + c.size();

        if( result )
        {
            fireIntervalAdded( begin, end );
        }

        return result;
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        int prevSize = this.items.size();

        clear();
        if( prevSize > 0 )
        {
            fireIntervalRemoved( 0, prevSize );
        }

        this.items.addAll( items );
        if( items.size() > 0 )
        {
            fireIntervalAdded( 0, this.items.size() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getItems()
    {
        return new ArrayList<>( items );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( int index, Collection<? extends T> c )
    {
        int begin = index;
        boolean result = items.addAll( index, c );
        int end = begin + c.size();

        if( result )
        {
            fireIntervalAdded( begin, end );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void clear()
    {
        int begin = 0;
        int end = items.size() - 1;
        items.clear();

        if( end > -1 )
        {
            fireIntervalRemoved( begin, end );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean contains( Object o )
    {
        return items.contains( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean containsAll( Collection<?> c )
    {
        return items.containsAll( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T get( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int indexOf( Object o )
    {
        return items.indexOf( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isEmpty()
    {
        return items.isEmpty();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return items.iterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int lastIndexOf( Object o )
    {
        return items.lastIndexOf( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator()
    {
        return items.listIterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator( int index )
    {
        return items.listIterator( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean remove( Object o )
    {
        int index = items.indexOf( o );
        boolean result = items.remove( o );

        if( result )
        {
            fireIntervalRemoved( index, index );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T remove( int index )
    {
        T t = items.remove( index );

        if( t != null )
        {
            fireIntervalRemoved( index, index );
        }

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean removeAll( Collection<?> c )
    {
        return items.removeAll( c );

        // TODO finish writing class by firing listeners.
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean retainAll( Collection<?> c )
    {
        return items.retainAll( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T set( int index, T element )
    {
        return items.set( index, element );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int size()
    {
        return items.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<T> subList( int fromIndex, int toIndex )
    {
        return items.subList( fromIndex, toIndex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object [] toArray()
    {
        return items.toArray();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public <I> I [] toArray( I [] a )
    {
        return items.toArray( a );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    @SuppressWarnings( "unchecked")
    public void addElement( Object obj )
    {
        boolean result = add( ( T )obj );

        if( !result )
        {
            throw new IllegalArgumentException(
                "Unable to add " + obj.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    @SuppressWarnings( "unchecked")
    public void insertElementAt( Object obj, int index )
    {
        add( index, ( T )obj );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeElement( Object obj )
    {
        boolean result = remove( obj );

        if( !result )
        {
            throw new IllegalArgumentException(
                "Unable to remove " + obj.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeElementAt( int index )
    {
        remove( index );
    }

    /***************************************************************************
     * @param item
     * @param list
     * @return
     **************************************************************************/
    public static <R> ItemWrapper<R> findItem( R item,
        Iterable<ItemWrapper<R>> list )
    {
        for( ItemWrapper<R> wrapper : list )
        {
            if( wrapper.item == item )
            {
                return wrapper;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param items
     * @param serializer
     * @return
     **************************************************************************/
    public static <R> List<ItemWrapper<R>> createListWithNull( List<R> items,
        IStringWriter<R> serializer )
    {
        List<ItemWrapper<R>> wrappedItems = new ArrayList<ItemWrapper<R>>();

        ItemWrapper<R> wrapper = new ItemWrapper<R>( null, serializer );

        wrappedItems.add( wrapper );

        for( R item : items )
        {
            wrapper = new ItemWrapper<R>( item, serializer );
            wrappedItems.add( wrapper );
        }

        return wrappedItems;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class ItemWrapper<R>
    {
        public final R item;
        public final IStringWriter<R> serializer;

        public ItemWrapper( R item, IStringWriter<R> serializer )
        {
            this.item = item;
            this.serializer = serializer;
        }

        @Override
        public String toString()
        {
            return item == null ? "" : serializer.toString( item );
        }

        @Override
        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }
            else if( obj instanceof ItemWrapper )
            {
                ItemWrapper<?> wrapper = ( ItemWrapper<?> )obj;
                if( item == null )
                {
                    if( wrapper.item == null )
                    {
                        return true;
                    }

                    return false;
                }

                return item.equals( wrapper.item );
            }

            return item.equals( obj );
        }

        @Override
        public int hashCode()
        {
            return item.hashCode();
        }
    }
}
