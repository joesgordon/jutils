package jutils.core.utils;

/*******************************************************************************
 * @param <F>
 * @param <S>
 ******************************************************************************/
public class Tuple2<F, S>
{
    /**  */
    public final F first;
    /**  */
    public final S second;

    /***************************************************************************
     * @param first
     * @param second
     **************************************************************************/
    public Tuple2( F first, S second )
    {
        this.first = first;
        this.second = second;
    }
}
