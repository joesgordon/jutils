package jutils.telemetry.data.ch10;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketTrailer implements ITierPrinter
{
    /**  */
    public long checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketTrailer()
    {
        this.checksum = 0;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printHexField( "Checksum", checksum );
    }
}
