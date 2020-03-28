package org.jutils.apps.summer.data;

import java.util.Comparator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SumFileSizeComparator implements Comparator<SumFile>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compare( SumFile thisOne, SumFile thatOne )
    {
        long sizeDiff = thisOne.length - thatOne.length;

        return sizeDiff < 0 ? 1 : sizeDiff > 0 ? -1 : 0;
    }
}
