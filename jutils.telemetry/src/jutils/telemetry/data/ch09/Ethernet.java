package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ethernet implements ITierPrinter
{
    /**  */
    public String name;
    /**  */
    public String physical;
    /**  */
    public String linkSpeed;
    /**  */
    public String type;
    /**  */
    public String address;
    /**  */
    public Integer portCount;
    /**  */
    public final List<EthernetPort> ports;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ethernet()
    {
        this.name = null;
        this.physical = null;
        this.linkSpeed = null;
        this.type = null;
        this.address = null;
        this.portCount = null;
        this.ports = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Interface Name", name );
        printer.printField( "Physical Interface", physical );
        printer.printField( "Link Speed", linkSpeed );
        printer.printField( "Type", type );
        printer.printField( "IP Address", address );
        printer.printField( "Port Count", portCount );
        printer.printTiers( "Ports", ports );
    }
}
