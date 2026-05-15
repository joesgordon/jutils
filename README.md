# JUtils

Contains common utility classes for java.

[To Do](./todo.md)

## Contents

- [License](#license)
- [Build](#build)
- [Sub Projects](#sub-projects)
- [Supporting Projects](#supporting-projects)
- [Screenshots](#screenshots)

## License

JUtils is licensed under the MIT License. You are permitted to use, copy, modify, distribute, sublicense and sell copies of the software.

JUtils comes with no warranty of correctness though it totally is.

Run with:

    javaw -Djava.net.preferIPv4Stack=true -jar jutils.jar

## Build

JUtils is built with it's own [bootstrap build system](./bukl/readme.md). You must have javac in your path before running:

    cd <jutils_dir>
    javac -sourcepath ./bukl/src -d ./bukl/bin ./bukl/src/bukl/BuklMain.java
    java -cp bukl/bin bukl.BuklMain

If you would like to create a jar for easier use with other projects:

    jar --create --file ./bukl.jar --main-class bukl.BuklMain -C ./bukl/bin .

## Sub Projects

| Name | Description |
| --- | --- |
| [Apps](./docs/apps.md) | Provides a launcher that bundles all applications, tools, and displays into a single application. |
| [Core](./docs/core.md) | A library of the core JUtils classes. |
| [Demo](./docs/demo.md) | Demonstrates the look and operation of JUtils components. |
| [Duak](./docs/duak.md) | Disk Usage Analyzer Kit |
| [Explorer](./docs/explorer.md) | A file explorer. |
| [FileSpy](./docs/filespy.md) | Search for, and within, files. |
| [HexEdit](./docs/hexedit.md) | A hex view who dreams of being an editor. |
| [CUtils](./docs/cutils.md) | A set of C++ classes; some of which, has access through JNI. |
| [Insomnia](./docs/insomnia.md) | Measures when the user is awake.  |
| [Iris](./iris.md) | Displays frames of raw camera data for inspection and analysis. |
| [Kairosion](./kairosion) | Converts times to several formats. |
| [Math](./docs/math.md) | Provides a library of math, matrix operations, and coordinate transformations. |
| [Mines](./docs/mines.md) | Java version of minesweeper. |
| [Multicon](./docs/multicon.md) | Multiple network connection manager. |
| [Platform](./docs/platform.md) | Provides an library for platform specific functions including Serial UART. |
| [Plot](./docs/plot.md) | Creates scatter plots. |
| [Summer](./docs/summer.md) | Generates/validates checksums. |
| [Telemetry](./docs/telemetry.md) | Displays telemetry files and parameters. |

## Supporting Projects

| Name | Description |
| --- | --- |
| Libs | Contains the JGoodies and XStream libraries. Both are planned to be phased out and replaced with support withing JUtils. |
| Tests | Unit and graphical tests for all sub-projects. |

## Screenshots


### Apps

- [Hexedit](#hexedit)
- [Hexulator](#hexulator)
- [Multicon](#multicon)
- [Kairosion](#kairosion)
- [Serial Console](#serial-console)
- [FileSpy](#filespy)
- [JChart](#jchart)
- [Duak](#duak)
- [PCAP Viewer](#pcap-viewer)

![JUtils Apps](./docs/apps_main.png)

[Screenshots](#screenshots)

### Hexedit

![JUtils HexEdit](./docs/hexedit_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Hexulator

![Hexulator](./docs/hexulator.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Multicon

![Multicon](./docs/multicon.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Kairosion

![Kairosion](./docs/kairosion_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Serial Console

![Serial Console](./docs/serial_console_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Filespy

![Filespy](./docs/filespy_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### JChart

![JChart](./docs/jchart_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Summer

![Summer](./docs/summer_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### Duak

![Duak](./docs/duak_main.png)

[Screenshots](#screenshots) | [Apps](#apps)

### PCAP Viewer

![Duak](./docs/pcap_viewer.png)

[Screenshots](#screenshots) | [Apps](#apps)
