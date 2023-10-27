
#include "SerialParams.h"

namespace CUtils
{

SerialParams::SerialParams()
    : binaryModeEnabled(false), baudRate(0), size(), parity(), stopBits(),
      ctsEnabled(false), dsrEnabled(false), dtrControl(),
      swFlowOutputEnabled(false), swFlowInputEnabled(false)
{
}

} // namespace CUtils
