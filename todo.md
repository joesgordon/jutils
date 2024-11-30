# JUtils To-Do List

[Home](./README.md)

- [jutils.core](#jutilscore)
- [jutils.iris](#jutilsiris)
- [jutils.math](#jutilsmath)
- [jutils.multicon](#jutilsmulticon)
- [jutils.filespy](#jutilsfilespy)
- [jutils.telemetry](#jutilstelemetry)

## jutils.core

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
- Write jutils.core.ini.
- NicsGui: Add splitpane.
- Hexulator: Add unsiged option.
- Serial Console: Add a message delimiter option.
- Add `CharsetName` enum.
- Icons
  - Add clock icon
  - Add color icon
  - Add stopwatch icon
  - Replace external icons
- Add generic embedded resource that can be extracted.
- Add Matlab mat file reader/writer.
- Add hdf5 reader/writer.

[Top](#jutils-to-do-list)

## jutils.hexedit

- Add ability to edit and save a file.
- Fix select->copy.

[Top](#jutils-to-do-list)

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
- Centroid algorithm.
- Add Gig-E support:
  - Listen for messages.
  - Import/Export msgs files.
  - Import/Export netmsgs files
  - Import/Export wireshark pcap (pcapng) files
- Develop set of test images for each type that can be imported.

[Top](#jutils-to-do-list)

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

[Top](#jutils-to-do-list)

## jutils.multicon

- Add ping
- Add NTP
- Add TFTP, FTP, SFTP clients and servers.
- Add the ability to save pcapng files.

[Top](#jutils-to-do-list)

## jutils.fileSpy

- Add binary file detection. Skip binary files according to the users selction to be added to search parameters. Show non-printable characters around matches as spaces.
- Capture only a few characters around a match up to the end of the line. Allow the user to edit this value in preferences. Set default to 1024 characters. Consider grabbing multiple lines up to the maximum amount.
- Add the ability to save results to markdown or html

[Top](#jutils-to-do-list)

## jutils.platform

- Add Linux platform or fail gracefully if not there. Add `isSupported()` function.

## jutils.telemetry

Chapter 10 library

- Make packet trailer serializer a Keyed serializer
- Make the channels table a JTable
- Implement `CompGen1BodySerializer`
- Should time come from the nearest time packet or the previous?
- Create TMATS Editor. Use to view fullly populated TMATS file to confirm reading/writing.
- Create fully populated TMATS file and read back in.
Handle corrupt chapter 10 files. Resynchronize on corruption. Allow user to view corrupt data.
- Add screen builder.
- Add packed binary import to chapter 10.
- Add CCSDS support.
- Add ability to publish chapter 10/ccsds to packets.
- Define parameter table.
- Add ability to import parameters to packed binary.
- Add ability to read from streaming (10.3.9 as well as 218-20 TMoIP)
- Add ability to export to matlab files.

[Top](#jutils-to-do-list)
