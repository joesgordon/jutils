
#include "SerialConsole.h"

#include "IJerialApi.hpp"

#include <cstdio>

namespace Jerial
{

int runConsole()
{ /*
    auto api = Jerial::getApi();
    auto ports = api->listSerialPorts();

    printf("%d ports found\n", (int)ports.size());

    for (auto port : ports)
    {
        printf("Port: %s\n", port.c_str());
        auto sprt = api->createSerialPort();

        sprt->open(port);

        sprt->getConfig();
    }
    */
    return 0;
}

} // namespace Jerial
