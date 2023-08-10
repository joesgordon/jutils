package org.jutils.core.pcap;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Pcapng
{
    /**  */
    public static final String PCAPNG_EXT = "pcapng";

    /**  */
    public final List<IBlock> blocks;

    /**
     * 
     */
    public Pcapng()
    {
        this.blocks = new ArrayList<>();
    }
}
