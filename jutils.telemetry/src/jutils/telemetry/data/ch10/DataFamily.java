package jutils.telemetry.data.ch10;

import jutils.core.INamedItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum DataFamily implements INamedItem
{
    /**  */
    COMPUTER_GENERATED( 0x00, 0x07, "Computer-Generated" ),
    /**  */
    PCM( 0x08, 0x10, "PCM" ),
    /**  */
    TIME( 0x10, 0x1F, "Time" ),
    /**  */
    MIL_STD_1553( 0x18, 0x1F, "MIL-STD-1553" ),
    /**  */
    ANALOG( 0x20, 0x27, "Analog" ),
    /**  */
    DISCRETE( 0x28, 0x2F, "Discrete" ),
    /**  */
    MESSAGE( 0x30, 0x37, "Message" ),
    /**  */
    ARINC_429( 0x38, 0x3F, "ARINC-429" ),
    /**  */
    VIDEO( 0x40, 0x47, "Video" ),
    /**  */
    IMAGE( 0x48, 0x4F, "Image" ),
    /**  */
    UART( 0x50, 0x57, "UART" ),
    /**  */
    IEEE_1394( 0x58, 0x5F, "IEEE 1394" ),
    /**  */
    PARALLEL( 0x60, 0x67, "Parallel" ),
    /**  */
    ETHERNET( 0x68, 0x6F, "Ethernet" ),
    /**  */
    TSPI_CTS( 0x70, 0x77, "TSPI/CTS" ),
    /**  */
    CANBUS( 0x78, "Controller Area Network" ),
    /**  */
    FIBRE_CHANNEL( 0x79, 0x80, "Fibre Channel" ),;

    /**  */
    public final int start;
    /**  */
    public final int end;
    /**  */
    public final String name;

    /***************************************************************************
     * @param value
     * @param name
     **************************************************************************/
    private DataFamily( int value, String name )
    {
        this( value, value, name );
    }

    /***************************************************************************
     * @param start
     * @param end
     * @param name
     **************************************************************************/
    private DataFamily( int start, int end, String name )
    {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * @param datatype
     * @return
     **************************************************************************/
    public static DataFamily deriveFamily( int datatype )
    {
        for( DataFamily d : values() )
        {
            if( d.start <= datatype && datatype <= d.end )
            {
                return d;
            }
        }

        return null;
    }
}
