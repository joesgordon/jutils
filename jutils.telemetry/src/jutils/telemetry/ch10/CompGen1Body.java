package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;
import jutils.telemetry.ch09.Tmats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CompGen1Body implements IPacketBody
{
    /**  */
    public TmatsFormat format;
    /**  */
    public boolean setupRecordConfigChanged;
    /**  */
    public Rcc106Version rccVersion;
    /**  */
    public int reserved;
    /**  */
    public String setup;
    /**  */
    public Tmats tmats;

    /***************************************************************************
     * 
     **************************************************************************/
    public CompGen1Body()
    {
        this.format = TmatsFormat.ASCII;
        this.setupRecordConfigChanged = false;
        this.rccVersion = Rcc106Version.RCC_106_07;
        this.reserved = 0;
        this.setup = "";
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printField( "Format", format.getDescription() );
        printer.printField( "Setup Record Config Changed",
            setupRecordConfigChanged );
        printer.printField( "RCC Version", rccVersion );
        printer.printHexField( "Reserved", reserved );
        printer.printField( "Setup Record", setup );
    }
}
