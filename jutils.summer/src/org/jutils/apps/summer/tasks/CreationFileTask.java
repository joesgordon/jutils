package org.jutils.apps.summer.tasks;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.jutils.apps.summer.data.SumFile;
import org.jutils.core.io.cksum.*;
import org.jutils.core.task.*;
import org.jutils.core.ui.hex.HexUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CreationFileTask implements IStatusTask
{
    /**  */
    private final SumFile sumFile;
    /**  */
    private final IChecksum checksummer;

    /***************************************************************************
     * @param sumFile
     * @param type
     **************************************************************************/
    public CreationFileTask( SumFile sumFile, ChecksumType type )
    {
        this.sumFile = sumFile;
        this.checksummer = CheckSumFactory.createSummer( type );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        TaskUpdater updater = new TaskUpdater( handler, sumFile.length );
        ChecksumGenenerator generator = new ChecksumGenenerator( checksummer,
            updater );

        try
        {
            byte [] csBytes = generator.generateChecksum( sumFile.file );

            sumFile.checksum = HexUtils.toHexString(
                HexUtils.asList( csBytes ) ).toLowerCase();
        }
        catch( FileNotFoundException ex )
        {
            ex.printStackTrace();
            handler.signalError(
                new TaskError( "File Not Found", ex.getMessage() ) );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            handler.signalError(
                new TaskError( "I/O Error", ex.getMessage() ) );
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return sumFile.path;
    }
}
