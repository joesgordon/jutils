package org.jutils.core.data;

/**
 *
 */
public interface IBitFlag extends IBitField
{
    /***************************************************************************
     * Returns the 0-relative bit this flag refers to. Bits 0 and 3 are set in
     * the value {@code 0x09}.
     * @return the bit this flag is refers to.
     **************************************************************************/
    public int getBit();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public default int getStartBit()
    {
        return getBit();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public default int getEndBit()
    {
        return getBit();
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends IBitFlag> String getStatuses( byte word,
        T [] flags )
    {
        return getStatuses( word & IBitField.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends IBitFlag> String getStatuses( short word,
        T [] flags )
    {
        return getStatuses( word & IBitField.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends IBitFlag> String getStatuses( int word,
        T [] flags )
    {
        return getStatuses( word & IBitField.INT_MASK, flags );
    }

    /***************************************************************************
     * @param <T>
     * @param word
     * @param flags
     * @return
     **************************************************************************/
    public static <T extends IBitFlag> String getStatuses( long word,
        T [] flags )
    {
        StringBuilder stss = new StringBuilder();

        for( IBitFlag def : flags )
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
