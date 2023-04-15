package org.jutils.core.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*******************************************************************************
 * @param <A>
 * @param <B>
 ******************************************************************************/
public class PairList<A, B> implements Iterable<Pair<A, B>>
{
    /**  */
    public final List<Pair<A, B>> pairs;

    /***************************************************************************
     * 
     **************************************************************************/
    public PairList()
    {
        this.pairs = new ArrayList<>();
    }

    /***************************************************************************
     * @param objects
     **************************************************************************/
    public PairList( Object... objects )
    {
        this();

        if( ( objects.length % 2 ) == 1 )
        {
            String err = String.format(
                "List of pairs must have an even number of entries; %s found",
                objects.length );
            throw new IllegalArgumentException( err );
        }

        for( int i = 0; i < objects.length; i += 2 )
        {
            @SuppressWarnings( "unchecked")
            A a = ( A )objects[i];
            @SuppressWarnings( "unchecked")
            B b = ( B )objects[i + 1];

            add( a, b );
        }
    }

    /***************************************************************************
     * @param a
     * @param b
     **************************************************************************/
    public void add( A a, B b )
    {
        pairs.add( new Pair<>( a, b ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Iterator<Pair<A, B>> iterator()
    {
        return pairs.iterator();
    }
}
