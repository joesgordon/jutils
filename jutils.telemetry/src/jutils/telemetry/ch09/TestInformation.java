package jutils.telemetry.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TestInformation implements ITierPrinter
{
    /**  */
    public String testDuration;
    /**  */
    public String preTestRequirement;
    /**  */
    public String postTestRequirement;
    /**  */
    public String classification;

    /***************************************************************************
     * 
     **************************************************************************/
    public TestInformation()
    {
        this.testDuration = "";
        this.preTestRequirement = "";
        this.postTestRequirement = "";
        this.classification = "";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Test Duration", testDuration );
        printer.printField( "Pre-Test Requirement", preTestRequirement );
        printer.printField( "Post-Test Requirement", postTestRequirement );
        printer.printField( "Classification", classification );
    }
}
