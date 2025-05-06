package jutils.telemetry.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Recorder implements ITierPrinter
{
    /**  */
    public String sourceId;
    /**  */
    public String id;
    /**  */
    public String description;
    /**  */
    public final RecorderMedia media;
    /**  */
    public final RecorderInfo info;

    /***************************************************************************
     * 
     **************************************************************************/
    public Recorder()
    {
        this.sourceId = null;
        this.id = null;
        this.description = null;
        this.media = new RecorderMedia();
        this.info = new RecorderInfo();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Data Source ID", sourceId );
        printer.printField( "Recorder/Reproducer ID", id );
        printer.printField( "Recorder/Reproducer Description", description );
        printer.printTier( "Recorder-Reproducer Media Characteristics", media );
        printer.printTier( "Recorder-Reproducer Information", info );
    }
}
