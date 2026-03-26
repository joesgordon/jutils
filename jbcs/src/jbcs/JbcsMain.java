package jbcs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * Entry point for the jcbs bootstrap build tool.
 ******************************************************************************/
public final class JbcsMain
{
    /***************************************************************************
     * 
     **************************************************************************/
    private JbcsMain()
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
            System.err.println( "jcbs failed: " + ex.getMessage() );
            ex.printStackTrace( System.err );
            System.exit( 1 );
        }
    }

    /***************************************************************************
     * @param args
     * @return
     * @throws IOException
     **************************************************************************/
    static int run( String [] args ) throws IOException
    {
        Options options = parseOptions( args );

        if( options.help )
        {
            printUsage();
            return 0;
        }

        Path configPath = options.configPath != null
            ? Paths.get( options.configPath )
            : Paths.get( "jcbs.properties" );

        Jbcs app = new Jbcs( configPath, options.verbose,
            options.generateDocs );
        app.run();
        return 0;
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
    private static void printUsage()
    {
        System.out.println(
            "Usage: java jbcs.JbcsMain [-v] [-d|--doc] [path-to-jcbs.properties]" );
    }

    /***************************************************************************
     * @param args
     * @return
     * @throws IOException
     **************************************************************************/
    private static Options parseOptions( String [] args ) throws IOException
    {
        Options options = new Options();
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
     * 
     **************************************************************************/
    private static final class Options
    {
        /**  */
        boolean help;
        /**  */
        boolean verbose;
        /**  */
        boolean generateDocs;
        /**  */
        String configPath;
    }
}
