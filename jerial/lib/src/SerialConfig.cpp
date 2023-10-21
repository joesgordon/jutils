
#include "SerialConfig.h"

namespace Jerial
{

SerialConfig::SerialConfig()
    : binaryModeEnabled(false), baudRate(0), size(), parity(), stopBits(),
      ctsEnabled(false), dsrEnabled(false), dtrControl(),
      swFlowOutputEnabled(false), swFlowInputEnabled(false)
{
}

} // namespace Jerial
