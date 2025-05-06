package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmThroughputData implements IPcmData
{
    /**  */
    public byte [] data;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcmThroughputData()
    {
        this.data = new byte[0];
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
