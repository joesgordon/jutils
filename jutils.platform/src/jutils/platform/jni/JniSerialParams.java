package jutils.platform.jni;

import jutils.core.INamedValue;
import jutils.platform.data.DtrControl;
import jutils.platform.data.Parity;
import jutils.platform.data.RtsControl;
import jutils.platform.data.SerialParams;
import jutils.platform.data.StopBits;
import jutils.platform.data.WordSize;

public class JniSerialParams
{
    /**  */
    public boolean binaryModeEnabled;
    /**  */
    public int baudRate;
    /**  */
    public int size;
    /**  */
    public int parity;
    /**  */
    public int stopBits;
    /** Clear-to-Send */
    public boolean ctsEnabled;
    /** Data-Set-Ready */
    public boolean dsrEnabled;
    /** Data-Terminal-Ready */
    public int dtrControl;
    /** Request-to-Send */
    public int rtsControl;
    /** Software flow control xon/xoff output */
    public boolean swFlowOutputEnabled;
    /** Software flow control xon/xoff input */
    public boolean swFlowInputEnabled;

    /***************************************************************************
     * 
     **************************************************************************/
    public JniSerialParams()
    {
        this( new SerialParams() );
    }

    /***************************************************************************
     * @param params
     **************************************************************************/
    public JniSerialParams( SerialParams params )
    {
        this.binaryModeEnabled = params.binaryModeEnabled;
        this.baudRate = params.baudRate;
        this.size = params.size.value;
        this.parity = params.parity.value;
        this.stopBits = params.stopBits.value;
        this.ctsEnabled = params.ctsEnabled;
        this.dsrEnabled = params.dsrEnabled;
        this.dtrControl = params.dtrControl.value;
        this.rtsControl = params.rtsControl.value;
        this.swFlowOutputEnabled = params.swFlowOutputEnabled;
        this.swFlowInputEnabled = params.swFlowInputEnabled;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public SerialParams getParams()
    {
        SerialParams params = new SerialParams();

        params.binaryModeEnabled = this.binaryModeEnabled;
        params.baudRate = this.baudRate;
        params.size = INamedValue.fromValue( this.size, WordSize.values(),
            null );
        params.parity = INamedValue.fromValue( this.parity, Parity.values(),
            null );
        params.stopBits = INamedValue.fromValue( this.stopBits,
            StopBits.values(), null );
        params.ctsEnabled = false;
        params.dsrEnabled = false;
        params.dtrControl = INamedValue.fromValue( this.dtrControl,
            DtrControl.values(), null );
        params.rtsControl = INamedValue.fromValue( this.rtsControl,
            RtsControl.values(), null );
        params.swFlowOutputEnabled = false;
        params.swFlowInputEnabled = false;

        return params;
    }
}
