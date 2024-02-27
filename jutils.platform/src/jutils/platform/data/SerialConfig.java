package jutils.platform.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialConfig
{
    /**  */
    public String comPort;
    /**  */
    public int rxTimeout;
    /**  */
    public SerialParams params;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialConfig()
    {
        this.comPort = "";
        this.params = new SerialParams();
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public SerialConfig( SerialConfig config )
    {
        this();

        set( config );
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void set( SerialConfig config )
    {
        this.comPort = config.comPort;
        this.params.set( config.params );
    }
}
