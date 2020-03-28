package org.jutils.core.datadist;

/*******************************************************************************
 * Defines a log for data frequency.
 ******************************************************************************/
public class DataRecord
{
    /** The data found. */
    public int data;
    /** The number of times the data was found. */
    public long count;

    /***************************************************************************
     * Creates a new record with the provided data and count.
     * @param data the data found.
     * @param count the number of times the data was found.
     **************************************************************************/
    public DataRecord( int data, long count )
    {
        this.data = data;
        this.count = count;
    }
}
