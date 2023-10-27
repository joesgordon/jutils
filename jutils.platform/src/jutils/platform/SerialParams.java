package jutils.platform;

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
}
