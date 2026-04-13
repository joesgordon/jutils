package bukl.build;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import bukl.config.BuklConfig;

/*******************************************************************************
 * Compiles aggregate Java source files into configured output directories.
 ******************************************************************************/
public final class JavaSourceCompiler
{
    /***************************************************************************
     * @param config
     * @param sourceFiles
     * @throws IOException
     **************************************************************************/
    public void compileRelease( BuklConfig config, List<Path> sourceFiles )
        throws IOException
    {
        compile( config, sourceFiles, config.getClassesDirectory(), false );
    }

    /***************************************************************************
     * @param config
     * @param sourceFiles
     * @throws IOException
     **************************************************************************/
    public void compileDebug( BuklConfig config, List<Path> sourceFiles )
        throws IOException
    {
        compile( config, sourceFiles, config.getDebugClassesDirectory(), true );
    }

    /***************************************************************************
     * @param config
     * @param sourceFiles
     * @param outputDirectory
     * @param debug
     * @throws IOException
     **************************************************************************/
    private void compile( BuklConfig config, List<Path> sourceFiles,
        Path outputDirectory, boolean debug ) throws IOException
    {
        if( sourceFiles.isEmpty() )
        {
            System.out.println(
                "No source files found for " + outputDirectory );
            return;
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        if( compiler == null )
        {
            throw new IOException(
                "No system Java compiler is available. Run bukl with a JDK." );
        }

        try( StandardJavaFileManager fileManager = compiler.getStandardFileManager(
            null, null, null ) )
        {
            if( !config.getDependencies().isEmpty() )
            {
                fileManager.setLocationFromPaths( StandardLocation.CLASS_PATH,
                    config.getDependencies() );
            }

            List<String> options = new ArrayList<>();
            options.add( "-d" );
            options.add( outputDirectory.toString() );
            options.add( debug ? "-g" : "-g:none" );

            if( config.getJavaVersion() != null &&
                !config.getJavaVersion().isBlank() )
            {
                options.add( "--release" );
                options.add( config.getJavaVersion() );
            }

            System.out.println( "Compiling " + sourceFiles.size() +
                " source files to " + outputDirectory + " (" +
                ( debug ? "debug" : "release" ) + ")" );

            boolean success = compiler.getTask( null, fileManager, null,
                options, null,
                fileManager.getJavaFileObjectsFromPaths( sourceFiles ) ).call();

            if( !success )
            {
                throw new IOException(
                    "Compilation failed for output directory: " +
                        outputDirectory );
            }
        }
    }
}
