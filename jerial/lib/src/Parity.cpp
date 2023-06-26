#include "Parity.h"

using std::string;

namespace Jerial
{

string to_string(Parity p)
{
    switch (p)
    {
    case Parity::NONE:
        return "None";

    case Parity::ODD:
        return "Odd";

    case Parity::EVEN:
        return "Even";

    case Parity::MARK:
        return "Mark";

    case Parity::SPACE:
        return "Space";
    }

    return "Unknown";
}

} // namespace Jerial