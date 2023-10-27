
#include <iostream>

#include "SerialPort.h"
#include "PortInfo.h"

using std::string;
using std::vector;

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
SerialPort::SerialPort() : port(new PortInfo())
{
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialPort::~SerialPort()
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

/*******************************************************************************
 *
 ******************************************************************************/
bool SerialPort::close()
{
    bool closed = true;

    if (this->isOpen())
    {
        if (CloseHandle(port->handle))
        {
            port->reset();
        }
        else
        {
            auto errNumber = GetLastError();

            string errMsg = std::system_category().message(errNumber);

            printf("Error %lu: \"%s\" trying to close port \"%s\".\n",
                errNumber, errMsg.c_str(), port->name.c_str());

            closed = false;
        }
    }

    return closed;
}

/*******************************************************************************
 *
 ******************************************************************************/
bool SerialPort::isOpen() const
{
    return port->handle != INVALID_HANDLE_VALUE;
}

/*******************************************************************************
 *
 ******************************************************************************/
string SerialPort::getDevice() const
{
    return port->name;
}

/*******************************************************************************
 *
 ******************************************************************************/
void SerialPort::setTimeout(int32_t millis)
{
    DWORD timeout = (DWORD)millis;

    if (port->isOpen())
    {
        COMMTIMEOUTS timeouts = {
            timeout, // interval timeout. 0 = not used
            0,       // read multiplier
            timeout, // read constant (milliseconds)
            0,       // Write multiplier
            0        // Write Constant
        };
        SetCommTimeouts(port->handle, &timeouts);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void SerialPort::setConfig(const SerialParams &config)
{
    port->setBinaryMode(config.binaryModeEnabled);
    port->setBaudRate(config.baudRate);
    port->setWordSize(config.size);
    port->setParity(config.parity);
    port->setStopBits(config.stopBits);
    port->setCtsEnabled(config.ctsEnabled);
    port->setDsrEnabled(config.dsrEnabled);
    port->setDtrControl(config.dtrControl);
    port->setSwFlowControl(true, config.swFlowOutputEnabled);
    port->setSwFlowControl(false, config.swFlowInputEnabled);

    port->setConfig();
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialParams SerialPort::getConfig()
{
    SerialParams config;

    port->getConfig();

    config.binaryModeEnabled = port->isBinaryMode();
    config.baudRate = port->getBaudRate();
    config.size = port->getWordSize();
    config.parity = port->getParity();
    config.stopBits = port->getStopBits();
    config.ctsEnabled = port->isCtsEnabled();
    config.dsrEnabled = port->isDsrEnabled();
    config.ctsEnabled = port->isCtsEnabled();
    config.dtrControl = port->getDtrControl();
    config.swFlowOutputEnabled = port->isSwFlowControl(true);
    config.swFlowInputEnabled = port->isSwFlowControl(false);

    return config;
}

/*******************************************************************************
 *
 ******************************************************************************/
int32_t SerialPort::read(void *buffer, int count)
{
    DWORD bytesRead = 0;

    if (port->isOpen())
    {
        if (!ReadFile(port->handle, buffer, count, &bytesRead, NULL))
        {
            auto err = GetLastError();
            string errMsg = std::system_category().message(err);

            printf("ReadFile(%p, %p, %d) to %s failed: 0x%X => %s\n",
                port->handle, buffer, count, port->name.c_str(), err,
                errMsg.c_str());
        }
    }

    return bytesRead;
}

/*******************************************************************************
 *
 ******************************************************************************/
int32_t SerialPort::write(const void *buffer, int count)
{
    DWORD bytesWritten = 0;

    if (port->isOpen())
    {
        if (!WriteFile(port->handle, buffer, count, &bytesWritten, NULL))
        {
            auto err = GetLastError();
            string errMsg = std::system_category().message(err);

            printf("WriteFile(%p, %p, %d) to %s failed: 0x%X => %s\n",
                port->handle, buffer, count, port->name.c_str(), err,
                errMsg.c_str());
        }
    }

    return bytesWritten;
}

/*******************************************************************************
 *
 ******************************************************************************/
vector<string> SerialPort::listSerialPorts()
{
    vector<string> portNames;

    ULONG numPorts = 10;
    ULONG portsFound = 0;
    ULONG *portNums = new ULONG[numPorts];

    ULONG result;

    result = GetCommPorts(portNums, numPorts, &portsFound);
    numPorts = portsFound;

    if (result == ERROR_MORE_DATA)
    {
        delete[] portNums;
        numPorts++;
        portNums = new ULONG[numPorts];
        result = GetCommPorts(portNums, numPorts, &portsFound);
        numPorts = portsFound;
    }

    if (result == ERROR_SUCCESS)
    {
        for (ULONG n = 0; n < numPorts; n++)
        {
            ULONG port = portNums[n];
            string name = "COM" + std::to_string(port);
            portNames.push_back(name);
        }
    }

    delete[] portNums;

    return portNames;
}

} // namespace CUtils
