package org.jutils.datadist;

import java.util.*;
import java.util.Map.Entry;

import org.jutils.io.ByteUtils;

/*******************************************************************************
 * Creates an object to build data distributions over large amounts of data. In
 * order to reduce memory overhead, this class uses a naive algorithm that
 * assumes that data is uniformly distributed throughout the file. Data is read
 * as overlapping integers read from each byte index.
 ******************************************************************************/
public class DistributionBuilder
{
    /**
     * The latest findings limited to the user provided count after each data
     * addition.
     */
    private final Map<Integer, Long> findings;
    /** The maximum number of records to retain after each addition. */
    private final int recordLimit;

    /***************************************************************************
     * Creates a new builder limited to 100 records.
     **************************************************************************/
    public DistributionBuilder()
    {
        this( 100 );
    }

    /***************************************************************************
     * Creates a new builder limited to the provided number of records.
     * @param recordLimit the maximum number of records to retain after each
     * addition.
     **************************************************************************/
    public DistributionBuilder( int recordLimit )
    {
        this.findings = new HashMap<>( recordLimit );
        this.recordLimit = recordLimit;
    }

    /***************************************************************************
     * Adds the data in the provided buffer to the distribution and limits the
     * number of records to the user provided value.
     * @param buffer the bytes to be added.
     **************************************************************************/
    public void addData( byte [] buffer )
    {
        addData( buffer, 0, buffer.length );
    }

    /***************************************************************************
     * Adds the data in the provided buffer to the distribution and limits the
     * number of records to the user provided value.
     * @param buffer the bytes to be added.
     * @param index the beginning index to start reading data.
     * @param length the number of bytes to be read.
     **************************************************************************/
    public void addData( byte [] buffer, int index, int length )
    {
        int cnt = length - 3;

        if( !findings.isEmpty() )
        {
            DataDistribution dd = buildDistribution();

            findings.clear();

            for( DataRecord dr : dd.records )
            {
                findings.put( dr.data, dr.count );
            }
        }

        for( int i = index; i < cnt; i++ )
        {
            int value = ByteUtils.getInteger( buffer, i );

            addValue( value );
        }
    }

    /***************************************************************************
     * Adds the integer read to the distribution.
     * @param value the data to be added.
     **************************************************************************/
    public void addValue( int value )
    {
        Long cnt = findings.get( value );
        cnt = cnt == null ? 1 : cnt + 1;
        findings.put( value, cnt );
    }

    /***************************************************************************
     * Builds a distribution of cataloged data sorted by count and limited to
     * the user provided number of records..
     * @return the distribution built.
     **************************************************************************/
    public DataDistribution buildDistribution()
    {
        Set<Entry<Integer, Long>> set = findings.entrySet();
        DataDistribution dd = new DataDistribution( set.size() );

        for( Entry<Integer, Long> e : set )
        {
            dd.records.add( new DataRecord( e.getKey(), e.getValue() ) );
        }

        // LogUtils.printInfo( "\t\tClearing: %d", dd.records.size() );
        findings.clear();

        // LogUtils.printInfo( "\t\tSorting" );
        Collections.sort( dd.records, new CountComparator() );

        if( dd.records.size() > recordLimit )
        {
            dd.records.subList( recordLimit, dd.records.size() ).clear();
        }

        return dd;
    }

    /***************************************************************************
     * {@link Comparator} used to sort {@link DataRecord}s.
     **************************************************************************/
    private static class CountComparator implements Comparator<DataRecord>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare( DataRecord this1, DataRecord that1 )
        {
            int cmp = Long.compare( that1.count, this1.count );
            return cmp != 0 ? cmp : Integer.compare( this1.data, that1.data );
        }
    }
}
