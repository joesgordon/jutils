package jutils.core.pcap;

import java.io.IOException;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class IBlock implements ITierPrinter
{
    /**  */
    public int id;
    /**  */
    public int length;

    /***************************************************************************
     * @param id
     **************************************************************************/
    protected IBlock( BlockType id )
    {
        this.id = id.value;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDescription()
    {
        FieldPrinter printer = new FieldPrinter();

        printer.printHexField( "ID", id );
        printer.printField( "Type",
            BlockType.fromValue( id ).getDescription() );
        printer.printField( "Length", length );

        printFields( printer );

        return printer.toString();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public abstract void printFields( FieldPrinter printer );

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IBlockBodySerializer
    {
        /**
         * @param stream
         * @param id
         * @param length
         * @return
         * @throws IOException
         */
        public IBlock read( IDataStream stream, int id, int length )
            throws IOException;
    }
}
