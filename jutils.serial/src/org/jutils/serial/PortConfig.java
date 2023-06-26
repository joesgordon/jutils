package org.jutils.serial;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PortConfig
{
    /**  */
    public static final int XLIMIT_MIN = 0;
    /**  */
    public static final int XLIMIT_MAX = 65535;

    /**  */
    public int baudRate;
    /**  */
    public Parity parity;
    /**  */
    public WordSize wordSize;
    /**  */
    public StopBits stopBits;

    /**  */
    public boolean ctsOutputControl;
    /**  */
    public boolean dsrOutputControl;
    /** DSR Sensitivity */
    public boolean dsrInputControl;
    /**  */
    public DtrControl dtrControl;
    /**  */
    public RtsControl rtsControl;

    /**  */
    public boolean softwareOutputControl;
    /**  */
    public boolean softwareInputControl;
    /**
     * The minimum number of bytes in use allowed in the input buffer before
     * flow control is activated to allow transmission by the sender. This
     * assumes that either XON/XOFF, RTS, or DTR input flow control is specified
     * in the fInX, fRtsControl, or fDtrControl members.
     */
    public int xOnLimit;
    /**
     * The minimum number of free bytes allowed in the input buffer before flow
     * control is activated to inhibit the sender. Note that the sender may
     * transmit characters after the flow control signal has been activated, so
     * this value should never be zero. This assumes that either XON/XOFF, RTS,
     * or DTR input flow control is specified in the fInX, fRtsControl, or
     * fDtrControl members. The maximum number of bytes in use allowed is
     * calculated by subtracting this value from the size, in bytes, of the
     * input buffer.
     */
    public int xOffLimit;
    /**  */
    public byte xOnChar;
    /**  */
    public byte xOffChar;

    /***************************************************************************
     * 
     **************************************************************************/
    public PortConfig()
    {

    }
}
