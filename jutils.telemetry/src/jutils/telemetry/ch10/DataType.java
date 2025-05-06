package jutils.telemetry.ch10;

import jutils.core.INamedValue;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum DataType implements INamedValue
{
    // -------------------------------------------------------------------------
    // Computer Generated Formats
    // -------------------------------------------------------------------------

    /**  */
    COMPUTER_GENERATED_0( 0x00, "Computer-Generated-0", "User-Defined", 0x06 ),
    /**  */
    COMPUTER_GENERATED_1( 0x01, "Computer-Generated-1", "Setup Record", 0x09 ),
    /**  */
    COMPUTER_GENERATED_2( 0x02, "Computer-Generated-2", "Recording Events",
        0x06 ),
    /**  */
    COMPUTER_GENERATED_3( 0x03, "Computer-Generated-3", "Recording Index",
        0x06 ),
    /**  */
    COMPUTER_GENERATED_4( 0x04, "Computer-Generated-4",
        "Streaming Configuration Records", 0x08 ),
    /**  */
    COMPUTER_GENERATED_5( 0x05, "Computer-Generated-5",
        "Reserved for future use", 0x06 ),
    /**  */
    COMPUTER_GENERATED_6( 0x06, "Computer-Generated-6",
        "Reserved for future use", 0x06 ),
    /**  */
    COMPUTER_GENERATED_7( 0x07, "Computer-Generated-7",
        "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // PCM Formats
    // -------------------------------------------------------------------------

    /**  */
    PCM_0( 0x08, "PCM-0", "Reserved for future use", 0x06 ),
    /**  */
    PCM_1( 0x09, "PCM-1", "Chapter 4, 7, or 8", 0x06 ),
    /**  */
    PCM_2( 0x0A, "PCM-2", "DQE PCM", 0x0A ),
    /**  */
    PCM_3( 0x0B, "PCM-3", "Reserved for future use", 0x06 ),
    /**  */
    PCM_4( 0x0C, "PCM-4", "Reserved for future use", 0x06 ),
    /**  */
    PCM_5( 0x0D, "PCM-5", "Reserved for future use", 0x06 ),
    /**  */
    PCM_6( 0x0E, "PCM-6", "Reserved for future use", 0x06 ),
    /**  */
    PCM_7( 0x0F, "PCM-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Time Formats
    // -------------------------------------------------------------------------

    /**  */
    TIME_0( 0x10, "Time-0", "Reserved for future use", 0x06 ),
    /**  */
    TIME_1( 0x11, "Time-1", "RCC/GPS/Relative Time Counter (RTC)", 0x06 ),
    /**  */
    TIME_2( 0x12, "Time-2", "Network Time", 0x08 ),
    /**  */
    TIME_3( 0x13, "Time-3", "Reserved for future use", 0x06 ),
    /**  */
    TIME_4( 0x14, "Time-4", "Reserved for future use", 0x06 ),
    /**  */
    TIME_5( 0x15, "Time-5", "Reserved for future use", 0x06 ),
    /**  */
    TIME_6( 0x16, "Time-6", "Reserved for future use", 0x06 ),
    /**  */
    TIME_7( 0x17, "Time-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // MIL-STD Formats
    // -------------------------------------------------------------------------

    /**  */
    MIL_STD_1553_0( 0x18, "MIL-STD-1553-0", "Reserved for future use", 0x06 ),
    /**  */
    MIL_STD_1553_1( 0x19, "MIL-STD-1553-1", "MIL-STD-1553B Data", 0x06 ),
    /**  */
    MIL_STD_1553_2( 0x1A, "MIL-STD-1553-2", "16PP194 Bus", 0x06 ),
    /**  */
    MIL_STD_1553_3( 0x1B, "MIL-STD-1553-3", "Reserved for future use", 0x06 ),
    /**  */
    MIL_STD_1553_4( 0x1C, "MIL-STD-1553-4", "Reserved for future use", 0x06 ),
    /**  */
    MIL_STD_1553_5( 0x1D, "MIL-STD-1553-5", "Reserved for future use", 0x06 ),
    /**  */
    MIL_STD_1553_6( 0x1E, "MIL-STD-1553-6", "Reserved for future use", 0x06 ),
    /**  */
    MIL_STD_1553_7( 0x1F, "MIL-STD-1553-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Analog Formats
    // -------------------------------------------------------------------------

    /**  */
    ANALOG_0( 0x20, "Analog-0", "Reserved for future use", 0x06 ),
    /**  */
    ANALOG_1( 0x21, "Analog-1", "Analog Data", 0x06 ),
    /**  */
    ANALOG_2( 0x22, "Analog-2", "Reserved for future use", 0x0A ),
    /**  */
    ANALOG_3( 0x23, "Analog-3",
        "In-phase and quadrature (I/Q) recording of intermediate frequency (IF)",
        0x0A ),
    /**  */
    ANALOG_4( 0x24, "Analog-4", "Reserved for future use", 0x06 ),
    /**  */
    ANALOG_5( 0x25, "Analog-5", "Reserved for future use", 0x06 ),
    /**  */
    ANALOG_6( 0x26, "Analog-6", "Reserved for future use", 0x06 ),
    /**  */
    ANALOG_7( 0x27, "Analog-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Discrete Formats
    // -------------------------------------------------------------------------

    /**  */
    DISCRETE_0( 0x28, "Discrete-0", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_1( 0x29, "Discrete-1", "Discrete Data", 0x06 ),
    /**  */
    DISCRETE_2( 0x2A, "Discrete-2", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_3( 0x2B, "Discrete-3", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_4( 0x2C, "Discrete-4", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_5( 0x2D, "Discrete-5", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_6( 0x2E, "Discrete-6", "Reserved for future use", 0x06 ),
    /**  */
    DISCRETE_7( 0x2F, "Discrete-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Message Formats
    // -------------------------------------------------------------------------

    /**  */
    MESSAGE_0( 0x30, "Message-0", "Generic Message Data", 0x06 ),
    /**  */
    MESSAGE_1( 0x31, "Message-1", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_2( 0x32, "Message-2", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_3( 0x33, "Message-3", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_4( 0x34, "Message-4", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_5( 0x35, "Message-5", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_6( 0x36, "Message-6", "Reserved for future use", 0x06 ),
    /**  */
    MESSAGE_7( 0x37, "Message-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // ARINC-429 Formats
    // -------------------------------------------------------------------------

    /**  */
    ARINC_429_0( 0x38, "ARINC-429-0", "ARINC-429 Data", 0x06 ),
    /**  */
    ARINC_429_1( 0x39, "ARINC-429-1", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_2( 0x3A, "ARINC-429-2", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_3( 0x3B, "ARINC-429-3", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_4( 0x3C, "ARINC-429-4", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_5( 0x3D, "ARINC-429-5", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_6( 0x3E, "ARINC-429-6", "Reserved for future use", 0x06 ),
    /**  */
    ARINC_429_7( 0x3F, "ARINC-429-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Video Formats
    // -------------------------------------------------------------------------

    /** LEGACY */
    VIDEO_0( 0x40, "Video-0", "MPEG-2 (Legacy)", 0x0A ),
    /** DEPRECATED */
    VIDEO_1( 0x41, "Video-1", "(Deprecated, use Video Format 2)", 0x0A ),
    /**  */
    VIDEO_2( 0x42, "Video-2", "MPEG-2/H.264 Video", 0x0A ),
    /** DEPRECATED */
    VIDEO_3( 0x43, "Video-3", "MJPEG (Deprecated)", 0x0A ),
    /**  */
    VIDEO_4( 0x44, "Video-4", "MJPEG-2000", 0x07 ),
    /**  */
    VIDEO_5( 0x45, "Video-5", "Reserved for future use", 0x06 ),
    /**  */
    VIDEO_6( 0x46, "Video-6", "Reserved for future use", 0x06 ),
    /**  */
    VIDEO_7( 0x47, "Video-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Image Formats
    // -------------------------------------------------------------------------

    /**  */
    IMAGE_0( 0x48, "Image-0", "Image Data", 0x06 ),
    /**  */
    IMAGE_1( 0x49, "Image-1", "Still Imagery", 0x06 ),
    /**  */
    IMAGE_2( 0x4A, "Image-2", "Dynamic Imagery", 0x06 ),
    /**  */
    IMAGE_3( 0x4B, "Image-3", "Reserved for future use", 0x06 ),
    /**  */
    IMAGE_4( 0x4C, "Image-4", "Reserved for future use", 0x06 ),
    /**  */
    IMAGE_5( 0x4D, "Image-5", "Reserved for future use", 0x06 ),
    /**  */
    IMAGE_6( 0x4E, "Image-6", "Reserved for future use", 0x06 ),
    /**  */
    IMAGE_7( 0x4F, "Image-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // UART Formats
    // -------------------------------------------------------------------------

    /**  */
    UART_0( 0x50, "UART-0", "UART Data", 0x06 ),
    /**  */
    UART_1( 0x51, "UART-1", "Reserved for future use", 0x06 ),
    /**  */
    UART_2( 0x52, "UART-2", "Reserved for future use", 0x06 ),
    /**  */
    UART_3( 0x53, "UART-3", "Reserved for future use", 0x06 ),
    /**  */
    UART_4( 0x54, "UART-4", "Reserved for future use", 0x06 ),
    /**  */
    UART_5( 0x55, "UART-5", "Reserved for future use", 0x06 ),
    /**  */
    UART_6( 0x56, "UART-6", "Reserved for future use", 0x06 ),
    /**  */
    UART_7( 0x57, "UART-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // IEEE-1394 Formats
    // -------------------------------------------------------------------------

    /**  */
    IEEE_1394_0( 0x58, "IEEE 1394-0", "IEEE 1394 Transaction", 0x06 ),
    /**  */
    IEEE_1394_1( 0x59, "IEEE 1394-1", "IEEE 1394 Physical Layer", 0x06 ),
    /**  */
    IEEE_1394_2( 0x5A, "IEEE 1394-2", "Reserved for future use", 0x06 ),
    /**  */
    IEEE_1394_3( 0x5B, "IEEE 1394-3", "Reserved for future use", 0x06 ),
    /**  */
    IEEE_1394_4( 0x5C, "IEEE 1394-4", "Reserved for future use", 0x06 ),
    /**  */
    IEEE_1394_5( 0x5D, "IEEE 1394-5", "Reserved for future use", 0x06 ),
    /**  */
    IEEE_1394_6( 0x5E, "IEEE 1394-6", "Reserved for future use", 0x06 ),
    /**  */
    IEEE_1394_7( 0x5F, "IEEE 1394-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Parallel Formats
    // -------------------------------------------------------------------------

    /**  */
    PARALLEL_0( 0x60, "Parallel-0", "Parallel Data", 0x06 ),
    /**  */
    PARALLEL_1( 0x61, "Parallel-1", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_2( 0x62, "Parallel-2", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_3( 0x63, "Parallel-3", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_4( 0x64, "Parallel-4", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_5( 0x65, "Parallel-5", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_6( 0x66, "Parallel-6", "Reserved for future use", 0x06 ),
    /**  */
    PARALLEL_7( 0x67, "Parallel-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Ethernet Formats
    // -------------------------------------------------------------------------

    /**  */
    ETHERNET_0( 0x68, "Ethernet-0", "Ethernet Data", 0x07 ),
    /**  */
    ETHERNET_1( 0x69, "Ethernet-1", "Ethernet UDP Payload", 0x06 ),
    /**  */
    ETHERNET_2( 0x6A, "Ethernet-2", "Reserved for future use", 0x06 ),
    /**  */
    ETHERNET_3( 0x6B, "Ethernet-3", "Reserved for future use", 0x06 ),
    /**  */
    ETHERNET_4( 0x6C, "Ethernet-4", "Reserved for future use", 0x06 ),
    /**  */
    ETHERNET_5( 0x6D, "Ethernet-5", "Reserved for future use", 0x06 ),
    /**  */
    ETHERNET_6( 0x6E, "Ethernet-6", "Reserved for future use", 0x06 ),
    /**  */
    ETHERNET_7( 0x6F, "Ethernet-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // TSPI/CTS Formats
    // -------------------------------------------------------------------------

    /**  */
    TSPI_CTS_0( 0x70, "TSPI/CTS-0", "GPS NMEA-RTCM", 0x06 ),
    /**  */
    TSPI_CTS_1( 0x71, "TSPI/CTS-1", "EAG ACMI", 0x06 ),
    /**  */
    TSPI_CTS_2( 0x72, "TSPI/CTS-2", "ACTTS", 0x06 ),
    /**  */
    TSPI_CTS_3( 0x73, "TSPI/CTS-3", "Reserved for future use", 0x06 ),
    /**  */
    TSPI_CTS_4( 0x74, "TSPI/CTS-4", "Reserved for future use", 0x06 ),
    /**  */
    TSPI_CTS_5( 0x75, "TSPI/CTS-5", "Reserved for future use", 0x06 ),
    /**  */
    TSPI_CTS_6( 0x76, "TSPI/CTS-6", "Reserved for future use", 0x06 ),
    /**  */
    TSPI_CTS_7( 0x77, "TSPI/CTS-7", "Reserved for future use", 0x06 ),

    // -------------------------------------------------------------------------
    // Controller Area Network Bus Formats
    // -------------------------------------------------------------------------

    /**  */
    CONTROLLER_AREA_NETWORK_BUS( 0x78, "Controller Area Network Bus", "CAN Bus",
        0x06 ),

    // -------------------------------------------------------------------------
    // Fiber channel Formats
    // -------------------------------------------------------------------------

    /**  */
    FIBRE_CHANNEL_0( 0x79, "Fibre Channel-0", "Fibre Channel Data", 0x07 ),
    /**  */
    FIBRE_CHANNEL_1( 0x7A, "Fibre Channel-1", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_2( 0x7B, "Fibre Channel-2", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_3( 0x7C, "Fibre Channel-3", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_4( 0x7D, "Fibre Channel-4", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_5( 0x7E, "Fibre Channel-5", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_6( 0x7F, "Fibre Channel-6", "Fibre Channel Data", 0x08 ),
    /**  */
    FIBRE_CHANNEL_7( 0x80, "Fibre Channel-7", "Fibre Channel Data", 0x08 ),;

    /**  */
    public final int value;
    /**  */
    public final String name;
    /**  */
    public final String description;
    /**  */
    public final int version;
    /**  */
    public final DataFamily family;

    /***************************************************************************
     * @param value
     * @param name
     * @param description
     * @param version
     **************************************************************************/
    private DataType( int value, String name, String description, int version )
    {
        this.value = value;
        this.name = name;
        this.description = description;
        this.version = version;
        this.family = DataFamily.deriveFamily( value );
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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static DataType fromValue( byte value )
    {
        return fromValue( value & 0xFF );
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    public static DataType fromValue( int value )
    {
        return INamedValue.fromValue( value, values(), null );
    }
}
