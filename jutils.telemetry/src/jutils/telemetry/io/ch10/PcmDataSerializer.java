package jutils.telemetry.io.ch10;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataStream;
import jutils.telemetry.data.ch10.IPcmData;
import jutils.telemetry.data.ch10.PacketHeader;
import jutils.telemetry.data.ch10.Pcm1SpecificData;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PcmDataSerializer
{
    /**  */
    private final PcmThroughputDataSerializer throughputSerializer;
    /**  */
    private final PcmPackedDataSerializer packedSerializer;
    /**  */
    private final PcmUnpackedDataSerializer unpackedSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public PcmDataSerializer()
    {
        this.throughputSerializer = new PcmThroughputDataSerializer();
        this.packedSerializer = new PcmPackedDataSerializer();
        this.unpackedSerializer = new PcmUnpackedDataSerializer();
    }

    /**
     * @param stream
     * @param header
     * @param psd
     * @return
     * @throws IOException
     * @throws ValidationException
     */
    public IPcmData read( IDataStream stream, PacketHeader header,
        Pcm1SpecificData psd ) throws IOException, ValidationException
    {
        IPcmDataSerializer<?> serializer = null;

        if( !psd.unpackedMode && !psd.packedMode && psd.throughputMode )
        {
            serializer = throughputSerializer;
        }
        else if( !psd.unpackedMode && psd.packedMode && !psd.throughputMode )
        {
            serializer = packedSerializer;
        }
        else if( psd.unpackedMode && !psd.packedMode && !psd.throughputMode )
        {
            serializer = unpackedSerializer;
        }
        else
        {
            String err = String.format(
                "Invalid PCM-1 Specific Data: Unpacked=%s, Packed=%s, Throughput=%s",
                psd.unpackedMode, psd.packedMode, psd.throughputMode );
            throw new ValidationException( err );
        }

        int size = header.dataLength - 4;

        return serializer.read( stream, psd, size );
    }

    /**
     * @param data
     * @param stream
     * @param header
     * @param psd
     * @throws IOException
     */
    public void write( IPcmData data, IDataStream stream, PacketHeader header,
        Pcm1SpecificData psd ) throws IOException
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IPcmDataSerializer<T extends IPcmData>
    {
        /**
         * @param stream
         * @param psd
         * @param size
         * @return
         * @throws IOException
         */
        public T read( IDataStream stream, Pcm1SpecificData psd, int size )
            throws IOException;

        /**
         * @param data
         * @param stream
         * @param psd
         * @param size
         * @throws IOException
         */
        public void write( T data, IDataStream stream, Pcm1SpecificData psd,
            int size ) throws IOException;
    }
}
