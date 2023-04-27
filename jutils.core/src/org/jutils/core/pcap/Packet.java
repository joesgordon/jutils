package org.jutils.core.pcap;

/*******************************************************************************
 * The Packet Block is obsolete, and MUST NOT be used in new files. Use the
 * Enhanced Packet Block or Simple Packet Block instead.
 ******************************************************************************/
public class Packet extends IBlock
{
    /***************************************************************************
     * 
     **************************************************************************/
    public Packet()
    {
        super( BlockType.PACKET );
    }
}
