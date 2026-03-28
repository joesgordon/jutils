package jbcs.build;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jbcs.build.SourceCollector.CollectedFile;

/**
 * Copies non-Java source-tree resources into compiled output directories.
 */
public final class ResourceCopier
{
    public void copyResources( List<CollectedFile> resources,
        Path outputDirectory ) throws IOException
    {
        for( CollectedFile resource : resources )
        {
            Path destination = outputDirectory.resolve(
                resource.getRelativePath() );
            Path parent = destination.getParent();

            if( parent != null )
            {
                Files.createDirectories( parent );
            }

            Files.copy( resource.getSourceFile(), destination,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES );
        }

        System.out.println(
            "Copied " + resources.size() + " resources to " + outputDirectory );
    }
}
