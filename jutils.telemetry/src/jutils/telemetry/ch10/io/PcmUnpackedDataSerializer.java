package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.io.IDataStream;
import jutils.telemetry.ch10.Pcm1SpecificData;
import jutils.telemetry.ch10.PcmUnpackedData;
import jutils.telemetry.ch10.io.PcmDataSerializer.IPcmDataSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmUnpackedDataSerializer
    implements IPcmDataSerializer<PcmUnpackedData>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PcmUnpackedData read( IDataStream stream, Pcm1SpecificData psd,
        int size ) throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( PcmUnpackedData data, IDataStream stream,
        Pcm1SpecificData psd, int size ) throws IOException
    {
        // TODO Auto-generated method stub
    }
}
