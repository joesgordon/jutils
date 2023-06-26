#pragma once

#include <cinttypes>
#include <string>

namespace Jerial
{

enum class Parity : uint32_t
{
    NONE = 0,
    ODD = 1,
    EVEN = 2,
    MARK = 3,
    SPACE = 4
};

std::string to_string(Parity p);

} // namespace Jerial