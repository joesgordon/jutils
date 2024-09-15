
#include <iostream>

#include "SerialPort.h"

#include "CUtils.h"
#include "IPlatform.hpp"
#include "PortInfo.h"

using std::string;
using std::vector;

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
SerialPort::SerialPort() : info(new PortInfo())
{
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialPort::~SerialPort()
{
    this->info.reset();
}

/*******************************************************************************
 *
 ******************************************************************************/
bool SerialPort::open(const string &device)
{
    bool result = false;
    string path = "\\\\.\\" + device;

    auto handle = ::CreateFileA(path.c_str(), GENERIC_READ | GENERIC_WRITE, 0,
        0, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL | FILE_FLAG_OVERLAPPED, 0);

    if (handle != INVALID_HANDLE_VALUE)
    {
        info->handle = handle;
        info->getConfig();
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
        if (!info->close())
        {
            auto errNumber = GetLastError();

            string errMsg = std::system_category().message(errNumber);

            // printf("Error %lu: \"%s\" trying to close port \"%s\".\n",
            //     errNumber, errMsg.c_str(), port->name.c_str());

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
    return info->handle != INVALID_HANDLE_VALUE;
}

/*******************************************************************************
 *
 ******************************************************************************/
string SerialPort::getDevice() const
{
    return info->name;
}

/*******************************************************************************
 *
 ******************************************************************************/
void SerialPort::setTimeout(int32_t millis)
{
    DWORD timeout = (DWORD)millis;

    // printf("DEBUG: Setting timeout to %u\n", millis);
    // fflush(stdout);

    if (info->isOpen())
    {
        info->timeout = millis;

        COMMTIMEOUTS timeouts = {
            millis, // interval timeout
            0,      // read multiplier
            millis, // read constant (milliseconds)
            0,      // write multiplier
            0       // write Constant
        };
        SetCommTimeouts(info->handle, &timeouts);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void SerialPort::setConfig(const SerialParams &config)
{
    info->setBinaryMode(config.binaryModeEnabled);

    info->setBaudRate(config.baudRate);
    info->setWordSize(config.size);
    info->setParity(config.parity);
    info->setStopBits(config.stopBits);

    info->setCtsEnabled(config.ctsEnabled);
    info->setDsrEnabled(config.dsrEnabled);

    info->setDtrControl(config.dtrControl);
    info->setRtsControl(config.rtsControl);

    info->setSwFlowControl(true, config.swFlowOutputEnabled);
    info->setSwFlowControl(false, config.swFlowInputEnabled);

    info->setConfig();
}

/*******************************************************************************
 *
 ******************************************************************************/
SerialParams SerialPort::getConfig()
{
    SerialParams config;

    info->getConfig();

    config.binaryModeEnabled = info->isBinaryMode();
    config.baudRate = info->getBaudRate();
    config.size = info->getWordSize();
    config.parity = info->getParity();
    config.stopBits = info->getStopBits();
    config.ctsEnabled = info->isCtsEnabled();
    config.dsrEnabled = info->isDsrEnabled();
    config.ctsEnabled = info->isCtsEnabled();
    config.dtrControl = info->getDtrControl();
    config.swFlowOutputEnabled = info->isSwFlowControl(true);
    config.swFlowInputEnabled = info->isSwFlowControl(false);

    return config;
}

/*******************************************************************************
 *
 ******************************************************************************/
DWORD handlePending(HANDLE handle, OVERLAPPED &ovrlpd, int32_t timeout)
{
    // printf("DEBUG: handlePending(%p, %d) - Enter\n", handle, timeout);

    DWORD bytesXferred = 0;
    DWORD toMillis = timeout;
    DWORD toWait = TRUE;

    if (timeout < 0)
    {
        toMillis = INFINITE;
    }
    else if (timeout == 0)
    {
        toWait = FALSE;
    }

    DWORD dwRes = WaitForSingleObject(ovrlpd.hEvent, timeout);
    if (WAIT_OBJECT_0 == dwRes)
    {
        if (!GetOverlappedResult(handle, &ovrlpd, &bytesXferred, TRUE))
        {
            DWORD err = GetLastError();
            string errMsg = getPlatform()->getError(err);
            // if (timeout > -1)
            // {
            //     printf("DEBUG: handlePending() failed: 0x%X => %s\n", err,
            //         errMsg.c_str());
            //     // fflush(stdout);
            // }
        }
        else
        {
            // if (timeout > -1)
            // {
            //     printf("DEBUG: handlePending() - Got overlapped: %d\n",
            //         bytesXferred);
            //     // fflush(stdout);
            // }
        }
    }
    else if (WAIT_TIMEOUT == dwRes)
    {
        // if (timeout > -1)
        // {
        //     printf("DEBUG: handlePending() - Timed out\n");
        //     // fflush(stdout);
        // }
        SetEvent(ovrlpd.hEvent);
    }
    else if (WAIT_FAILED)
    {
        auto err = GetLastError();
        if (err != ERROR_IO_PENDING)
        {
            DWORD err = GetLastError();
            string errMsg = getPlatform()->getError(err);
            printf("handlePending(%p, %d) failed: 0x%X => %s\n", handle,
                timeout, err, errMsg.c_str());
            fflush(stdout);
        }
    }

    // printf("DEBUG: handlePending(%p, %d) - Exit\n", handle, timeout);

    // fflush(stdout);

    return bytesXferred;
}

/*******************************************************************************
 *
 ******************************************************************************/
int32_t SerialPort::read(void *buffer, int count)
{
    // printf("DEBUG: SerialPort::read(%p, %d) - Enter\n", buffer, count);

    DWORD bytesRead = -1;

    if (info->isOpen())
    {
        OVERLAPPED ovrlpd = {0};
        //
        // create the overlapped structure hEvent
        //
        ovrlpd.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);
        if (ovrlpd.hEvent == NULL)
        {
            return -1;
        }

        bool readResult = true;
        readResult = ReadFile(info->handle, buffer, count, &bytesRead, &ovrlpd);
        if (!readResult)
        {
            auto err = GetLastError();
            bool stillError = true;

            if (err == ERROR_IO_PENDING)
            {
                bytesRead = handlePending(info->handle, ovrlpd, -1);

                // printf("DEBUG: Read (2) %d bytes:", bytesRead);
                // if (bytesRead > 0)
                // {
                //     printBytes(buffer, bytesRead);
                // }
                // printf("\n");
                // fflush(stdout);
            }
            else
            {
                string errMsg = getPlatform()->getError(err);
                printf("ReadFile(%p, %p, %d) to %s failed: 0x%X => %s\n",
                    info->handle, buffer, count, info->name.c_str(), err,
                    errMsg.c_str());
                fflush(stdout);
            }
        }
        // else
        // {
        //     printf("DEBUG: Read (1) %d bytes:", bytesRead);
        //     printBytes(buffer, bytesRead);
        //     printf("\n");
        //     fflush(stdout);
        // }

        // printf("DEBUG: SerialPort::read() - Read %d bytes\n", bytesRead);
        // fflush(stdout);

        CloseHandle(ovrlpd.hEvent);
    }
    // else
    // {
    //     printf("DEBUG: SerialPort::read() - Port isn't open\n");
    //     fflush(stdout);
    // }

    // fflush(stdout);

    return bytesRead;
}

/*******************************************************************************
 *
 ******************************************************************************/
int32_t SerialPort::write(const void *buffer, int count)
{
    // printf("DEBUG: SerialPort::write(%p, %d) - Enter\n", buffer, count);

    DWORD bytesWritten = 0;

    // printf("DEBUG: Writing %d bytes\n", count);
    // fflush(stdout);

    if (info->isOpen())
    {
        OVERLAPPED ovrlpd = {0};

        //
        // create the overlapped structure hEvent
        //
        ovrlpd.hEvent = CreateEventA(NULL, TRUE, FALSE, NULL);
        if (ovrlpd.hEvent == NULL)
        {
            return -1;
        }

        bool writeResult =
            WriteFile(info->handle, buffer, count, &bytesWritten, &ovrlpd);

        if (!writeResult)
        {
            auto err = GetLastError();
            bool stillError = true;

            if (err == ERROR_IO_PENDING)
            {
                bytesWritten = handlePending(info->handle, ovrlpd, -1);
                stillError = err != ERROR_SUCCESS;
            }
            else
            {
                string errMsg = getPlatform()->getError(err);
                printf("WriteFile(%p, %p, %d) to %s failed: 0x%X => %s\n",
                    info->handle, buffer, count, info->name.c_str(), err,
                    errMsg.c_str());
                fflush(stdout);
            }
        }

        CloseHandle(ovrlpd.hEvent);
    }

    // printf("DEBUG: Wrote %d bytes:", bytesWritten);
    // if (bytesWritten > 0)
    // {
    //     printBytes(buffer, bytesWritten);
    // }
    // printf("\n");
    // fflush(stdout);

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
