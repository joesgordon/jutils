package bukl.config;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/*******************************************************************************
 * Resolved configuration for one included project directory.
 ******************************************************************************/
public final class ProjectConfig
{
    /**  */
    private final String name;
    /**  */
    private final Path projectDirectory;
    /**  */
    private final Path sourceDirectory;
    /**  */
    private final List<String> sourceExcludes;
    /**  */
    private final List<String> resourceExcludes;
    /**  */
    private final List<String> javadocExcludes;

    /***************************************************************************
     * @param name
     * @param projectDirectory
     * @param sourceDirectory
     * @param sourceExcludes
     * @param resourceExcludes
     * @param javadocExcludes
     **************************************************************************/
    public ProjectConfig( String name, Path projectDirectory,
        Path sourceDirectory, List<String> sourceExcludes,
        List<String> resourceExcludes, List<String> javadocExcludes )
    {
        this.name = name;
        this.projectDirectory = projectDirectory;
        this.sourceDirectory = sourceDirectory;
        this.sourceExcludes = List.copyOf( sourceExcludes );
        this.resourceExcludes = List.copyOf( resourceExcludes );
        this.javadocExcludes = List.copyOf( javadocExcludes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getProjectDirectory()
    {
        return projectDirectory;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Path getSourceDirectory()
    {
        return sourceDirectory;
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
    public List<String> getResourceExcludes()
    {
        return Collections.unmodifiableList( resourceExcludes );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<String> getJavadocExcludes()
    {
        return Collections.unmodifiableList( javadocExcludes );
    }
}
