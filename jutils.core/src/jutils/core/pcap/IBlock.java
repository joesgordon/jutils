package jutils.core.pcap;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jutils.core.io.ByteArrayStream;
import jutils.core.io.DataStream;
import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;
import jutils.core.io.IDataStream;
import jutils.core.pcap.options.Option;
import jutils.core.pcap.options.Option.OptionSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class IBlock implements ITierPrinter
{
    /**  */
    public int id;
    /**  */
    public int length;

    /**  */
    public final List<Option> options;

    /***************************************************************************
     * @param id
     **************************************************************************/
    protected IBlock( BlockType id )
    {
        this.id = id.value;
        this.length = -1;
        this.options = new ArrayList<>();
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
     * @param <T>
     **************************************************************************/
    public static interface IBlockBodySerializer<T extends IBlock>
    {
        /**
         * @param stream
         * @param type
         * @param length
         * @return
         * @throws IOException
         */
        public T read( IDataStream stream, BlockType type, int length )
            throws IOException;

        /**
         * @param stream
         * @param block
         * @param optionsSize
         * @throws IOException
         * @throws EOFException
         */
        public default void readOptions( IDataStream stream, T block,
            int optionsSize ) throws EOFException, IOException
        {
            OptionSerializer serializer = new OptionSerializer();
            byte [] options = new byte[optionsSize];

            stream.readFully( options );

            try( ByteArrayStream bas = new ByteArrayStream( options,
                optionsSize, 0, false );
                 IDataStream os = new DataStream( bas, stream.getOrder() ) )
            {
                while( bas.getAvailable() > 0 )
                {
                    Option option = serializer.read( os );
                    block.options.add( option );
                }
            }
        }
    }
}
