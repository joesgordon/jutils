package jutils.telemetry.data.ch10;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum Time1Word implements IBitField
{
    /**  */
    SOURCE( 0, 3, "Time Source" ),
    /**  */
    FORMAT( 4, 7, "Time Format" ),
    /**  */
    DATE( 8, 11, "Date Format" ),
    /**  */
    ITS( 12, 15, "IRIG Time Source" ),
    /**  */
    RESERVED( 16, 31, "Reserved" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param bit
     * @param name
     **************************************************************************/
    private Time1Word( int bit, String name )
    {
        this( bit, bit, name );
    }

    /***************************************************************************
     * @param startBit
     * @param endBit
     * @param name
     **************************************************************************/
    private Time1Word( int startBit, int endBit, String name )
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
