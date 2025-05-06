package jutils.telemetry.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HwModule implements ITierPrinter
{
    /**  */
    public String id;
    /**  */
    public String serialNumber;
    /**  */
    public String firmwareRevision;

    /***************************************************************************
     * 
     **************************************************************************/
    public HwModule()
    {
        this.id = null;
        this.serialNumber = null;
        this.firmwareRevision = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "ID", id );
        printer.printField( "Serial Number", serialNumber );
        printer.printField( "Firmware Revision", firmwareRevision );
    }
}
