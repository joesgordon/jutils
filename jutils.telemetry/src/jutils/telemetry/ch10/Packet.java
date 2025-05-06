package jutils.telemetry.ch10;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Packet implements ITierPrinter
{
    /**  */
    public final PacketHeader header;
    /**  */
    public final SecondaryHeader secondary;
    /**  */
    public IPacketBody body;
    /**  */
    public final PacketTrailer trailer;

    /***************************************************************************
     * 
     **************************************************************************/
    public Packet()
    {
        this.header = new PacketHeader();
        this.secondary = new SecondaryHeader();
        this.body = null;
        this.trailer = new PacketTrailer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer )
    {
        printer.printTier( "Header", header );
        if( header.secHdrPresent )
        {
            printer.printTier( "Secondary Header", secondary );
        }
        printer.printTier( "Trailer", trailer );
        if( body != null )
        {
            printer.printTier( "Body", body );
        }
    }

    /***************************************************************************
     * @param <T>
     * @return
     **************************************************************************/
    public <T extends IPacketBody> T getBody()
    {
        @SuppressWarnings( "unchecked")
        T t = ( T )body;
        return t;
    }
}
