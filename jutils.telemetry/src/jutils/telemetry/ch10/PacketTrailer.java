package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PacketTrailer implements ITierPrinter
{
    /**  */
    public byte [] filler;
    /**  */
    public long checksum;

    /***************************************************************************
     * 
     **************************************************************************/
    public PacketTrailer()
    {
        this.filler = new byte[0];
        this.checksum = 0L;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printFieldValues( "Filler", filler );
        printer.printHexField( "Checksum", checksum );
    }
}
