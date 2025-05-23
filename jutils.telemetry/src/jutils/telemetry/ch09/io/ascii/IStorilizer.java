package jutils.telemetry.ch09.io.ascii;

/*******************************************************************************
 * Defines a Store Reader.
 * @param <T>
 ******************************************************************************/
public interface IStorilizer<T>
{
    /***************************************************************************
     * @param item
     * @param store
     * @return
     **************************************************************************/
    public void read( T item, AsciiStore store );
}
