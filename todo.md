# JUtils To-Do List

## General Issues

- Error with built-in resolution:
  - message - what happened
  - context - when did it happen/what doing; not time
  - content - what did it happen to
- or alternatively
  - action - what failed
  - reason - why did it fail
  - context - what did it happen to
- Rename license dialog
- Add clean target to jutils builds.
- Add license to JUtils.
- Update versioning to support:
  - Rebuild (Same version)
  - Build (Engineering version)
  - Release (New version)
- Add time chooser that uses Chronos
- Add cyclic buffer to NetMsgsView.
- jutils.ini
- Add focus listeners to form fields
- Write `fieldHasFocus(field:IFormField):boolean` and `hasFocus(comp:JComponent):boolean` using JFrame.getFocusOwner() and `Container.isAnscestorOf()`.
- Summer: Find files using `TaskView.startAndShow()`.
- Summer: Multithread checksum verification.
- ListView:
  - add `ItemActionList<ItemChanged<T>>` change listeners.
  - make add/delete item listeners lamdas & call change listeners.
- Add `IStringWriter` to `ParserFormField`.
- TaskPool: Add `AtomicInteger` complete count.
- Add `Utils.contains(items:List<T>, data:D, IBinaryComparator<T,D>)`
- Write `InputStreamLineReader` or `InputLineStream`.
- Move `FileField` to validation and rename.
- Write `ComboFileFormField`.
- Games:
  - sudoku
  - snake
  - tetris
  - sokoban
  - lunar lander
  - fruit ninja
  - hearts
  - solataire
  - pinball
  - magic 8 ball

## jutils.maths

## jutils.serial

## jutils.plot

- jutils.chart.StripChart
- Update jutils.chart widgets to support log space in both the x & y axes.
- JPlot - add copy button
- Move point removal to chart or appOptions.fillbetween

## jutils.jemini - replace xstream

## jutils.helps - help system

## jutils.img

- Color maps
- Image data definitions
- Image viewers
- Histogram
- X/Y Levels
- Centroid algorithm
- Playback
- Save as (raw,png,jpg,bmp)

# Multicon

- Add ping
- Add NTP
- Add TFTP, FTP, SFTP clients and servers.

# FileSpy

- Add binary file detection. Skip binary files according to the users selction to be added to search parameters. Show non-printable characters around matches as spaces.
- Capture only a few characters around a match up to the end of the line. Allow the user to edit this value in preferences. Set default to 1024 characters. Consider grabbing multiple lines up to the maximum amount.