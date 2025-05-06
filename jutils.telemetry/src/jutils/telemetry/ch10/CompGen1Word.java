package jutils.telemetry.ch10;

import jutils.core.data.BitFieldInfo;
import jutils.core.data.IBitField;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum CompGen1Word implements IBitField
{
    /**  */
    RCC_VERSION( 0, 7, "RCC Version" ),
    /**  */
    SETUP_RECORD_CONFIG_CHANGE( 8, "Setup Record Configuration Change" ),
    /**  */
    FORMAT( 9, "Format" ),
    /**  */
    RESERVED( 10, 31, "Reserved" ),;

    /**  */
    private final BitFieldInfo info;

    /***************************************************************************
     * @param bit
     * @param name
     **************************************************************************/
    private CompGen1Word( int bit, String name )
    {
        this( bit, bit, name );
    }

    /***************************************************************************
     * @param startBit
     * @param endBit
     * @param name
     **************************************************************************/
    private CompGen1Word( int startBit, int endBit, String name )
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
