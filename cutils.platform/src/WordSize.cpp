#pragma once

#include "WordSize.h"

namespace Jerial
{

/*******************************************************************************
 *
 ******************************************************************************/
std::string toString(WordSize size)
{
    switch (size)
    {
    case WordSize::BITS_4:
        return "4-bit";

    case WordSize::BITS_5:
        return "5-bit";

    case WordSize::BITS_6:
        return "6-bit";

    case WordSize::BITS_7:
        return "7-bit";

    case WordSize::BITS_8:
        return "8-bit";
    }

    // TODO concatenate with number

    return "Undefined";
}

} // namespace Jerial
