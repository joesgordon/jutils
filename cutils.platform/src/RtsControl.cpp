#pragma once

#include "RtsControl.h"

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
std::string toString(RtsControl size)
{
    switch (size)
    {
    case RtsControl::DISABLE:
        return "Disable";

    case RtsControl::ENABLE:
        return "Enable";

    case RtsControl::HANDSHAKE:
        return "Handshake";

    case RtsControl::TOGGLE:
        return "Toggle";
    }

    // TODO concatenate with number

    return "Undefined";
}

} // namespace CUtils
