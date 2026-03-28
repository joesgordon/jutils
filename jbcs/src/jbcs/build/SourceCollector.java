package jbcs.build;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import jbcs.config.JbcsConfig;
import jbcs.config.ProjectConfig;

/**
 * Collects Java source files from included project source directories.
 */
public final class SourceCollector
{
    public List<Path> collectSources( JbcsConfig config ) throws IOException
    {
        return collectJavaFiles( config, FileKind.SOURCE );
    }

    public List<Path> collectJavadocSources( JbcsConfig config )
        throws IOException
    {
        return collectJavaFiles( config, FileKind.JAVADOC );
    }

    public List<CollectedFile> collectSourceArchiveFiles( JbcsConfig config )
        throws IOException
    {
        List<CollectedFile> sourceFiles = new ArrayList<>();

        for( ProjectConfig project : config.getProjects() )
        {
            Path sourceDirectory = project.getSourceDirectory();

            if( !Files.isDirectory( sourceDirectory ) )
            {
                throw new IOException(
                    "Source directory not found: " + sourceDirectory );
            }

            sourceFiles.addAll( collectProjectFiles( project ) );
        }

        return sourceFiles;
    }

    public List<CollectedFile> collectResources( JbcsConfig config )
        throws IOException
    {
        List<CollectedFile> resourceFiles = new ArrayList<>();

        for( ProjectConfig project : config.getProjects() )
        {
            Path sourceDirectory = project.getSourceDirectory();

            if( !Files.isDirectory( sourceDirectory ) )
            {
                throw new IOException(
                    "Source directory not found: " + sourceDirectory );
            }

            resourceFiles.addAll( collectProjectResources( project ) );
        }

        return resourceFiles;
    }

    private List<Path> collectJavaFiles( JbcsConfig config, FileKind kind )
        throws IOException
    {
        List<Path> sourceFiles = new ArrayList<>();

        for( ProjectConfig project : config.getProjects() )
        {
            Path sourceDirectory = project.getSourceDirectory();

            if( !Files.isDirectory( sourceDirectory ) )
            {
                throw new IOException(
                    "Source directory not found: " + sourceDirectory );
            }

            sourceFiles.addAll( collectProjectJavaFiles( project, kind ) );
        }

        return sourceFiles;
    }

    private List<Path> collectProjectJavaFiles( ProjectConfig project,
        FileKind kind ) throws IOException
    {
        Path sourceDirectory = project.getSourceDirectory();
        List<PathMatcher> excludeMatchers = createMatchers( sourceDirectory,
            kind == FileKind.JAVADOC ? project.getJavadocExcludes()
                : project.getSourceExcludes() );
        List<Path> sourceFiles = new ArrayList<>();

        FileVisitor<Path> visitor = new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile( Path file,
                BasicFileAttributes attrs )
            {
                Path relativePath = sourceDirectory.relativize( file );

                if( relativePath.toString().endsWith( ".java" ) &&
                    !matchesAny( relativePath, excludeMatchers ) )
                {
                    sourceFiles.add( file );
                }

                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree( sourceDirectory, visitor );

        return sourceFiles;
    }

    private List<CollectedFile> collectProjectFiles( ProjectConfig project )
        throws IOException
    {
        Path sourceDirectory = project.getSourceDirectory();
        List<PathMatcher> excludeMatchers = createMatchers( sourceDirectory,
            project.getSourceExcludes() );
        List<CollectedFile> files = new ArrayList<>();

        FileVisitor<Path> visitor = new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile( Path file,
                BasicFileAttributes attrs )
            {
                Path relativePath = sourceDirectory.relativize( file );

                if( !matchesAny( relativePath, excludeMatchers ) )
                {
                    files.add( new CollectedFile( file, relativePath ) );
                }

                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree( sourceDirectory, visitor );

        return files;
    }

    private List<CollectedFile> collectProjectResources( ProjectConfig project )
        throws IOException
    {
        Path sourceDirectory = project.getSourceDirectory();
        List<PathMatcher> excludeMatchers = createMatchers( sourceDirectory,
            project.getResourceExcludes() );
        List<CollectedFile> files = new ArrayList<>();

        FileVisitor<Path> visitor = new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult visitFile( Path file,
                BasicFileAttributes attrs )
            {
                Path relativePath = sourceDirectory.relativize( file );

                if( !relativePath.toString().endsWith( ".java" ) &&
                    !matchesAny( relativePath, excludeMatchers ) )
                {
                    files.add( new CollectedFile( file, relativePath ) );
                }

                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree( sourceDirectory, visitor );

        return files;
    }

    /**
     * @param sourceDirectory
     * @param patterns
     * @return
     */
    @SuppressWarnings( "resource")
    private List<PathMatcher> createMatchers( Path sourceDirectory,
        List<String> patterns )
    {
        List<PathMatcher> matchers = new ArrayList<>();

        for( String pattern : patterns )
        {
            matchers.add( sourceDirectory.getFileSystem().getPathMatcher(
                "glob:" + pattern ) );
        }

        return matchers;
    }

    /***************************************************************************
     * @param relativePath
     * @param matchers
     * @return
     **************************************************************************/
    private boolean matchesAny( Path relativePath, List<PathMatcher> matchers )
    {
        for( PathMatcher matcher : matchers )
        {
            if( matcher.matches( relativePath ) )
            {
                return true;
            }
        }

        return false;
    }

    private enum FileKind
    {
        SOURCE,
        JAVADOC
    }

    public static final class CollectedFile
    {
        private final Path sourceFile;
        private final Path relativePath;

        public CollectedFile( Path sourceFile, Path relativePath )
        {
            this.sourceFile = sourceFile;
            this.relativePath = relativePath;
        }

        public Path getSourceFile()
        {
            return sourceFile;
        }

        public Path getRelativePath()
        {
            return relativePath;
        }
    }
}
