package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecorderInfo implements ITierPrinter
{
    /**  */
    public String manufacturer;
    /**  */
    public String model;
    /**  */
    public String isOriginal;
    /**  */
    public String originalDateTime;
    /**  */
    public final PointOfContact creatingPoc;
    /**  */
    public String copyDateTime;
    /**  */
    public final PointOfContact copyingPoc;
    /**  */
    public String isModified;
    /**  */
    public String modificationType;
    /**  */
    public String modificationDateTime;
    /**  */
    public final PointOfContact modifyingPoc;
    /**  */
    public String isContinuous;
    /**  */
    public String setupSource;
    /**  */
    public String serialNumber;
    /**  */
    public String firmwareRevision;
    /**  */
    public Integer moduleCount;
    /**  */
    public final List<HwModule> modules;
    /**  */
    public Integer rmmCount;
    /**  */
    public final List<HwModule> rmms;
    /**  */
    public Integer ethernetCount;
    /**  */
    public final List<Ethernet> ethernets;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecorderInfo()
    {
        this.manufacturer = null;
        this.model = null;
        this.isOriginal = null;
        this.originalDateTime = null;
        this.creatingPoc = new PointOfContact();
        this.copyDateTime = null;
        this.copyingPoc = new PointOfContact();
        this.isModified = null;
        this.modificationType = null;
        this.modificationDateTime = null;
        this.modifyingPoc = new PointOfContact();
        this.isContinuous = null;
        this.setupSource = null;
        this.serialNumber = null;
        this.firmwareRevision = null;
        this.moduleCount = null;
        this.modules = new ArrayList<>();
        this.rmmCount = null;
        this.rmms = new ArrayList<>();
        this.ethernetCount = null;
        this.ethernets = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Manufacturer", manufacturer );
        printer.printField( "Model", model );
        printer.printField( "Is Original", isOriginal );

        printer.printField( "Original Date/Time", originalDateTime );
        printer.printTier( "Creating POC", creatingPoc );

        printer.printField( "Copy Date/Time", copyDateTime );
        printer.printTier( "Copying POC", copyingPoc );

        printer.printField( "Is Modified", isModified );
        printer.printField( "Modification Type", modificationType );
        printer.printField( "Modification Date/Time", modificationDateTime );
        printer.printTier( "Modifying POC", modifyingPoc );

        printer.printField( "Is Continuous", isContinuous );
        printer.printField( "Setup Source", setupSource );
        printer.printField( "Serial Number", serialNumber );
        printer.printField( "Firmware Revision", firmwareRevision );
        printer.printField( "Module Count", moduleCount );
        printer.printTiers( "Modules", modules );
        printer.printField( "RMM Count", rmmCount );
        printer.printTiers( "RMMs", rmms );

        printer.printField( "Ethernet Count", ethernetCount );
    }
}
