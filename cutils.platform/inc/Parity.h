#pragma once

#include <cinttypes>
#include <string>

namespace CUtils
{

enum class Parity : uint32_t
{
    NONE = 0,
    ODD = 1,
    EVEN = 2,
    MARK = 3,
    SPACE = 4
};

std::string toString(Parity p);

} // namespace CUtils