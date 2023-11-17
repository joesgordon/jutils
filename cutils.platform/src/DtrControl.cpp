#pragma once

#include "DtrControl.h"

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
std::string toString(DtrControl size)
{
    switch (size)
    {
    case DtrControl::DISABLE:
        return "Disable";

    case DtrControl::ENABLE:
        return "Enable";

    case DtrControl::HANDSHAKE:
        return "Handshake";
    }

    // TODO concatenate with number

    return "Undefined";
}

} // namespace CUtils
