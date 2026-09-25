package bukl;

import java.io.IOException;
import java.nio.file.Path;

/*******************************************************************************
 * Entry point for the Bukl Java bootstrap build tool.
 ******************************************************************************/
public final class BuklMain
{
    /***************************************************************************
     * Declare the default and only constructor private to prevent instances.
     **************************************************************************/
    private BuklMain()
    {
    }

    /***************************************************************************
     * @param args arguments represented by {@link BuklOptions}.
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
     * @param args arguments represented by {@link BuklOptions}.
     * @return the exit code for the application.
     * @throws IOException any I/O error that occurs.
     **************************************************************************/
    public static int run( String [] args ) throws IOException
    {
        BuklOptions options = BuklOptions.parse( args );

        if( options.help )
        {
            BuklOptions.printUsage();
            return 0;
        }

        Path configPath = options.getPath();

        BuklRunner app = new BuklRunner( configPath, options.verbose,
            options.generateDocs );
        app.run();

        return 0;
    }
}
