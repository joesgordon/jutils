package org.duak.utils;

import java.util.Comparator;

/*******************************************************************************
 *
 ******************************************************************************/
public class LongComparer implements Comparator<Long>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compare( Long thisLong, Long thatLong )
    {
        return compareLong( thisLong, thatLong );
    }

    /***************************************************************************
     * @param thisLong
     * @param thatLong
     * @return
     **************************************************************************/
    public static int compareLong( Long thisLong, Long thatLong )
    {
        long diff = thisLong - thatLong;

        if( diff < 0 )
        {
            return -1;
        }
        else if( diff > 0 )
        {
            return 1;
        }

        return 0;
    }
}
