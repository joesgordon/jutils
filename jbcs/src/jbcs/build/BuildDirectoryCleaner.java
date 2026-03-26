package jbcs.build;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;

/**
 * Recreates the configured build directory so each run starts clean.
 */
public final class BuildDirectoryCleaner
{
    public void recreateDirectory( Path directory ) throws IOException
    {
        if( Files.exists( directory ) )
        {
            deleteRecursively( directory );
        }

        Files.createDirectories( directory );
    }

    private void deleteRecursively( Path directory ) throws IOException
    {
        Files.walkFileTree( directory, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile( Path file,
                BasicFileAttributes attrs ) throws IOException
            {
                clearReadOnly( file );
                Files.delete( file );
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory( Path dir,
                IOException exc ) throws IOException
            {
                if( exc != null )
                {
                    throw exc;
                }

                clearReadOnly( dir );
                Files.delete( dir );
                return FileVisitResult.CONTINUE;
            }
        } );
    }

    private void clearReadOnly( Path path ) throws IOException
    {
        DosFileAttributeView dosView = Files.getFileAttributeView( path,
            DosFileAttributeView.class );

        if( dosView != null )
        {
            dosView.setReadOnly( false );
        }
    }
}
