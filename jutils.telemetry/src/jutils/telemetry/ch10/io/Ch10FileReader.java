package jutils.telemetry.ch10.io;

import java.io.File;
import java.io.IOException;

import jutils.core.ValidationException;
import jutils.core.io.DataStream;
import jutils.core.io.FileStream;
import jutils.core.io.IDataStream;
import jutils.core.io.IReader;
import jutils.core.utils.ByteOrdering;
import jutils.telemetry.ch10.Ch10File;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ch10FileReader implements IReader<Ch10File, File>
{
    /**  */
    private final Ch10StreamReader reader;

    /***************************************************************************
     * 
     **************************************************************************/
    public Ch10FileReader()
    {
        this.reader = new Ch10StreamReader();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @SuppressWarnings( "resource")
    @Override
    public Ch10File read( File file ) throws IOException, ValidationException
    {
        FileStream fs = new FileStream( file, true );
        IDataStream stream = new DataStream( fs, ByteOrdering.INTEL_ORDER );
        Ch10File c10 = reader.read( stream );

        c10.name = file.getName();

        return c10;
    }
}
