package bukl.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/*******************************************************************************
 * Loads bukl.properties and optional bukl_proj.properties files.
 ******************************************************************************/
public final class BuklConfigLoader
{
    /**  */
    private static final String PROJECT_CONFIG_FILE = "bukl_proj.properties";

    /***************************************************************************
     * @param configPath
     * @return
     * @throws IOException
     **************************************************************************/
    public BuklConfig load( Path configPath ) throws IOException
    {
        Path resolvedConfig = configPath.toAbsolutePath().normalize();

        if( !Files.isRegularFile( resolvedConfig ) )
        {
            throw new IOException( "Config file not found: " + resolvedConfig );
        }

        Properties props = loadProperties( resolvedConfig );
        Path rootDirectory = Objects.requireNonNull( resolvedConfig.getParent(),
            "Config file must have a parent directory" );

        String buildName = requireValue( props, "build.name" );
        List<String> projectNames = splitCsv(
            requireValue( props, "build.projects" ) );

        if( projectNames.isEmpty() )
        {
            throw new IOException(
                "Property build.projects must not be empty" );
        }

        String javaVersion = trimToNull(
            props.getProperty( "build.java.version" ) );
        if( javaVersion == null )
        {
            javaVersion = Runtime.version().feature() + "";
        }

        String mainClass = trimToNull(
            props.getProperty( "build.main.class" ) );

        Path outputDirectory = resolvePath( rootDirectory,
            props.getProperty( "build.output.dir" ), "build" );
        Path classesDirectory = resolvePath( rootDirectory,
            props.getProperty( "build.output.classes.dir" ), "build/release" );
        Path debugClassesDirectory = resolvePath( rootDirectory,
            props.getProperty( "build.output.debug.classes.dir" ),
            "build/debug" );
        Path javadocDirectory = resolvePath( rootDirectory,
            props.getProperty( "build.output.doc.dir" ), "build/docs/javadoc" );
        Path releaseJar = resolvePath( rootDirectory,
            props.getProperty( "build.output.release.jar" ),
            "build/single/" + buildName + ".jar" );
        Path debugJar = resolvePath( rootDirectory,
            props.getProperty( "build.output.debug.jar" ),
            "build/single/" + buildName + "-debug.jar" );
        Path sourcesJar = resolvePath( rootDirectory,
            props.getProperty( "build.output.sources.jar" ),
            "build/single/" + buildName + "-src.jar" );
        Path javadocJar = resolvePath( rootDirectory,
            props.getProperty( "build.output.javadoc.jar" ),
            "build/single/" + buildName + "-javadoc.jar" );

        List<Path> dependencies = new ArrayList<>();
        for( String dependency : splitCsv(
            props.getProperty( "build.dependencies" ) ) )
        {
            dependencies.add( rootDirectory.resolve( dependency ).normalize() );
        }

        List<String> topLevelSourceExcludes = splitCsv(
            props.getProperty( "build.source.exclude" ) );
        List<String> topLevelResourceExcludes = splitCsvWithDefault(
            props.getProperty( "build.resource.exclude" ), "**/*.java" );
        List<String> topLevelJavadocExcludes = splitCsv(
            props.getProperty( "build.javadoc.exclude" ) );

        String defaultSourceDirectory = defaultValue(
            props.getProperty( "build.source.dir" ), "src" );

        List<ProjectConfig> projects = new ArrayList<>();
        for( String projectName : projectNames )
        {
            Path projectDirectory = rootDirectory.resolve(
                projectName ).normalize();
            Properties projectProps = loadOptionalProjectProperties(
                projectDirectory );

            String projectSourceDirectoryName = defaultValue(
                projectProps.getProperty( "project.source.dir" ),
                defaultSourceDirectory );

            Path sourceDirectory = projectDirectory.resolve(
                projectSourceDirectoryName ).normalize();

            List<String> projectSourceExcludes = mergeLists(
                topLevelSourceExcludes, splitCsv(
                    projectProps.getProperty( "project.source.exclude" ) ) );
            List<String> projectResourceExcludes = mergeLists(
                topLevelResourceExcludes, splitCsv(
                    projectProps.getProperty( "project.resource.exclude" ) ) );
            List<String> projectJavadocExcludes = mergeLists(
                topLevelJavadocExcludes, splitCsv(
                    projectProps.getProperty( "project.javadoc.exclude" ) ) );

            projects.add( new ProjectConfig( projectName, projectDirectory,
                sourceDirectory, projectSourceExcludes, projectResourceExcludes,
                projectJavadocExcludes ) );
        }

        return new BuklConfig( resolvedConfig, rootDirectory, buildName,
            javaVersion, mainClass, dependencies, outputDirectory,
            classesDirectory, debugClassesDirectory, javadocDirectory,
            releaseJar, debugJar, sourcesJar, javadocJar,
            topLevelResourceExcludes, topLevelSourceExcludes,
            topLevelJavadocExcludes, projects );
    }

    /***************************************************************************
     * @param projectDirectory
     * @return
     * @throws IOException
     **************************************************************************/
    private Properties loadOptionalProjectProperties( Path projectDirectory )
        throws IOException
    {
        Path projectConfig = projectDirectory.resolve( PROJECT_CONFIG_FILE );

        if( !Files.isRegularFile( projectConfig ) )
        {
            return new Properties();
        }

        return loadProperties( projectConfig );
    }

    /***************************************************************************
     * @param path
     * @return
     * @throws IOException
     **************************************************************************/
    private Properties loadProperties( Path path ) throws IOException
    {
        Properties props = new Properties();

        try( InputStream input = Files.newInputStream( path ) )
        {
            props.load( input );
        }

        return props;
    }

    /***************************************************************************
     * @param props
     * @param key
     * @return
     * @throws IOException
     **************************************************************************/
    private static String requireValue( Properties props, String key )
        throws IOException
    {
        String value = trimToNull( props.getProperty( key ) );

        if( value == null )
        {
            throw new IOException( "Missing required property: " + key );
        }

        return value;
    }

    /***************************************************************************
     * @param value
     * @param defaultValue
     * @return
     **************************************************************************/
    private static String defaultValue( String value, String defaultValue )
    {
        String trimmed = trimToNull( value );
        return trimmed == null ? defaultValue : trimmed;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private static String trimToNull( String value )
    {
        if( value == null )
        {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private static List<String> splitCsv( String value )
    {
        List<String> items = new ArrayList<>();
        String trimmed = trimToNull( value );

        if( trimmed == null )
        {
            return items;
        }

        for( String item : trimmed.split( "," ) )
        {
            String candidate = trimToNull( item );

            if( candidate != null )
            {
                items.add( candidate );
            }
        }

        return items;
    }

    /***************************************************************************
     * @param value
     * @param fallback
     * @return
     **************************************************************************/
    private static List<String> splitCsvWithDefault( String value,
        String fallback )
    {
        List<String> items = splitCsv( value );

        if( items.isEmpty() )
        {
            items.add( fallback );
        }

        return items;
    }

    /***************************************************************************
     * @param first
     * @param second
     * @return
     **************************************************************************/
    private static List<String> mergeLists( List<String> first,
        List<String> second )
    {
        List<String> merged = new ArrayList<>( first );
        merged.addAll( second );
        return merged;
    }

    /***************************************************************************
     * @param rootDirectory
     * @param value
     * @param fallback
     * @return
     **************************************************************************/
    private static Path resolvePath( Path rootDirectory, String value,
        String fallback )
    {
        String text = defaultValue( value, fallback );
        return rootDirectory.resolve( text ).normalize();
    }
}
