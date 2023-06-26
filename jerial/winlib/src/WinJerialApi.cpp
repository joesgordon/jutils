
#include "WinJerialApi.h"

#include <windows.h>

#pragma comment(lib, "OneCore.lib")

using std::vector;
using std::string;

namespace Jerial
{

WinJerialApi::WinJerialApi()
{
}

WinJerialApi::~WinJerialApi()
{
}

void WinJerialApi::initialize()
{
}

ISerialPort_ WinJerialApi::createSerialPort()
{
    ISerialPort_ port(nullptr);

    return port;
}

vector<string> WinJerialApi::listSerialPorts()
{
    vector<string> portNames;

    ULONG numPorts = 10;
    ULONG *portNums = new ULONG[numPorts];
    ULONG portsFound = 1;

    ULONG result;

    result = GetCommPorts(portNums, numPorts, &portsFound);

    if (result == ERROR_MORE_DATA)
    {
        delete[] portNums;
        portNums = new ULONG[portsFound];
        numPorts = portsFound;
        result = GetCommPorts(portNums, numPorts, &portsFound);
    }

    if (result == ERROR_SUCCESS)
    {
        for (int n = 0; n < (int)portsFound; n++)
        {
            ULONG port = portNums[n];
            string name = "COM" + std::to_string(port);
            portNames.push_back(name);
        }
    }

    delete[] portNums;

    return portNames;
}

} // namespace JUtils
