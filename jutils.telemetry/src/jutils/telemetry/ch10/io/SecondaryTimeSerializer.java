package jutils.telemetry.ch10.io;

import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.IDataSerializer;
import jutils.core.io.IDataStream;
import jutils.core.io.IKeyedSerializer;
import jutils.core.io.KeyedSerializer;
import jutils.telemetry.ch10.ISecondaryTime;
import jutils.telemetry.ch10.SecondaryHeaderTimeFormat;
import jutils.telemetry.ch10.SecondaryHeader.Chapter4Time;
import jutils.telemetry.ch10.SecondaryHeader.ErtcTime;
import jutils.telemetry.ch10.SecondaryHeader.Ieee1588Time;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SecondaryTimeSerializer
    implements IKeyedSerializer<SecondaryHeaderTimeFormat, ISecondaryTime>
{
    /**  */
    private final KeyedSerializer<SecondaryHeaderTimeFormat,
        ISecondaryTime> timeSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public SecondaryTimeSerializer()
    {
        this.timeSerializer = new KeyedSerializer<>();

        this.timeSerializer.put( SecondaryHeaderTimeFormat.CHAPTER4,
            new Chapter4TimeSerializer() );
        this.timeSerializer.put( SecondaryHeaderTimeFormat.IEEE_1588,
            new Ieee1588TimeSerializer() );
        this.timeSerializer.put( SecondaryHeaderTimeFormat.ERTC,
            new ErtcTimeSerializer() );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ISecondaryTime read( IDataStream stream,
        SecondaryHeaderTimeFormat format )
        throws IOException, ValidationException
    {
        return timeSerializer.read( stream, format );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( ISecondaryTime time, IDataStream stream,
        SecondaryHeaderTimeFormat format ) throws IOException
    {
        timeSerializer.write( time, stream, format );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class Chapter4TimeSerializer
        implements IDataSerializer<Chapter4Time>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Chapter4Time read( IDataStream stream )
            throws IOException, ValidationException
        {
            Chapter4Time time = new Chapter4Time();

            time.microseconds = stream.readShort();
            time.reserved = stream.readShort();
            time.highOrderTime = stream.readShort();
            time.lowOrderTime = stream.readShort();

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Chapter4Time time, IDataStream stream )
            throws IOException
        {
            stream.writeShort( time.microseconds );
            stream.writeShort( time.reserved );
            stream.writeShort( time.highOrderTime );
            stream.writeShort( time.lowOrderTime );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class Ieee1588TimeSerializer
        implements IDataSerializer<Ieee1588Time>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public Ieee1588Time read( IDataStream stream )
            throws IOException, ValidationException
        {
            Ieee1588Time time = new Ieee1588Time();

            time.nanoseconds = stream.readInt();
            time.seconds = stream.readInt();

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( Ieee1588Time time, IDataStream stream )
            throws IOException
        {
            stream.writeInt( time.nanoseconds );
            stream.writeInt( time.seconds );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ErtcTimeSerializer
        implements IDataSerializer<ErtcTime>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public ErtcTime read( IDataStream stream )
            throws IOException, ValidationException
        {
            ErtcTime time = new ErtcTime();

            time.nanoseconds = stream.readLong();

            return time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write( ErtcTime time, IDataStream stream )
            throws IOException
        {
            stream.writeLong( time.nanoseconds );
        }
    }
}
