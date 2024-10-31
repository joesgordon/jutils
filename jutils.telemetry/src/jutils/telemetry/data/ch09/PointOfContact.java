package jutils.telemetry.data.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PointOfContact implements ITierPrinter
{
    /** G\POC1-n */
    public String name;
    /** G\POC2-n */
    public String agency;
    /** G\POC3-n */
    public String address;
    /** G\POC4-n */
    public String telephone;

    /***************************************************************************
     * 
     **************************************************************************/
    public PointOfContact()
    {
        this.name = null;
        this.agency = null;
        this.address = null;
        this.telephone = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Name", name );
        printer.printField( "Agency", agency );
        printer.printField( "Address", address );
        printer.printField( "Telephone", telephone );
    }
}
