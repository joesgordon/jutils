package jutils.platform.data;

import java.time.LocalDateTime;

import jutils.core.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialMessage
{
    /**  */
    public final boolean isTransmitted;
    /**  */
    public final LocalDateTime time;
    /**  */
    public final byte [] data;

    /***************************************************************************
     * @param isTransmitted
     * @param data
     **************************************************************************/
    public SerialMessage( boolean isTransmitted, byte [] data )
    {
        this( isTransmitted, data, TimeUtils.getUtcNow() );
    }

    /***************************************************************************
     * @param isTransmitted
     * @param data
     * @param time
     **************************************************************************/
    public SerialMessage( boolean isTransmitted, byte [] data,
        LocalDateTime time )
    {
        this.isTransmitted = isTransmitted;
        this.data = data;
        this.time = time;
    }
}
