package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.MajorLockStatus;
import jutils.telemetry.data.ch10.MinorLockStatus;
import jutils.telemetry.data.ch10.Pcm1SpecificData;
import jutils.telemetry.data.ch10.Pcm1Word;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Pcm1SpecificDataSerializer
    implements IDataSerializer<Pcm1SpecificData>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Pcm1SpecificData read( IDataStream stream )
        throws IOException, ValidationException
    {
        Pcm1SpecificData data = new Pcm1SpecificData();

        read( data, stream );

        return data;
    }

    /***************************************************************************
     * @param data
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void read( Pcm1SpecificData data, IDataStream stream )
        throws IOException
    {
        int word = stream.readInt();
        int field;

        data.syncOffset = Pcm1Word.SYNC_OFFSET.getField( word );

        data.unpackedMode = Pcm1Word.UNPACKED_MODE.getFlag( word );

        data.packedMode = Pcm1Word.PACKED_MODE.getFlag( word );

        data.throughputMode = Pcm1Word.THROUGHPUT_MODE.getFlag( word );

        data.alignmentMode = Pcm1Word.ALIGNMENT_MODE.getFlag( word );

        data.modeReserved = Pcm1Word.MODE_RESERVED.getField( word );

        field = Pcm1Word.MAJOR_LOCK_STATUS.getField( word );
        data.majorStatus = MajorLockStatus.fromValue( field );

        field = Pcm1Word.MINOR_LOCK_STATUS.getField( word );
        data.minorStatus = MinorLockStatus.fromValue( field );

        data.minorIndicator = Pcm1Word.MINOR_INDICATOR.getFlag( word );

        data.minorIndicator = Pcm1Word.MINOR_INDICATOR.getFlag( word );

        data.iphIndicator = Pcm1Word.IPH_INDICATOR.getFlag( word );

        data.reserved = Pcm1Word.RESERVED.getField( word );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( Pcm1SpecificData data, IDataStream stream )
        throws IOException
    {
        // TODO Auto-generated method stub
    }
}
