package jutils.telemetry.ch10.io.time1;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IrigDayWord3Fields implements IBitField
{
    /**  */
    DAYS( 0, 3, "Days" ),
    /**  */
    DAYS_10S( 4, 7, "Tens of Days" ),
    /**  */
    DAYS_100S( 8, 9, "Hundreds of Days" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param start
     * @param end
     * @param name
     **************************************************************************/
    private IrigDayWord3Fields( int start, int end, String name )
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
