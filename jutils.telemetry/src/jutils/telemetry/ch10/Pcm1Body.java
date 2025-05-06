package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1Body implements IPacketBody
{
    /**  */
    public final Pcm1SpecificData specificData;

    /**  */
    public IPcmData data;

    /***************************************************************************
     * 
     **************************************************************************/
    public Pcm1Body()
    {
        this.specificData = new Pcm1SpecificData();
        this.data = new PcmThroughputData();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "Specific Data", specificData );
        printer.printTier( "Data", data );
    }
}
