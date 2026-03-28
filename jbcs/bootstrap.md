# Bootstrapping jcbs

This document describes the minimum steps needed to compile and run `jcbs`
using only a JDK.

## Prerequisites

- `javac` must be available
- `java` must be available

## Compile

From the `jbcs` directory:

```text
javac -sourcepath ./src -d ./bin ./src/jbcs/JbcsMain.java
```

This compiles the `jcbs` classes into `jbcs/bin`.

## Run

To run with the default top-level config file:

```text
java -cp ./bin jbcs.JbcsMain
```

To run with verbose project-level output:

```text
java -cp ./bin jbcs.JbcsMain -v
```

To run with an explicit config path:

```text
java -cp ./bin jbcs.JbcsMain path/to/jcbs.properties
```

## Current Behavior

The current implementation:

- loads `jcbs.properties`
- applies default values for optional settings
- loads optional `jcbs_proj.properties` files from included projects
- deletes and recreates the configured `build` directory
- resolves aggregate output paths
- writes a jar manifest, including `Main-Class` when `build.main.class` is set
- prints a resolved build summary
- prints per-project details only when `-v` is specified
- collects aggregate Java source files from included projects
- compiles release classes into `build/release`
- compiles debug classes into `build/debug`
- creates fat release and debug jar files that include dependency jar contents
- creates `${build.name}-src.jar`
- generates javadocs and creates `${build.name}-javadoc.jar`

It does not yet copy non-Java resources into the compiled output jars.
