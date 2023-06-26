
#include "SerialPort.h"

#include <windows.h>

#pragma comment(lib, "OneCore.lib")

using std::string;

namespace Jerial
{

/*******************************************************************************
 *
 ******************************************************************************/
class PortInfo
{
public:
    HANDLE handle;
    DCB params;
    string name;

    PortInfo();

    explicit PortInfo(const PortInfo &) = delete;
    explicit PortInfo(PortInfo &&) noexcept = delete;
    PortInfo &operator=(const PortInfo &) = delete;
    PortInfo &operator=(PortInfo &&) noexcept = delete;

    bool getConfig();
};

/*******************************************************************************
 *
 ******************************************************************************/
PortInfo::PortInfo() : handle(INVALID_HANDLE_VALUE), params(), name()
{
    params.DCBlength = sizeof(DCB);
}

/*******************************************************************************
 *
 ******************************************************************************/
bool PortInfo::getConfig()
{
    bool result = false;

    if (handle != INVALID_HANDLE_VALUE)
    {
        COMMCONFIG cc;
        DWORD ccsize = sizeof(COMMCONFIG);

        cc.dwSize = ccsize;

        BOOL getr = GetCommConfig(handle, &cc, &ccsize);

        this->params = cc.dcb;

        result = getr == TRUE;
    }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialPort::SerialPort() : port(new PortInfo())
{
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialConfig SerialPort::getConfig()
{
    SerialConfig config;

    port->getConfig();

    config.baudRate = port->params.BaudRate;

    return config;
}

/*******************************************************************************
 *
 ******************************************************************************/
void setConfig(const SerialConfig &config)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
bool SerialPort::open(const string &device)
{
    bool result = false;
    string path = "\\\\.\\" + device;

    auto handle = ::CreateFileA(path.c_str(), GENERIC_READ | GENERIC_WRITE, 0,
        0, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);

    if (handle != INVALID_HANDLE_VALUE)
    {
        port->handle = handle;
        port->getConfig();
        result = true;
    }

    return result;
}

} // namespace Jerial
