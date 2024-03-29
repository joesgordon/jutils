package jutils.duak.utils;

import java.util.LinkedList;

/*******************************************************************************
 * @param <E>
 ******************************************************************************/
public class HistoryList<E>
{
    /**  */
    private LinkedList<E> history;
    /**  */
    private int current;

    /***************************************************************************
     * 
     **************************************************************************/
    public HistoryList()
    {
        history = new LinkedList<E>();
        current = -1;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean hasPrevious()
    {
        return current > 0;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean hasNext()
    {
        return current < history.size() - 1;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public E next()
    {
        return history.get( ++current );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public E previous()
    {
        return history.get( --current );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        history.clear();
        current = -1;
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    public void add( E e )
    {
        for( int i = current + 1; i < history.size(); )
        {
            history.remove( i );
        }

        history.add( e );
        current++;
    }
}
