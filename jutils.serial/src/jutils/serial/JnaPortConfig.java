package jutils.serial;

/*******************************************************************************
 * 
 ******************************************************************************/
class JnaPortConfig
{
    /**  */
    public int baud;
    /**  */
    public int parity;
    /**  */
    public int wordSize;
    /**  */
    public int stopBits;

    /**  */
    public boolean ctsOutputControl;
    /**  */
    public boolean dsrOutputControl;
    /** DSR Sensitivity */
    public boolean dsrInputControl;
    /**  */
    public int dtrControl;
    /**  */
    public int rtsControl;

    /**  */
    public boolean softwareOutputControl;
    /**  */
    public boolean softwareInputControl;
    /**  */
    public int xOnLimit;
    /**  */
    public int xOffLimit;
    /**  */
    public byte xOnChar;
    /**  */
    public byte xOffChar;

    /***************************************************************************
     * 
     **************************************************************************/
    JnaPortConfig()
    {
        // TODO Auto-generated constructor stub
    }
}
