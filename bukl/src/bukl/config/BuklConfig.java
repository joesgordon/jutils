package bukl.config;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/*******************************************************************************
 * Resolved top-level bukl configuration.
 ******************************************************************************/
public final class BuklConfig
{
    /**  */
    private final Path configFile;
    /**  */
    private final Path rootDirectory;
    /**  */
    private final String buildName;
    /**  */
    private final String javaVersion;
    /**  */
    private final String mainClass;
    /**  */
    private final List<Path> dependencies;
    /**  */
    private final Path outputDirectory;
    /**  */
    private final Path classesDirectory;
    /**  */
    private final Path debugClassesDirectory;
    /**  */
    private final Path javadocDirectory;
    /**  */
    private final Path releaseJar;
    /**  */
    private final Path debugJar;
    /**  */
    private final Path sourcesJar;
    /**  */
    private final Path javadocJar;
    /**  */
    private final List<String> resourceExcludes;
    /**  */
    private final List<String> sourceExcludes;
    /**  */
    private final List<String> javadocExcludes;
    /**  */
    private final List<ProjectConfig> projects;

    /***************************************************************************
     * @param configFile
     * @param rootDirectory
     * @param buildName
     * @param javaVersion
     * @param mainClass
     * @param dependencies
     * @param outputDirectory
     * @param classesDirectory
     * @param debugClassesDirectory
     * @param javadocDirectory
     * @param releaseJar
     * @param debugJar
     * @param sourcesJar
     * @param javadocJar
     * @param resourceExcludes
     * @param sourceExcludes
     * @param javadocExcludes
     * @param projects
     **************************************************************************/
    public BuklConfig( Path configFile, Path rootDirectory, String buildName,
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

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getConfigFile()
    {
        return configFile;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getRootDirectory()
    {
        return rootDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getBuildName()
    {
        return buildName;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getJavaVersion()
    {
        return javaVersion;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getMainClass()
    {
        return mainClass;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Path> getDependencies()
    {
        return Collections.unmodifiableList( dependencies );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getOutputDirectory()
    {
        return outputDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getClassesDirectory()
    {
        return classesDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getDebugClassesDirectory()
    {
        return debugClassesDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getJavadocDirectory()
    {
        return javadocDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getReleaseJar()
    {
        return releaseJar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getDebugJar()
    {
        return debugJar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getSourcesJar()
    {
        return sourcesJar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getJavadocJar()
    {
        return javadocJar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<String> getResourceExcludes()
    {
        return Collections.unmodifiableList( resourceExcludes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<String> getSourceExcludes()
    {
        return Collections.unmodifiableList( sourceExcludes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<String> getJavadocExcludes()
    {
        return Collections.unmodifiableList( javadocExcludes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<ProjectConfig> getProjects()
    {
        return Collections.unmodifiableList( projects );
    }
}
