package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Information implements ITierPrinter
{
    /** Name of this TMATS file. Always allowed. Max 256 characters. */
    public String filename;
    /**
     * Version of RCC IRIG 106 standard used to generate this TMATS file. The
     * last 2 digits of the year should be used. Use a leading 0 if necessary.
     * Always required. Range 00-90.
     */
    public String revisionLevel;
    /**
     * Date of origination of this mission configuration. Always allowed. Format
     * MM-DD-YYYY.
     */
    public String originationDate;
    /**
     * Revision number associated with this mission configuration. Always
     * allowed. Range 0-9999.
     */
    public String revisionNumber;
    /** Date of revision. Always allowed. Format MM-DD-YYYY. */
    public String revisionDate;
    /**
     * Update number of current change that has not been incorporated as a
     * revision. Always required. Range 00-90.
     */
    public String updateNumber;
    /** Date of update. Always allowed. Format MM-DD-YYYY. */
    public String updateDate;
    /** Test identification. Always allowed. Max 16 characters. */
    public String testNumber;
    /**  */
    public Integer pocCount;
    /**  */
    public final List<PointOfContact> pocs;

    /***************************************************************************
     * 
     **************************************************************************/
    public Information()
    {
        this.filename = "";
        this.revisionLevel = "";
        this.originationDate = "";
        this.revisionNumber = "";
        this.revisionDate = "";
        this.updateNumber = "";
        this.updateDate = "";
        this.testNumber = "";
        this.pocCount = null;
        this.pocs = new ArrayList<>();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "File Name", filename );
        printer.printField( "Revision Level", revisionLevel );
        printer.printField( "Origination Date", originationDate );
        printer.printField( "Revision Number", revisionNumber );
        printer.printField( "Revision Date", revisionDate );
        printer.printField( "Update Number", updateNumber );
        printer.printField( "Update Date", updateDate );
        printer.printField( "Test Number", testNumber );
        printer.printField( "POC Count", pocCount );
        printer.printTiers( "POCs", pocs );
    }
}
