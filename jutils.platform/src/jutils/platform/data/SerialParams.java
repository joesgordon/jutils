package jutils.platform.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SerialParams
{
    /**  */
    public boolean binaryModeEnabled;
    /**  */
    public int baudRate;
    /**  */
    public WordSize size;
    /**  */
    public Parity parity;
    /**  */
    public StopBits stopBits;
    /** Clear-to-Send */
    public boolean ctsEnabled;
    /** Data-Set-Ready */
    public boolean dsrEnabled;
    /** Data-Terminal-Ready */
    public DtrControl dtrControl;
    /** Request-to-Send */
    public RtsControl rtsControl;
    /** Software flow control xon/xoff output */
    public boolean swFlowOutputEnabled;
    /** Software flow control xon/xoff input */
    public boolean swFlowInputEnabled;

    /***************************************************************************
     * 
     **************************************************************************/
    public SerialParams()
    {
        this.binaryModeEnabled = false;
        this.baudRate = BaudRate.B19200.value;
        this.size = WordSize.EIGHT;
        this.parity = Parity.NONE;
        this.stopBits = StopBits.ONE;
        this.ctsEnabled = false;
        this.dsrEnabled = false;
        this.dtrControl = DtrControl.DISABLE;
        this.rtsControl = RtsControl.DISABLE;
        this.swFlowOutputEnabled = false;
        this.swFlowInputEnabled = false;
    }

    /***************************************************************************
     * @param params
     **************************************************************************/
    public SerialParams( SerialParams params )
    {
        this();

        set( params );
    }

    /***************************************************************************
     * @param params
     **************************************************************************/
    public void set( SerialParams params )
    {
        this.binaryModeEnabled = params.binaryModeEnabled;
        this.baudRate = params.baudRate;
        this.size = params.size;
        this.parity = params.parity;
        this.stopBits = params.stopBits;
        this.ctsEnabled = params.ctsEnabled;
        this.dsrEnabled = params.dsrEnabled;
        this.dtrControl = params.dtrControl;
        this.rtsControl = params.rtsControl;
        this.swFlowOutputEnabled = params.swFlowOutputEnabled;
        this.swFlowInputEnabled = params.swFlowInputEnabled;
    }
}
