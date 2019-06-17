package org.jutils.io;

import java.io.File;
import java.util.Comparator;

/*******************************************************************************
 * Defines a {@link Comparator} that sorts files from largest to smallest.
 ******************************************************************************/
public class FileSizeSorter implements Comparator<File>
{
    /**  */
    private final boolean largestToSmallest;

    /***************************************************************************
     * Returns a sorter that orders larger files before smaller files.
     **************************************************************************/
    public FileSizeSorter()
    {
        this( true );
    }

    /***************************************************************************
     * Returns a sorter that orders files according to size as directed by the
     * input parameter.
     * @param largestToSmallest order larger files before smaller files if
     * {@code true}.
     **************************************************************************/
    public FileSizeSorter( boolean largestToSmallest )
    {
        this.largestToSmallest = largestToSmallest;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compare( File thisOne, File thatOne )
    {
        long thisLen = thisOne.length();
        long thatLen = thatOne.length();
        int result = 0;

        if( thisLen < thatLen )
        {
            result = 1;
        }
        else if( thisLen > thatLen )
        {
            result = -1;
        }

        return largestToSmallest ? result : -1 * result;
    }
}
