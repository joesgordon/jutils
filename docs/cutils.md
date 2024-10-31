# CUtils

CUtils is a set of C++ projects:

- [cutils.platform](../cutils.platform/cutils_platform.md) - a library that provides platform independent access to funtionality such as threads, sockets, serial ports, etc.
- [cutils.platform.win](../cutils.platform.win/cutils_platform_win.md) - the Windows implementation of the cutils.platform.
- [cutils.platform.linux] - the Linux implementation of the cutils.platform.
- [cutils.jni](../cutils.jni/cutils_jni.md) - a JNI implementation to access cutils.platform.
- [cutils.console](../cutils.console/) - a main-entry point that runs a serial console application.
- [cutils.main](stdmain/readme.md) - a main project using the standard C main function definition.

Support Files

- genh.cmd - generates the JNI API on windows
- genh.sh - generates the JNI API on linux
- readme.md - this file.

## Dependencies

| Product | Dependencies |
| --- | --- |
| cutils.platform | none |
| cutils.platform.win | cutils.platform |
| cutils.platform.linux | cutils.platform |
| cutils.jni | cutils.platform, cutils.platform.[impl] |
| cutils.console | cutils.platform, cutils.platform.[impl] |
| cutils.main | cutils.platform, cutils.platform.[impl] |

## Compiling on Windows

1. Run genh.cmd
1. Open windows/cutils.sln
    1. Select x64/x86 and Release
    1. Clean
    1. Compile

## Compiling on Linux

TBD