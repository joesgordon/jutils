package jutils.telemetry.ch10.io.time1;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IrigDayWord2Fields implements IBitField
{
    /**  */
    MINUTES( 0, 3, "Minutes" ),
    /**  */
    MINUTES_10S( 4, 6, "Tens of Minutes" ),
    /**  */
    HOURS( 8, 11, "Hours" ),
    /**  */
    HOURS_10S( 12, 13, "Tens of Hours" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param start
     * @param end
     * @param name
     **************************************************************************/
    private IrigDayWord2Fields( int start, int end, String name )
    {
        this.info = new BitFieldInfo( start, end, name );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getStartBit()
    {
        return info.getStartBit();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getEndBit()
    {
        return info.getEndBit();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public long getMask()
    {
        return info.getMask();
    }
}
