package jbcs.config;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * Resolved top-level jcbs configuration.
 */
public final class JbcsConfig
{
    private final Path configFile;
    private final Path rootDirectory;
    private final String buildName;
    private final String javaVersion;
    private final String mainClass;
    private final List<Path> dependencies;
    private final Path outputDirectory;
    private final Path classesDirectory;
    private final Path debugClassesDirectory;
    private final Path javadocDirectory;
    private final Path releaseJar;
    private final Path debugJar;
    private final Path sourcesJar;
    private final Path javadocJar;
    private final List<String> resourceExcludes;
    private final List<String> sourceExcludes;
    private final List<String> javadocExcludes;
    private final List<ProjectConfig> projects;

    public JbcsConfig( Path configFile, Path rootDirectory, String buildName,
        String javaVersion, String mainClass, List<Path> dependencies,
        Path outputDirectory, Path classesDirectory, Path debugClassesDirectory,
        Path javadocDirectory, Path releaseJar, Path debugJar, Path sourcesJar,
        Path javadocJar, List<String> resourceExcludes,
        List<String> sourceExcludes, List<String> javadocExcludes,
        List<ProjectConfig> projects )
    {
        this.configFile = configFile;
        this.rootDirectory = rootDirectory;
        this.buildName = buildName;
        this.javaVersion = javaVersion;
        this.mainClass = mainClass;
        this.dependencies = List.copyOf( dependencies );
        this.outputDirectory = outputDirectory;
        this.classesDirectory = classesDirectory;
        this.debugClassesDirectory = debugClassesDirectory;
        this.javadocDirectory = javadocDirectory;
        this.releaseJar = releaseJar;
        this.debugJar = debugJar;
        this.sourcesJar = sourcesJar;
        this.javadocJar = javadocJar;
        this.resourceExcludes = List.copyOf( resourceExcludes );
        this.sourceExcludes = List.copyOf( sourceExcludes );
        this.javadocExcludes = List.copyOf( javadocExcludes );
        this.projects = List.copyOf( projects );
    }

    public Path getConfigFile()
    {
        return configFile;
    }

    public Path getRootDirectory()
    {
        return rootDirectory;
    }

    public String getBuildName()
    {
        return buildName;
    }

    public String getJavaVersion()
    {
        return javaVersion;
    }

    public String getMainClass()
    {
        return mainClass;
    }

    public List<Path> getDependencies()
    {
        return Collections.unmodifiableList( dependencies );
    }

    public Path getOutputDirectory()
    {
        return outputDirectory;
    }

    public Path getClassesDirectory()
    {
        return classesDirectory;
    }

    public Path getDebugClassesDirectory()
    {
        return debugClassesDirectory;
    }

    public Path getJavadocDirectory()
    {
        return javadocDirectory;
    }

    public Path getReleaseJar()
    {
        return releaseJar;
    }

    public Path getDebugJar()
    {
        return debugJar;
    }

    public Path getSourcesJar()
    {
        return sourcesJar;
    }

    public Path getJavadocJar()
    {
        return javadocJar;
    }

    public List<String> getResourceExcludes()
    {
        return Collections.unmodifiableList( resourceExcludes );
    }

    public List<String> getSourceExcludes()
    {
        return Collections.unmodifiableList( sourceExcludes );
    }

    public List<String> getJavadocExcludes()
    {
        return Collections.unmodifiableList( javadocExcludes );
    }

    public List<ProjectConfig> getProjects()
    {
        return Collections.unmodifiableList( projects );
    }
}
