package bukl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Defines the data that represents the command line arguments for the
 * application.
 ******************************************************************************/
public class BuklOptions
{
    /** If help is to be displayed. */
    public boolean help;
    /** If verbose information is enabled. */
    public boolean verbose;
    /** If javadoc are to be generated. */
    public boolean generateDocs;
    /** Path to a properties file. */
    public String configPath;

    /***************************************************************************
     * 
     **************************************************************************/
    public BuklOptions()
    {
        this.help = false;
        this.verbose = false;
        this.generateDocs = false;
        this.configPath = null;
    }

    /***************************************************************************
     * @param args
     * @return
     * @throws IOException
     **************************************************************************/
    public static BuklOptions parse( String [] args ) throws IOException
    {
        BuklOptions options = new BuklOptions();
        List<String> positional = new ArrayList<>();

        for( String arg : args )
        {
            if( isHelp( arg ) )
            {
                options.help = true;
            }
            else if( "-v".equals( arg ) || "--verbose".equals( arg ) )
            {
                options.verbose = true;
            }
            else if( "-d".equals( arg ) || "--doc".equals( arg ) )
            {
                options.generateDocs = true;
            }
            else if( arg.startsWith( "-" ) )
            {
                throw new IOException( "Unknown option: " + arg );
            }
            else
            {
                positional.add( arg );
            }
        }

        if( positional.size() > 1 )
        {
            throw new IOException( "Too many positional arguments" );
        }

        if( !positional.isEmpty() )
        {
            options.configPath = positional.get( 0 );
        }

        return options;
    }

    /***************************************************************************
     * @param arg
     * @return
     **************************************************************************/
    private static boolean isHelp( String arg )
    {
        return "-h".equals( arg ) || "--help".equals( arg ) ||
            "help".equals( arg );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static void printUsage()
    {
        System.out.println(
            "Usage: java bukl.BuklMain [-v] [-d|--doc] [path-to-bukl.properties]" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getPath()
    {
        return configPath != null ? Paths.get( configPath )
            : Paths.get( "bukl.properties" );
    }
}
