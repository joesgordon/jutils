package jbcs.build;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DocumentationTool;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import jbcs.config.JbcsConfig;

/**
 * Generates aggregate javadocs from the configured source set.
 */
public final class JavadocGenerator
{
    public void generate( JbcsConfig config, List<Path> sourceFiles )
        throws IOException
    {
        if( sourceFiles.isEmpty() )
        {
            System.out.println( "No javadoc source files found" );
            return;
        }

        DocumentationTool tool = ToolProvider.getSystemDocumentationTool();

        if( tool == null )
        {
            throw new IOException(
                "No system javadoc tool is available. Run jcbs with a JDK." );
        }

        try( StandardJavaFileManager fileManager = tool.getStandardFileManager(
            null, null, null ) )
        {
            if( !config.getDependencies().isEmpty() )
            {
                fileManager.setLocationFromPaths( StandardLocation.CLASS_PATH,
                    config.getDependencies() );
            }

            List<String> options = new ArrayList<>();
            options.add( "-d" );
            options.add( config.getJavadocDirectory().toString() );
            options.add( "-quiet" );
            options.add( "-Xdoclint:none" );

            if( config.getJavaVersion() != null &&
                !config.getJavaVersion().isBlank() )
            {
                options.add( "--release" );
                options.add( config.getJavaVersion() );
            }

            System.out.println( "Generating javadocs for " +
                sourceFiles.size() + " source files" );

            StringWriter output = new StringWriter();

            boolean success = tool.getTask( output, fileManager, null, null,
                options,
                fileManager.getJavaFileObjectsFromPaths( sourceFiles ) ).call();

            if( !success )
            {
                String details = output.toString().trim();

                if( details.isEmpty() )
                {
                    throw new IOException( "Javadoc generation failed" );
                }

                throw new IOException(
                    "Javadoc generation failed\n" + details );
            }
        }
    }
}
