# CUtils Platform
This code describes a platform independent library for accessing serial ports.

## Supported Serial Options

- Port - String
- Baud rate - integer from 50 to 10,000,000
- Parity:
  - None
  - Odd
  - Even
  - Mark
  - Space
- Data Bits
- Stop Bits
- CTS Output Control
- DSR Output Control
- DSR Sensitivity (Input Control)
- DTR Control:
  - Enable
  - Disable
  - Handshake
- RTS Control
  - Enable
  - Disable
  - Handshake
  - Toggle
- XON/XOFF Output Control
- XON/XOFF Input Control
- XON Limit
- XON Character
- XOFF Limit
- XOFF Character