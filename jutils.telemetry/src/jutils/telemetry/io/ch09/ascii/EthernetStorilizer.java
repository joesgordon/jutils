package jutils.telemetry.io.ch09.ascii;

import jutils.telemetry.data.ch09.Ethernet;
import jutils.telemetry.data.ch09.EthernetPort;

/*******************************************************************************
 * 
 ******************************************************************************/
public class EthernetStorilizer implements IStorilizer<Ethernet>
{
    /**  */
    private final EthernetPortStorilizer portStorilizer;

    /***************************************************************************
     * 
     **************************************************************************/
    public EthernetStorilizer()
    {
        this.portStorilizer = new EthernetPortStorilizer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void read( Ethernet eth, AsciiStore store )
    {
        eth.name = store.getString( "EINM" );
        eth.physical = store.getString( "PEIN" );
        eth.linkSpeed = store.getString( "EILS" );
        eth.type = store.getString( "EIT" );
        eth.address = store.getString( "EIIP" );
        eth.portCount = store.getInteger( "EIIP\\N" );

        eth.ports.clear();
        if( eth.portCount != null )
        {
            for( int i = 0; i < eth.portCount; i++ )
            {
                int num = i + 1;
                String suffix = "-" + num;
                EthernetPort port = new EthernetPort();

                portStorilizer.read( port, store.createSubstore( "", suffix ) );

                eth.ports.add( port );
            }
        }
    }
}
