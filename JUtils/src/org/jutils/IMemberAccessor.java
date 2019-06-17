package org.jutils;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Defines a method of returning a member of an object explicitly (without
 * reflection).
 * @param <T> the type of the object containing the member to be accessed.
 * @param <M> the type of the member to be accessed.
 ******************************************************************************/
public interface IMemberAccessor<T, M>
{
    /***************************************************************************
     * Returns the member from the provided item.
     * @param item the object instance containing the member.
     * @return the member instance.
     **************************************************************************/
    public M get( T item );

    /***************************************************************************
     * Creates a list of all members returned by the provided accessor by each
     * item in the provided list.
     * @param <T> the type of the object containing the member to be accessed.
     * @param <M> the type of the member to be accessed.
     * @param items the list containing the object instances.
     * @param ima the accessor to the member instances desired.
     * @return the list of members.
     **************************************************************************/
    public static <T, M> List<M> getMemberList( List<T> items,
        IMemberAccessor<T, M> ima )
    {
        List<M> members = new ArrayList<>( items.size() );

        for( T item : items )
        {
            members.add( ima.get( item ) );
        }

        return members;
    }
}
