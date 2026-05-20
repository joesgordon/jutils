package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.io.IDataStream;
import jutils.core.swap.ByteSwapper;
import jutils.core.swap.SwapPattern;
import jutils.telemetry.ch10.Pcm1SpecificData;
import jutils.telemetry.ch10.PcmThroughputData;
import jutils.telemetry.ch10.io.PcmDataSerializer.IPcmDataSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmThroughputDataSerializer
    implements IPcmDataSerializer<PcmThroughputData>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public PcmThroughputData read( IDataStream stream, Pcm1SpecificData psd,
        int size ) throws IOException
    {
        PcmThroughputData data = new PcmThroughputData();

        data.data = new byte[size];

        stream.readFully( data.data );

        if( psd.is16BitAligned() )
        {
            ByteSwapper.swap( SwapPattern.ENDIAN16, data.data );
        }
        else
        {
            ByteSwapper.swap( SwapPattern.ENDIAN32, data.data );
        }

        return data;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( PcmThroughputData data, IDataStream stream,
        Pcm1SpecificData psd, int size ) throws IOException
    {
        // TODO Auto-generated method stub
    }
}
