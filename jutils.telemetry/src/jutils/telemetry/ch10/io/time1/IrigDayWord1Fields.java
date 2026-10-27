package jutils.telemetry.ch10.io.time1;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum IrigDayWord1Fields implements IBitField
{
    /**  */
    MILLIS_10S( 0, 3, "Tens of Milliseconds" ),
    /**  */
    MILLIS_100S( 4, 7, "Hundreds of Milliseconds" ),
    /**  */
    SECONDS( 8, 11, "Seconds" ),
    /**  */
    SECONDS_10S( 12, 14, "Tens of Seconds" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param start
     * @param end
     * @param name
     **************************************************************************/
    private IrigDayWord1Fields( int start, int end, String name )
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
