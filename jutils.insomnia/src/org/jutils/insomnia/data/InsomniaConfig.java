package org.jutils.insomnia.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class InsomniaConfig
{
    /**
     * The number of milliseconds between user activity before they are declared
     * inactive.
     */
    public long inactiveDelay;
    /**
     * The number of milliseconds a user is inactive before they are declared
     * idle.
     */
    public long inactiveDuration;
    /**
     * The number of milliseconds a user is not active before stimulating this
     * system to show activity.
     */
    public long resetPeriod;

    /***************************************************************************
     * 
     **************************************************************************/
    public InsomniaConfig()
    {
        this.inactiveDelay = 60 * 1000;
        this.inactiveDuration = 2 * 60 * 1000;
        this.resetPeriod = 5 * 60 * 1000;

        // this.inactiveDelay = 5 * 1000;
        // this.inactiveDuration = 6 * 1000;
        // this.resetPeriod = 7 * 1000;
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public InsomniaConfig( InsomniaConfig config )
    {
        this.inactiveDelay = config.inactiveDelay;
        this.inactiveDuration = config.inactiveDuration;
        this.resetPeriod = config.resetPeriod;
    }
}
