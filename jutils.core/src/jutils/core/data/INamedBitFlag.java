package jutils.core.data;

import jutils.core.INamedItem;
import jutils.core.utils.BitMasks;

/**
 *
 */
public interface INamedBitFlag extends IBitFlag, INamedItem
{
    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends INamedBitFlag> String getStatuses( byte word,
        T [] flags )
    {
        return getStatuses( word & BitMasks.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends INamedBitFlag> String getStatuses( short word,
        T [] flags )
    {
        return getStatuses( word & BitMasks.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends INamedBitFlag> String getStatuses( int word,
        T [] flags )
    {
        return getStatuses( word & BitMasks.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends INamedBitFlag> String getStatuses( long word,
        T [] flags )
    {
        StringBuilder stss = new StringBuilder();

        for( INamedBitFlag def : flags )
        {
            if( def.getFlag( word ) )
            {
                if( stss.length() > 0 )
                {
                    stss.append( ',' );
                }

                stss.append( def.getName() );
            }
        }

        return stss.toString();
    }
}
