#pragma once

#include "BaudRate.h"

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
std::string toString(BaudRate size)
{
    switch (size)
    {
    case BaudRate::BAUD_9600:
        return "9600";

    case BaudRate::BAUD_19200:
        return "19200";

    case BaudRate::BAUD_38400:
        return "38.4K";

    case BaudRate::BAUD_56000:
        return "56K";

    case BaudRate::BAUD_115200:
        return "115.2K";

    case BaudRate::BAUD_128000:
        return "128K";

    case BaudRate::BAUD_230400:
        return "230.4K";
    }

    // TODO concatenate with number

    return "Undefined";
}

} // namespace CUtils
