#include "StopBits.h"

using std::string;

namespace Jerial
{

/*******************************************************************************
 *
 ******************************************************************************/
string toString(StopBits p)
{
    switch (p)
    {
    case StopBits::ONE:
        return "1 Stop Bit";

    case StopBits::ONE5:
        return "1.5 Stop Bits";

    case StopBits::TWO:
        return "2 Stop Bits";
    }

    return "Unknown";
}

} // namespace Jerial