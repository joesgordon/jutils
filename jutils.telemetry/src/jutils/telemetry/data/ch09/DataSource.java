package jutils.telemetry.data.ch09;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DataSource implements ITierPrinter
{
    /**  */
    public String id;
    /**  */
    public DataSourceType type;
    /**  */
    public String classification;

    /***************************************************************************
     * 
     **************************************************************************/
    public DataSource()
    {
        this.id = "";
        this.type = null;
        this.classification = "";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "ID", id );
        printer.printField( "Type", type );
        printer.printField( "Classification", classification );
    }
}
