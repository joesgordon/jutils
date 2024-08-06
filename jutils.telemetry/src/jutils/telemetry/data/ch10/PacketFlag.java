package jutils.telemetry.data.ch10;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.INamedBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum PacketFlag implements INamedBitField
{
    /**  */
    CHECKSUM_PRESENT( 0, 1, "Checksum Present" ),
    /**  */
    SEC_HDR_TIME_FMT( 2, 3, "Secondary Header Time Format" ),
    /**  */
    DATA_OVERFLOW( 4, "Data Overflow" ),
    /**  */
    RTC_SYNC_ERROR( 5, "RTC Sync Error" ),
    /**  */
    IPTS_TIME_SOURCE( 6, "IPTS Time Source" ),
    /**  */
    SEC_HDR_PRESENT( 7, "Secondary Header Present" ),;

    /**  */
    private BitFieldInfo info;

    /***************************************************************************
     * @param bit
     * @param name
     **************************************************************************/
    private PacketFlag( int bit, String name )
    {
        this( bit, bit, name );
    }

    /***************************************************************************
     * @param startBit
     * @param endBit
     * @param name
     **************************************************************************/
    private PacketFlag( int startBit, int endBit, String name )
    {
        this.info = new BitFieldInfo( startBit, endBit, name );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return info.getName();
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
