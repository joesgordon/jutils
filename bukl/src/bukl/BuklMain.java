package bukl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*******************************************************************************
 * Entry point for the Bukl Java bootstrap build tool.
 ******************************************************************************/
public final class BuklMain
{
    /***************************************************************************
     * 
     **************************************************************************/
    private BuklMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        try
        {
            int exitCode = run( args );

            if( exitCode != 0 )
            {
                System.exit( exitCode );
            }
        }
        catch( Exception ex )
        {
            System.err.println( "bukl failed: " + ex.getMessage() );
            ex.printStackTrace( System.err );
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * @param args
     * @return
     * @throws IOException
     **************************************************************************/
    public static int run( String [] args ) throws IOException
    {
        BuklOptions options = BuklOptions.parse( args );

        if( options.help )
        {
            printUsage();
            return 0;
        }

        Path configPath = options.configPath != null
            ? Paths.get( options.configPath )
            : Paths.get( "bukl.properties" );

        BuklRunner app = new BuklRunner( configPath, options.verbose,
            options.generateDocs );
        app.run();
        return 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void printUsage()
    {
        System.out.println(
            "Usage: java bukl.BuklMain [-v] [-d|--doc] [path-to-bukl.properties]" );
    }
}
