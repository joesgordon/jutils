package org.jutils.core.pcap;

import java.io.File;
import java.io.IOException;

import org.jutils.core.ValidationException;
import org.jutils.core.io.DataStream;
import org.jutils.core.io.FileStream;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;
import org.jutils.core.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BlockSerializer implements IDataSerializer<IBlock>
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IBlock read( IDataStream stream )
        throws IOException, ValidationException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( IBlock data, IDataStream stream ) throws IOException
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        if( args.length == 1 )
        {
            File f = new File( args[0] );

            try( FileStream fs = new FileStream( f );
                 DataStream ds = new DataStream( fs ) )
            {
                BlockSerializer bs = new BlockSerializer();

                while( ds.getAvailable() > 0 )
                {
                    IBlock block = bs.read( ds );

                    LogUtils.printDebug( "Read %X", block.id );
                }
            }
            catch( IOException | ValidationException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }
}
