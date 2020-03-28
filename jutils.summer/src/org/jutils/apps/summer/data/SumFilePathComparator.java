package org.jutils.apps.summer.data;

import java.util.Comparator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SumFilePathComparator implements Comparator<SumFile>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compare( SumFile thisOne, SumFile thatOne )
    {
        boolean isThisDir = thisOne.path.indexOf( '/' ) > -1;
        boolean isThatDir = thatOne.path.indexOf( '/' ) > -1;

        if( isThisDir && !isThatDir )
        {
            return -1;
        }
        else if( !isThisDir && isThatDir )
        {
            return 1;
        }

        return thisOne.file.compareTo( thatOne.file );
    }
}
