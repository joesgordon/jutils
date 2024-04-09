package jutils.iris.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChannelConfig
{
    /**  */
    public static final int MAX_BIT_DEPTH = 16;

    /**  */
    public int bitDepth;
    /**  */
    public String name;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChannelConfig()
    {
        this.bitDepth = 8;
        this.name = "Mono";
    }

    /***************************************************************************
     * @param bitDepth
     * @param name
     **************************************************************************/
    public ChannelConfig( int bitDepth, String name )
    {
        this.bitDepth = bitDepth;
        this.name = name;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public ChannelConfig( ChannelConfig config )
    {
        this.bitDepth = config.bitDepth;
        this.name = config.name;
    }

    /***************************************************************************
     * @param bitDepth
     * @param name
     **************************************************************************/
    public void set( int bitDepth, String name )
    {
        this.bitDepth = bitDepth;
        this.name = name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMaxChannelValue()
    {
        return getChannelValueCount() - 1;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getChannelValueCount()
    {
        return 1 << bitDepth;
    }
}
