package jutils.telemetry.data.ch10;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataBody implements IPacketBody
{
    /**  */
    public byte [] data;

    /***************************************************************************
     * @param length
     **************************************************************************/
    public DataBody( int length )
    {
        this.data = new byte[length];
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printFieldValues( "Data", data );
    }
}
