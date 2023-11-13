
#include "WinPlatform.h"
#include "SerialPort.h"

#define _WINSOCKAPI_ // stops windows.h including winsock.h
#include <Windows.h>

using std::string;
using std::vector;

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
WinPlatform::WinPlatform() : initialized(false), destroyed(false)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
WinPlatform::~WinPlatform()
{
}

/*******************************************************************************
 *
 ******************************************************************************/
bool WinPlatform::initialize()
{
    if (!initialized && !destroyed)
    {
        initialized = true;
    }

    return initialized;
}

/*******************************************************************************
 *
 ******************************************************************************/
bool WinPlatform::destroy()
{
    printf("DEBUG: Destroy Entered - %s, %s\n", initialized ? "true" : "false",
        destroyed ? "true" : "false");

    if (initialized)
    {
        printf("DEBUG: Destroying - %s, %s\n", initialized ? "true" : "false",
            destroyed ? "true" : "false");
        this->initialized = false;
        this->destroyed = true;
    }

    printf("DEBUG: Destroy Exiting - %s, %s\n", initialized ? "true" : "false",
        destroyed ? "true" : "false");

    fflush(stdout);

    return destroyed;
}

/*******************************************************************************
 *
 ******************************************************************************/
ISerialPort_ WinPlatform::createSerialPort()
{
    ISerialPort_ port(new SerialPort());

    return port;
}

/*******************************************************************************
 *
 ******************************************************************************/
vector<string> WinPlatform::listSerialPorts()
{
    return SerialPort::listSerialPorts();
}

/*******************************************************************************
 *
 ******************************************************************************/
string WinPlatform::getError(const int32_t &errorCode)
{
    char msgbuf[256]; // for a message up to 255 bytes.
    msgbuf[0] = '\0'; // Microsoft doesn't guarantee this on man page.

    FormatMessageA(
        FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS, // flags
        nullptr,                                                    // lpsource
        static_cast<DWORD>(errorCode),             // message id
        MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), // languageid
        msgbuf,                                    // output buffer
        sizeof(msgbuf),                            // size of msgbuf, bytes
        nullptr);

    return msgbuf;
}

static IPlatform_ PLATFORM(nullptr);

/*******************************************************************************
 *
 ******************************************************************************/
IPlatform_ getPlatform()
{
    if (!PLATFORM)
    {
        PLATFORM.reset(new WinPlatform());
    }

    return PLATFORM;
}

} // namespace CUtils
