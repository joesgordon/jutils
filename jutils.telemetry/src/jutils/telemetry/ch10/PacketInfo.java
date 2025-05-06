package jutils.telemetry.ch10;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketInfo
{
    /**  */
    public final long position;
    /**  */
    public final Packet packet;
    /** . */
    public final byte [] data;

    /***************************************************************************
     * @param position
     * @param packet
     **************************************************************************/
    public PacketInfo( long position, Packet packet )
    {
        this.position = position;
        this.packet = packet;
        this.data = new byte[packet.header.packetLength];
    }
}
