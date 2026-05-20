# bukl

`bukl` is a small Java bootstrap build tool intended for repositories that use
Eclipse-style sibling project directories.

The tool is designed for cases where multiple projects contribute source and
resources to a single aggregate build output.

## Goals

- Keep bootstrap simple: compile `bukl` with a single `javac` command, then run
  it with `java`
- Avoid requiring Gradle, Maven, Ant, Python, or a platform-specific scripting
  environment on developer machines
- Treat multiple included project `src` folders as one aggregate compile unit
- Keep configuration easy to edit with Java `.properties` files
- Keep external dependencies defined only at the top level

## Repository Model

The expected repository layout is a top-level directory that contains multiple
Eclipse-style project folders, for example:

- `myproj1`
- `myproj2`
- `myproj3`

Each included project is expected to contain a `src` directory by default.

Non-Java files under each project source tree are treated as resources and are
copied into the output using the same relative paths they have under `src`.

## Configuration Files

Top-level configuration file:

- `bukl.properties`

Optional per-project override file:

- `bukl_proj.properties`

The top-level file defines the aggregate build.

The per-project file is only for project-specific exceptions. It should be
optional and used sparingly.

## Build Model

`bukl` treats all included project source trees as one combined source set.

This means:

- all included `src` folders are compiled together
- all non-`.java` files under those source trees are copied as resources
- all external dependency jars are supplied from one top-level dependency list
- outputs are produced as aggregate artifacts instead of building per-project
  jars and merging them later

## Outputs

One invocation of `bukl` is intended to support producing:

- release jar
- debug jar
- source jar
- javadoc jar

The release and debug jars contain compiled classes and copied resources.
They are intended to be fat jars that also include configured dependency jar
contents.

The source jar contains all included source trees, preserving relative paths.

The javadoc jar contains generated documentation for the aggregate source set.

Each build run is intended to start by deleting and recreating the configured
build output directory.

## Top-Level Schema

The top-level file is `bukl.properties`.

Required keys:

- `build.name`
- `build.projects`

All other keys are optional and may use defaults.

### Required Keys

`build.name`

- Logical build name
- Used to derive default output artifact names
- Example: `mybuild`

`build.projects`

- Comma-separated list of included project directories
- These are treated as sibling directories relative to the top-level config
- Example: `myproj1,myproj2,myproj3`

### Optional Keys

`build.java.version`

- Desired Java version
- Default: current runtime JDK version, or left unspecified unless enforced by
  the tool implementation

`build.main.class`

- Fully qualified main class to write to the jar manifest
- Used for release and debug jar output
- Default: omitted

`build.source.dir`

- Source directory name used inside each included project
- Default: `src`

`build.dependencies`

- Comma-separated list of external dependency jar paths
- Paths are relative to the top-level config file unless otherwise documented
- Default: empty

`build.output.dir`

- Root output directory
- Default: `build`

`build.output.classes.dir`

- Directory for release-compiled classes
- Default: `build/release`

`build.output.debug.classes.dir`

- Directory for debug-compiled classes
- Default: `build/debug`

`build.output.doc.dir`

- Directory for generated javadocs
- Default: `build/docs/javadoc`

`build.output.release.jar`

- Aggregate release jar path
- Default: `build/single/${build.name}.jar`

`build.output.debug.jar`

- Aggregate debug jar path
- Default: `build/single/${build.name}-debug.jar`

`build.output.sources.jar`

- Aggregate source jar path
- Default: `build/single/${build.name}-src.jar`

`build.output.javadoc.jar`

- Aggregate javadoc jar path
- Default: `build/single/${build.name}-javadoc.jar`

`build.resource.exclude`

- Exclude pattern for resources
- Default: `**/*.java`

`build.source.exclude`

- Exclude pattern for source collection
- Default: empty

`build.javadoc.exclude`

- Exclude pattern for javadoc generation
- Default: empty

## Minimal Example

```properties
build.name=mybuild
build.projects=myproj1,myproj2,myproj3
```

## Expanded Example

```properties
build.name=mybuild
build.projects=myproj1,myproj2,myproj3

build.java.version=21
build.main.class=myproj1.Main
build.dependencies=libs/dep1.jar,libs/dep2.jar

build.source.dir=src
build.output.dir=build
build.output.classes.dir=build/release
build.output.debug.classes.dir=build/debug
build.output.doc.dir=build/docs/javadoc

build.output.release.jar=build/single/mybuild.jar
build.output.debug.jar=build/single/mybuild-debug.jar
build.output.sources.jar=build/single/mybuild-src.jar
build.output.javadoc.jar=build/single/mybuild-javadoc.jar

build.resource.exclude=**/*.java
build.source.exclude=
build.javadoc.exclude=
```

## Per-Project Schema

The per-project file is `bukl_proj.properties`.

This file is optional.

It exists only to describe local exceptions to the default project contract.

Recommended keys:

- `project.source.dir`
- `project.source.exclude`
- `project.resource.exclude`
- `project.javadoc.exclude`

Defaults:

- `project.source.dir=src`
- all excludes default to empty

Example:

```properties
project.source.dir=src
project.source.exclude=
project.resource.exclude=
project.javadoc.exclude=
```

## Dependency Model

External dependencies are intended to be defined only at the top level.

This keeps the aggregate compile model simple:

- one compile classpath
- one javadoc classpath
- one shared dependency list

Per-project dependency declarations are intentionally excluded from the current
design.

## Bootstrap Model

The preferred bootstrap flow is:

1. Compile the `bukl` Java sources with `javac`
2. Run `bukl` with `java`
3. `bukl` reads `bukl.properties`
4. `bukl` loads the included project source trees
5. `bukl` compiles sources, copies resources, and emits the configured jars

This keeps bootstrap limited to a JDK, without requiring a separately installed
build system.

The current implementation compiles aggregate Java sources, recreates the build
directory, and creates release/debug/source/javadoc jars. Resource copying into
the compiled output jars is still planned.

## Console Output

By default, `bukl` prints a compact build summary.

Project-by-project details are only printed when `-v` or `--verbose` is
specified on the command line.

## Design Constraints

The current design intentionally avoids:

- per-project dependency graphs
- plugin systems
- build DSLs
- machine-specific scripting assumptions
- per-project jar production followed by jar merging

The goal is a narrow tool that does exactly what this build style needs and no
more.
