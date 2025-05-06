package jutils.telemetry.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecorderMedia implements ITierPrinter
{
    /**  */
    public String type;
    /**  */
    public String manufacturer;
    /**  */
    public String code;
    /**  */
    public String location;
    /**  */
    public String rmmBusSpeed;
    /**  */
    public String tapeWidth;
    /**  */
    public String tapeHousing;
    /**  */
    public String trackType;
    /**  */
    public Integer channelCount;
    /**  */
    public String recordSpeed;
    /**  */
    public String dataDensity;
    /**  */
    public String tapeRewound;
    /**  */
    public String numSourceBits;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecorderMedia()
    {
        this.type = null;
        this.manufacturer = null;
        this.code = null;
        this.location = null;
        this.rmmBusSpeed = null;
        this.tapeWidth = null;
        this.tapeHousing = null;
        this.trackType = null;
        this.channelCount = null;
        this.recordSpeed = null;
        this.dataDensity = null;
        this.tapeRewound = null;
        this.numSourceBits = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Type", type );
        printer.printField( "Manufacturer", manufacturer );
        printer.printField( "Code", code );
        printer.printField( "Location", location );
        printer.printField( "External RMM Bus Speed", rmmBusSpeed );
        printer.printField( "Tape Width", tapeWidth );
        printer.printField( "Tape Housing", tapeHousing );
        printer.printField( "Type of Tracks", trackType );
        printer.printField( "Number of Tracks/Channels", channelCount );
        printer.printField( "Record Speed", recordSpeed );
        printer.printField( "Data Packing Density", dataDensity );
        printer.printField( "Tape Rewound", tapeRewound );
        printer.printField( "Number of Source bits", numSourceBits );
    }
}
