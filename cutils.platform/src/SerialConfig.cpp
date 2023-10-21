
#include "SerialConfig.h"

namespace CUtils
{

SerialConfig::SerialConfig()
    : binaryModeEnabled(false), baudRate(0), size(), parity(), stopBits(),
      ctsEnabled(false), dsrEnabled(false), dtrControl(),
      swFlowOutputEnabled(false), swFlowInputEnabled(false)
{
}

} // namespace CUtils
