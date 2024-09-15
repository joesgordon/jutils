#pragma once

#include "ISerialPort.hpp"

namespace CUtils
{

struct JniSerialPort
{
    CUtils::ISerialPort_ port;

    JniSerialPort();
};

} // namespace CUtils
