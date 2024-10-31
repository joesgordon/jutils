# JUtils To-Do List

[Home](./README.md)

## General Issues

- Add time chooser that uses Chronos
- Add cyclic buffer to NetMsgsView.
- Add focus listeners to form fields
- Write 
  - `fieldHasFocus(field:IFormField):boolean` and 
  - `hasFocus(comp:JComponent):boolean` using `JFrame.getFocusOwner()` and 
  - `Container.isAnscestorOf()`.
- Summer: Find files using `TaskView.startAndShow()`.
- Summer: Multithread checksum verification.
- ListView:
  - add `ItemActionList<ItemChanged<T>>` change listeners.
  - make add/delete item listeners lamdas & call change listeners.
- TaskPool: Add `AtomicInteger` complete count.
- Add `Utils.contains(items:List<T>, data:D, IBinaryComparator<T,D>)`
- Write `InputStreamLineReader` or `InputLineStream`.
- Move `FileField` to validation and rename.
- Write `ComboFileFormField`.
- Write jutils.ini.
- Finish [jutils.maths](./docs/math.md)
- Finish [cutils.platform](./docs/cutils.md)
- See [jutils.plot](./docs/plot.md)

## jutils.iris

- Fix scrolling
- Add Bayer as a channel placement. Each bayer channel must be the same bit depth.
- Add a histogram for each channel.
- Show zoom level value.
- Show image stats for each channel in the whole image, selection, and hover.
  - width
  - height
  - min
  - max
  - average
  - standard deviation
- Add ability to Save (raw, png, jpg, bmp, tiff).
- Finish Bayer Bilinear demosaic
- Add ability to pan with middle-click and drag.
- Add configuration for MonoColorizer
  - Color map
  - Thresholds
  - Brightness/Contrast
- Finish remainder of Bayer demosaic
- X/Y Levels for a channel on top/side
- Add ability to Playback.
- Add preview to Raw Options on open.
- Define the Raw Image Album format and add to open/save support. Default save to this format.
- Centroid algorithm

## jutils.math

- Add units to Vector3dField
- Add EastNorthUpView
- Add EcefView
- Add EciView
- Add LatLonAltView
- Add NorthEastDownView
- Add RangeAzElView
- Add MatrixEditorView
- Add QuaternionField
- Add Vector4dField
- Add CoordinateTransformView
- Add unit tests

## jutils.multicon

- Add ping
- Add NTP
- Add TFTP, FTP, SFTP clients and servers.

## jutils.fileSpy

- Add binary file detection. Skip binary files according to the users selction to be added to search parameters. Show non-printable characters around matches as spaces.
- Capture only a few characters around a match up to the end of the line. Allow the user to edit this value in preferences. Set default to 1024 characters. Consider grabbing multiple lines up to the maximum amount.

## jutils.rob
*PROPOSED*

- Add clean target to jutils builds.
- Update versioning to support:
  - Rebuild (Same version)
  - Build (Engineering version)
  - Release (New version)

## jutils.games

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

## jutils.chio
*PROPOSED*

Chapter 10 library

## jutils.helps
*PROPOSED*

help system

## jutils.jemini
*PROPOSED*

replacement for xstream

## Error exploration

- Error with built-in resolution:
  - message - what happened
  - context - when did it happen/what doing; not time
  - content - what did it happen to
- or alternatively
  - action - what failed
  - reason - why did it fail
  - context - what did it happen to
