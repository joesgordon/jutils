package jbcs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import jbcs.build.BuildDirectoryCleaner;
import jbcs.build.JarArchiver;
import jbcs.build.JavaSourceCompiler;
import jbcs.build.JavadocGenerator;
import jbcs.build.ResourceCopier;
import jbcs.build.SourceArchiveBuilder;
import jbcs.build.SourceCollector;
import jbcs.build.SourceCollector.CollectedFile;
import jbcs.config.JbcsConfig;
import jbcs.config.JbcsConfigLoader;

/*******************************************************************************
 * Loads jcbs configuration and compiles the aggregate source set.
 ******************************************************************************/
public final class Jbcs
{
    /**  */
    private final Path configPath;
    /**  */
    private final boolean verbose;
    /**  */
    private final boolean generateDocs;

    /***************************************************************************
     * @param configPath
     * @param verbose
     * @param generateDocs
     **************************************************************************/
    public Jbcs( Path configPath, boolean verbose, boolean generateDocs )
    {
        this.configPath = configPath;
        this.verbose = verbose;
        this.generateDocs = generateDocs;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void run() throws IOException
    {
        JbcsConfig config = new JbcsConfigLoader().load( configPath );
        SourceCollector collector = new SourceCollector();
        List<Path> sourceFiles = collector.collectSources( config );
        List<
            CollectedFile> sourceArchiveFiles = collector.collectSourceArchiveFiles(
                config );
        List<CollectedFile> resources = collector.collectResources( config );
        List<Path> javadocSourceFiles = generateDocs
            ? collector.collectJavadocSources( config )
            : List.of();

        printSummary( config );
        System.out.println( "  source files  : " + sourceFiles.size() );
        System.out.println( "  resources     : " + resources.size() );
        System.out.println( "  docs enabled  : " + generateDocs );

        if( generateDocs )
        {
            System.out.println(
                "  javadoc files : " + javadocSourceFiles.size() );
        }

        prepareBuildDirectories( config );

        JavaSourceCompiler compiler = new JavaSourceCompiler();
        compiler.compileRelease( config, sourceFiles );
        compiler.compileDebug( config, sourceFiles );

        ResourceCopier resourceCopier = new ResourceCopier();
        resourceCopier.copyResources( resources, config.getClassesDirectory() );
        resourceCopier.copyResources( resources,
            config.getDebugClassesDirectory() );

        JarArchiver archiver = new JarArchiver();
        archiver.createJar( config.getClassesDirectory(),
            config.getReleaseJar(), config.getDependencies(), config );
        archiver.createJar( config.getDebugClassesDirectory(),
            config.getDebugJar(), config.getDependencies(), config );

        SourceArchiveBuilder sourceArchive = new SourceArchiveBuilder();
        sourceArchive.createSourceJar( sourceArchiveFiles,
            config.getSourcesJar() );

        if( generateDocs )
        {
            JavadocGenerator javadocs = new JavadocGenerator();
            javadocs.generate( config, javadocSourceFiles );
            archiver.createPlainJar( config.getJavadocDirectory(),
                config.getJavadocJar() );
        }
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    private void printSummary( JbcsConfig config )
    {
        System.out.println( "jcbs build summary" );
        System.out.println( "  config        : " + config.getConfigFile() );
        System.out.println( "  root dir      : " + config.getRootDirectory() );
        System.out.println( "  build name    : " + config.getBuildName() );
        System.out.println( "  java version  : " + config.getJavaVersion() );
        System.out.println( "  main class    : " + config.getMainClass() );
        System.out.println(
            "  projects      : " + config.getProjects().size() );
        System.out.println(
            "  dependencies  : " + config.getDependencies().size() );
        System.out.println(
            "  output dir    : " + config.getOutputDirectory() );
        System.out.println( "  release jar   : " + config.getReleaseJar() );
        System.out.println( "  debug jar     : " + config.getDebugJar() );
        System.out.println( "  sources jar   : " + config.getSourcesJar() );
        System.out.println( "  javadoc jar   : " + config.getJavadocJar() );

        if( verbose )
        {
            for( var project : config.getProjects() )
            {
                System.out.println( "  project       : " + project.getName() );
                System.out.println(
                    "    directory   : " + project.getProjectDirectory() );
                System.out.println(
                    "    source dir  : " + project.getSourceDirectory() );
                System.out.println(
                    "    source excl : " + project.getSourceExcludes() );
                System.out.println(
                    "    resource ex : " + project.getResourceExcludes() );
                System.out.println(
                    "    javadoc ex  : " + project.getJavadocExcludes() );
            }
        }
    }

    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    private void prepareBuildDirectories( JbcsConfig config ) throws IOException
    {
        new BuildDirectoryCleaner().recreateDirectory(
            config.getOutputDirectory() );

        Files.createDirectories( config.getOutputDirectory() );
        Files.createDirectories( config.getClassesDirectory() );
        Files.createDirectories( config.getDebugClassesDirectory() );
        Files.createDirectories( config.getJavadocDirectory() );
        Files.createDirectories( config.getReleaseJar().getParent() );
        Files.createDirectories( config.getDebugJar().getParent() );
        Files.createDirectories( config.getSourcesJar().getParent() );
        Files.createDirectories( config.getJavadocJar().getParent() );
    }
}
