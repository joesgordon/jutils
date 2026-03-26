package jbcs.build;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import jbcs.build.SourceCollector.CollectedFile;

/**
 * Creates source jars from collected source-tree files.
 */
public final class SourceArchiveBuilder
{
    public void createSourceJar( List<CollectedFile> files, Path jarFile )
        throws IOException
    {
        Files.createDirectories( jarFile.getParent() );

        try( OutputStream out = Files.newOutputStream( jarFile );
             JarOutputStream jarOut = new JarOutputStream( out ) )
        {
            Set<String> writtenEntries = new HashSet<>();

            for( CollectedFile file : files )
            {
                String entryName = normalizeEntryName(
                    file.getRelativePath().toString() );
                addParentDirectories( jarOut, writtenEntries, entryName );

                if( !writtenEntries.add( entryName ) )
                {
                    continue;
                }

                JarEntry entry = new JarEntry( entryName );
                entry.setTime( Files.getLastModifiedTime(
                    file.getSourceFile() ).toMillis() );

                jarOut.putNextEntry( entry );
                Files.copy( file.getSourceFile(), jarOut );
                jarOut.closeEntry();
            }
        }

        System.out.println( "Created jar: " + jarFile );
    }

    private void addParentDirectories( JarOutputStream jarOut,
        Set<String> writtenEntries, String entryName ) throws IOException
    {
        int slashIndex = entryName.indexOf( '/' );

        while( slashIndex >= 0 )
        {
            String dirName = entryName.substring( 0, slashIndex + 1 );

            if( writtenEntries.add( dirName ) )
            {
                JarEntry dirEntry = new JarEntry( dirName );
                jarOut.putNextEntry( dirEntry );
                jarOut.closeEntry();
            }

            slashIndex = entryName.indexOf( '/', slashIndex + 1 );
        }
    }

    private String normalizeEntryName( String relativePath )
    {
        return relativePath.replace( '\\', '/' );
    }
}
