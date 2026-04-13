package bukl.build;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import bukl.config.BuklConfig;

/*******************************************************************************
 * Creates jar files from compiled output directories.
 ******************************************************************************/
public final class JarArchiver
{
    /***************************************************************************
     * @param inputDirectory
     * @param jarFile
     * @throws IOException
     **************************************************************************/
    public void createPlainJar( Path inputDirectory, Path jarFile )
        throws IOException
    {
        createJar( inputDirectory, jarFile, List.of(), null );
    }

    /***************************************************************************
     * @param inputDirectory
     * @param jarFile
     * @param dependencyJars
     * @param config
     * @throws IOException
     **************************************************************************/
    public void createJar( Path inputDirectory, Path jarFile,
        Iterable<Path> dependencyJars, BuklConfig config ) throws IOException
    {
        Files.createDirectories( jarFile.getParent() );

        try( OutputStream out = Files.newOutputStream( jarFile );
             JarOutputStream jarOut = createJarOutputStream( out, config ) )
        {
            Set<String> writtenEntries = new HashSet<>();

            Files.walkFileTree( inputDirectory, new SimpleFileVisitor<>()
            {
                @Override
                public FileVisitResult visitFile( Path file,
                    BasicFileAttributes attrs ) throws IOException
                {
                    String entryName = normalizeEntryName(
                        inputDirectory.relativize( file ).toString() );
                    addParentDirectories( jarOut, writtenEntries, entryName );
                    addFileEntry( jarOut, writtenEntries, entryName, file,
                        attrs.lastModifiedTime().toMillis() );

                    return FileVisitResult.CONTINUE;
                }
            } );

            for( Path dependencyJar : dependencyJars )
            {
                addDependencyJar( jarOut, writtenEntries, dependencyJar );
            }
        }

        System.out.println( "Created jar: " + jarFile );
    }

    /***************************************************************************
     * @param out
     * @param config
     * @return
     * @throws IOException
     **************************************************************************/
    private JarOutputStream createJarOutputStream( OutputStream out,
        BuklConfig config ) throws IOException
    {
        if( config == null )
        {
            return new JarOutputStream( out );
        }

        return new JarOutputStream( out, createManifest( config ) );
    }

    /***************************************************************************
     * @param config
     * @return
     **************************************************************************/
    private Manifest createManifest( BuklConfig config )
    {
        Manifest manifest = new Manifest();
        Attributes attributes = manifest.getMainAttributes();
        attributes.put( Attributes.Name.MANIFEST_VERSION, "1.0" );

        if( config.getMainClass() != null && !config.getMainClass().isBlank() )
        {
            attributes.put( Attributes.Name.MAIN_CLASS, config.getMainClass() );
        }

        return manifest;
    }

    /***************************************************************************
     * @param jarOut
     * @param writtenEntries
     * @param dependencyJar
     * @throws IOException
     **************************************************************************/
    private void addDependencyJar( JarOutputStream jarOut,
        Set<String> writtenEntries, Path dependencyJar ) throws IOException
    {
        try( JarFile jarFile = new JarFile( dependencyJar.toFile() ) )
        {
            var entries = jarFile.entries();

            while( entries.hasMoreElements() )
            {
                JarEntry sourceEntry = entries.nextElement();
                String entryName = sourceEntry.getName();

                if( sourceEntry.isDirectory() || shouldSkipEntry( entryName ) )
                {
                    continue;
                }

                addParentDirectories( jarOut, writtenEntries, entryName );

                if( !writtenEntries.add( entryName ) )
                {
                    continue;
                }

                JarEntry newEntry = new JarEntry( entryName );
                newEntry.setTime( sourceEntry.getTime() );
                jarOut.putNextEntry( newEntry );

                try( InputStream input = jarFile.getInputStream( sourceEntry ) )
                {
                    input.transferTo( jarOut );
                }

                jarOut.closeEntry();
            }
        }
    }

    /***************************************************************************
     * @param jarOut
     * @param writtenEntries
     * @param entryName
     * @param file
     * @param modifiedTime
     * @throws IOException
     **************************************************************************/
    private void addFileEntry( JarOutputStream jarOut,
        Set<String> writtenEntries, String entryName, Path file,
        long modifiedTime ) throws IOException
    {
        if( shouldSkipEntry( entryName ) || !writtenEntries.add( entryName ) )
        {
            return;
        }

        JarEntry entry = new JarEntry( entryName );
        entry.setTime( modifiedTime );

        jarOut.putNextEntry( entry );
        Files.copy( file, jarOut );
        jarOut.closeEntry();
    }

    /***************************************************************************
     * @param jarOut
     * @param writtenEntries
     * @param entryName
     * @throws IOException
     **************************************************************************/
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

    /***************************************************************************
     * @param entryName
     * @return
     **************************************************************************/
    private boolean shouldSkipEntry( String entryName )
    {
        String normalized = entryName.replace( '\\', '/' );
        return normalized.isBlank() || normalized.endsWith( "/" ) ||
            "META-INF/MANIFEST.MF".equalsIgnoreCase( normalized ) ||
            normalized.endsWith( ".SF" ) || normalized.endsWith( ".DSA" ) ||
            normalized.endsWith( ".RSA" );
    }

    /***************************************************************************
     * @param relativePath
     * @return
     **************************************************************************/
    private String normalizeEntryName( String relativePath )
    {
        return relativePath.replace( '\\', '/' );
    }
}
