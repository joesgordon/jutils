package jutils.telemetry.data.ch10;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Pcm1Word implements IBitField
{
    /**
     * 18-bit binary value representing the word offset into the major frame for
     * the first data word in the packet. The sync offset is not applicable for
     * packed or throughput mode.
     */
    SYNC_OFFSET( 0, 17, "SYNC Offset" ),
    /**  */
    UNPACKED_MODE( 18, "Unpacked Mode" ),
    /**  */
    PACKED_MODE( 19, "Packed Mode" ),
    /**  */
    THROUGHPUT_MODE( 20, "Throughput Mode" ),
    /**  */
    ALIGNMENT_MODE( 21, "Alignment Mode" ),
    /**  */
    MODE_RESERVED( 22, 23, "Mode - Reserved" ),
    /**  */
    MAJOR_LOCK_STATUS( 24, 25, "Major Lock Status" ),
    /**  */
    MINOR_LOCK_STATUS( 25, 26, "Minor Lock Status" ),
    /**  */
    MINOR_INDICATOR( 28, "Minor Frame Indicator" ),
    /**  */
    MAJOR_INDICATOR( 29, "Major Frame Indicator" ),
    /**  */
    IPH_INDICATOR( 30, "Intra-Packet Header Indicator" ),
    /**  */
    RESERVED( 31, "Reserved" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param bit
     * @param name
     **************************************************************************/
    private Pcm1Word( int bit, String name )
    {
        this( bit, bit, name );
    }

    /***************************************************************************
     * @param startBit
     * @param endBit
     * @param name
     **************************************************************************/
    private Pcm1Word( int startBit, int endBit, String name )
    {
        this.info = new BitFieldInfo( startBit, endBit, name );
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
