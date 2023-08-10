package org.jutils.core.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import org.jutils.core.ValidationException;
import org.jutils.core.io.DataStream;
import org.jutils.core.io.FileStream;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.IOUtils;
import org.jutils.core.io.LengthStringSerializer;
import org.jutils.core.io.LocalDateTimeSerializer;
import org.jutils.core.io.LogUtils;
import org.jutils.core.io.parsers.IpAddressParser;
import org.jutils.core.task.IStatusRunnable;
import org.jutils.core.task.IStatusTask;
import org.jutils.core.task.ITaskStatusHandler;
import org.jutils.core.task.NamedStatusTask;
import org.jutils.core.task.TaskUpdater;
import org.jutils.core.task.TaskView;
import org.jutils.core.ui.app.AppRunner;

/*******************************************************************************
 *
 ******************************************************************************/
public class NetMessageSerializerV0 implements IDataSerializer<NetMessage>
{
    /**  */
    private final LengthStringSerializer stringSerializer = new LengthStringSerializer();
    /**  */
    private final LocalDateTimeSerializer timeSerializer = new LocalDateTimeSerializer();

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage read( IDataStream stream )
        throws IOException, ValidationException
    {
        boolean received = stream.readBoolean();

        LocalDateTime time = timeSerializer.read( stream );

        String localAddress = stringSerializer.read( stream );
        int localPort = stream.readInt();

        String remoteAddress = stringSerializer.read( stream );
        int remotePort = stream.readInt();

        int contentsLen = stream.readInt();
        byte [] contents = new byte[contentsLen];

        stream.read( contents );

        EndPoint local = new EndPoint();
        EndPoint remote = new EndPoint();

        IpAddressParser addressParser = new IpAddressParser();

        local.address.set( addressParser.parse( localAddress ) );
        local.port = localPort;
        remote.address.set( addressParser.parse( remoteAddress ) );
        remote.port = remotePort;

        return new NetMessage( received, time, local, remote, contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( NetMessage msg, IDataStream stream ) throws IOException
    {
        throw new IllegalStateException( "Not implemented" );
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        if( args.length != 1 )
        {
            LogUtils.printError( "No input file of extension \"netmsgsold\"" );
            return;
        }

        File inFile = new File( args[0] );

        if( !inFile.isFile() )
        {
            LogUtils.printError( "Input file does not exist: %s",
                inFile.getAbsolutePath() );
            return;
        }

        AppRunner.invokeLater( () -> startTranslation( inFile ) );
    }

    /***************************************************************************
     * @param inFile
     **************************************************************************/
    private static void startTranslation( File inFile )
    {
        File outFile = IOUtils.replaceExtension( inFile,
            NetMessageSerializer.NETMSGS_EXT );
        IStatusRunnable runnable = ( h ) -> executeTranslation( inFile, outFile,
            h );
        IStatusTask task = new NamedStatusTask( "NetMsgs Translation",
            runnable );
        TaskView.startAndShow( null, task, "Translating " + inFile.getName() );

        System.exit( 0 );
    }

    /***************************************************************************
     * @param inFile
     * @param outFile
     * @param handler
     **************************************************************************/
    private static void executeTranslation( File inFile, File outFile,
        ITaskStatusHandler handler )
    {
        try( FileStream ifs = new FileStream( inFile, true );
             IDataStream ids = new DataStream( ifs ) )
        {
            try( FileStream ofs = new FileStream( outFile, false );
                 IDataStream ods = new DataStream( ofs ) )
            {
                executeTranslation( ids, ods, handler );
            }
            catch( FileNotFoundException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
            catch( ValidationException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
        catch( FileNotFoundException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param ids
     * @param ods
     * @param handler
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private static void executeTranslation( IDataStream ids, IDataStream ods,
        ITaskStatusHandler handler ) throws IOException, ValidationException
    {
        long inputSize = ids.getLength();
        TaskUpdater updater = new TaskUpdater( handler, inputSize );
        NetMessageSerializerV0 oldSerializer = new NetMessageSerializerV0();
        NetMessageSerializer newSerializer = new NetMessageSerializer();

        while( ids.getAvailable() > 0 )
        {
            NetMessage netMsg = oldSerializer.read( ids );

            newSerializer.write( netMsg, ods );

            updater.update( ids.getPosition() );
        }

        updater.update( inputSize );
    }
}
