package jutils.core.pcap.ethernet;

import jutils.core.io.FieldPrinter;
import jutils.core.io.FieldPrinter.ITierPrinter;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface ITcpIpLayer extends ITierPrinter
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void printFields( FieldPrinter printer );

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName();

    /***************************************************************************
     * @return
     **************************************************************************/
    public Class<? extends ITcpIpLayer> getNextLayerType();
}
