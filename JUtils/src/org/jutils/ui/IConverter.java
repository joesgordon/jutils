package org.jutils.ui;

/*******************************************************************************
 * @param <A> Type to be converted from.
 * @param <B> Type to be converted to.
 ******************************************************************************/
public interface IConverter<A, B>
{
    /***************************************************************************
     * Converts {@code from} to {@code to}, setting {@code to} if possible.
     * @param from quantity to convert from.
     * @param to quantity to convert to (filled if mutable).
     * @return the value converted to.
     **************************************************************************/
    public B convFrom( A from, B to );

    /***************************************************************************
     * Converts {@code to} to {@code from}, setting {@code from} if possible.
     * @param from quantity to convert from.
     * @param to quantity to convert to (filled if mutable).
     * @return the value converted to.
     **************************************************************************/
    public A convTo( B from, A to );
}
