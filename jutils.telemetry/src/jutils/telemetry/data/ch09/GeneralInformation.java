package jutils.telemetry.data.ch09;

import java.util.ArrayList;
import java.util.List;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class GeneralInformation implements ITierPrinter
{
    /** Name of program. Always allowed. Max 16 characters. */
    public String programName;
    /**
     * Test item description in terms of name, model, platform, or
     * identification code, as appropriate. Always allowed. Max 64 characters.
     */
    public String testItem;
    /**  */
    public final Information information;
    /**  */
    public final List<DataSource> dataSources;
    /**  */
    public final TestInformation testInfo;
    /**  */
    public String checksum;
    /**  */
    public String comments;

    /***************************************************************************
     * 
     **************************************************************************/
    public GeneralInformation()
    {
        this.programName = "";
        this.testItem = "";
        this.information = new Information();
        this.dataSources = new ArrayList<>();
        this.testInfo = new TestInformation();
        this.checksum = "";
        this.comments = "";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Program Name", programName );
        printer.printField( "Test Item", testItem );
        printer.printTier( "Information", information );
        printer.printTiers( "Data Sources", dataSources );
        printer.printTier( "Test Information", testInfo );
        printer.printField( "Checksum", checksum );
        printer.printField( "Comments", comments );
    }
}
