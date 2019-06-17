package org.jutils.datadist;

import java.util.ArrayList;
import java.util.List;

import org.jutils.io.StringPrintStream;

/*******************************************************************************
 * Defines an object to store the distribution of data found.
 ******************************************************************************/
public class DataDistribution
{
    /** The most frequent data found. */
    public final List<DataRecord> records;

    /***************************************************************************
     * Creates a data distribution with an initial capacity.
     * @param size the initial capacity of the records list.
     **************************************************************************/
    public DataDistribution( int size )
    {
        this.records = new ArrayList<>( size );
    }

    /***************************************************************************
     * Builds a multi-line description of the distribution for logging/viewing.
     * @return the multi-line description.
     **************************************************************************/
    public String getDescription()
    {
        String desc = null;

        try( StringPrintStream stream = new StringPrintStream() )
        {
            for( int i = 0; i < records.size(); i++ )
            {
                DataRecord dr = records.get( i );
                stream.println( "#%03d %08X: %d", i + 1, dr.data, dr.count );
            }

            desc = stream.toString();
        }

        return desc;
    }
}
