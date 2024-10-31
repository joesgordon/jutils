package jutils.telemetry.data.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EthernetPort implements ITierPrinter
{
    /**  */
    public String address;
    /**  */
    public String type;

    /***************************************************************************
     * 
     **************************************************************************/
    public EthernetPort()
    {
        this.address = null;
        this.type = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Address", address );
        printer.printField( "Type", type );
    }
}
