# Jerial

Jerial is a set of that defines these C++ projects:

- [Jerial JNI](jni/readme.md) (jni) - a JNI implementation to access the library.
- [Jerial Lib](lib/readme.md) (lib) - a library that provides access to serial ports in a platform independent manner.
- [Jerial Console](console/readme.md) (console) - a main-entry point that runs a serial console application.
- [StdMain](stdmain/readme.md) (stdmain) - a main project using the standard C main function definition.
- [WinLib](winlib/readme.md) (winlib) - an implementation of the library
- [LinuxLib](linuxlib/readme.md) (linuxlib) - an implementation of the library

Support Files

- genh.cmd - generates the JNI API on windows
- genh.sh - generates the JNI API on linux
- readme.md - this file.

## Dependencies

| Product | Dependencies |
| --- | --- |
|  Jerial Lib | none |
| Jerial JNI | Jerial Lib |
| Jerial Main | Jerial Lib |
| WinLib | Jerial Lib |
| LinuxLib | Jerial Lib |
| Winmain | Jerial Lib, WinLib |
| Linuxmain | Jerial Lib, LinuxLib |

## Compiling on Windows

1. Run genh.cmd
1. Open windows/jerial.sln
    1. Select x64/x86 and Release
    1. Clean
    1. Compile

## Compiling on Linux

TBD