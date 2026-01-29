package jutils.core.pcapng.blocks;

import jutils.core.io.FieldPrinter;
import jutils.core.pcapng.BlockType;
import jutils.core.pcapng.IBlock;

/*******************************************************************************
 *
 ******************************************************************************/
public class SectionHeader extends IBlock
{
    /**  */
    public int byteOrderCode;
    /**  */
    public short majorVersion;
    /**  */
    public short minorVersion;
    /**  */
    public long sectionLength;
    /**  */
    public final SectionOptions options;

    /***************************************************************************
     * 
     **************************************************************************/
    public SectionHeader()
    {
        super( BlockType.SECTION_HEADER );

        this.byteOrderCode = 0;
        this.majorVersion = 0;
        this.minorVersion = 0;
        this.sectionLength = 0;
        this.options = new SectionOptions();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printHexField( "Byte Order Code", byteOrderCode );
        printer.printField( "Major Version", majorVersion );
        printer.printField( "Minor Version", minorVersion );
        printer.printField( "Section Length", sectionLength );
        printer.printTier( "Section Options", options );
    }
}
