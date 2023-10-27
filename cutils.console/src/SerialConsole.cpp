#pragma warning(disable : 4996)

#include "SerialConsole.h"
#include "IPlatform.hpp"
#include "BaudRate.h"

#include <cstdio>

using std::string;

using CUtils::ISerialPort_;

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
int getChoice(int defaultChoice)
{
    int choice = defaultChoice;
    printf("[%d] > ", defaultChoice);
    fflush(stdout);

    if (scanf("%d", &choice) != 1)
    {
        choice = defaultChoice;
    }

    return choice;
}

/*******************************************************************************
 *
 ******************************************************************************/
void promptForTimeout(ISerialPort_ port)
{
    printf("Enter timeout [cancel]\n");

    int choice = getChoice(-1);

    if (choice > -1)
    {
        port->setTimeout(choice);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void promptForBaudRate(ISerialPort_ port, SerialParams config)
{
    BaudRate choices[] = {BaudRate::BAUD_9600, BaudRate::BAUD_19200,
        BaudRate::BAUD_38400, BaudRate::BAUD_56000, BaudRate::BAUD_115200,
        BaudRate::BAUD_128000, BaudRate::BAUD_230400};
    int count = sizeof(choices) / sizeof(BaudRate);

    printf("0) Cancel %s\n", port->getDevice().c_str());
    for (int i = 0; i < count; i++)
    {
        printf("%d) %s\n", i + 1, toString(choices[i]).c_str());
    }

    int choice = getChoice(0);

    if (choice > 0 && choice <= count)
    {
        config.baudRate = (uint32_t)choices[choice - 1];

        port->setConfig(config);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void promptForWordSize(ISerialPort_ port, SerialParams config)
{
    WordSize choices[] = {WordSize::BITS_4, WordSize::BITS_5, WordSize::BITS_6,
        WordSize::BITS_7, WordSize::BITS_8};
    int count = sizeof(choices) / sizeof(BaudRate);

    printf("0) Cancel %s\n", port->getDevice().c_str());
    for (int i = 0; i < count; i++)
    {
        printf("%d) %s\n", i + 1, toString(choices[i]).c_str());
    }

    int choice = getChoice(0);

    if (choice > 0 && choice <= count)
    {
        config.size = choices[choice - 1];

        port->setConfig(config);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void promptForParity(ISerialPort_ port, SerialParams config)
{
    Parity choices[] = {
        Parity::NONE, Parity::ODD, Parity::EVEN, Parity::MARK, Parity::SPACE};
    int count = sizeof(choices) / sizeof(BaudRate);

    printf("0) Cancel %s\n", port->getDevice().c_str());
    for (int i = 0; i < count; i++)
    {
        printf("%d) %s\n", i + 1, toString(choices[i]).c_str());
    }

    int choice = getChoice(0);

    if (choice > 0 && choice <= count)
    {
        config.parity = choices[choice - 1];

        port->setConfig(config);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void printPortOptions(ISerialPort_ port)
{
    auto config = port->getConfig();
    bool isHwFlowEnabled = config.dtrControl != DtrControl::DISABLE;
    bool isSwFlowEnabled =
        config.swFlowInputEnabled == true || config.swFlowOutputEnabled == true;

    printf("0) Cancel %s\n", port->getDevice().c_str());
    printf("1) Set Timeout\n");
    printf("2) Toggle from %s mode\n",
        config.binaryModeEnabled ? "binary" : "text");
    printf("3) Change baud rate from %d\n", config.baudRate);
    printf("4) Change word size from %s\n", toString(config.size).c_str());
    printf("5) Change parity from %s\n", toString(config.parity).c_str());
    printf("6) Change stop bits from %s\n", toString(config.stopBits).c_str());
    printf("7) Toggle HW flow control from %s\n",
        isHwFlowEnabled ? "on" : "false");
    printf("8) Toggle SW flow control from %s\n",
        isSwFlowEnabled ? "on" : "false");

    int choice = getChoice(0);

    if (choice > 0)
    {
        if (choice <= 7)
        {
            switch (choice)
            {
            case 1:
                promptForTimeout(port);
                break;

            case 2:
                config.binaryModeEnabled = !config.binaryModeEnabled;
                port->setConfig(config);
                break;

            case 3:
                promptForBaudRate(port, config);
                break;

            case 4:
                promptForWordSize(port, config);
                break;

            case 5:
                promptForParity(port, config);
                break;

            case 6:
                break;

            case 7:
                break;

            case 8:
                break;
            }
        }
        else
        {
            printf("Invalid choice: %d\n", choice);
        }
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
void readPort(ISerialPort_ port)
{
    uint8_t buf[64];
    int32_t count = port->read(&buf, sizeof(buf));

    printf("Read %d bytes\n", count);
}

/*******************************************************************************
 *
 ******************************************************************************/
void writePort(ISerialPort_ port)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
void openPort(const string &portName)
{
    printf("Opening %s\n\n", portName.c_str());

    auto api = CUtils::getPlatform();
    auto port = api->createSerialPort();

    if (!port->open(portName))
    {
        printf("ERROR: Unable to open %s\n", portName.c_str());
        return;
    }

    bool keepOpen = true;

    while (keepOpen)
    {
        printf("0) Close %s\n", port->getDevice().c_str());
        printf("1) Options %s\n", port->getDevice().c_str());
        printf("2) Read %s\n", port->getDevice().c_str());
        printf("3) Write %s\n", port->getDevice().c_str());

        // Continue until the user closes the port
        int choice = getChoice(0);

        if (choice == 0)
        {
            keepOpen = false;
        }
        else if (choice > 0 && choice < 4)
        {
            switch (choice)
            {
            case 1:
                printPortOptions(port);
                break;

            case 2:
                readPort(port);
                break;

            case 3:
                writePort(port);
                break;
            }
        }
        else
        {
            printf("Invalid choice: %d\n", choice);
        }
    }

    port->close();

    printf("\n");
}

/*******************************************************************************
 *
 ******************************************************************************/
bool printPorts()
{
    auto api = CUtils::getPlatform();
    auto ports = api->listSerialPorts();
    int portCount = (int)ports.size();

    printf("Found %d ports:\n\n", (int)ports.size());

    printf("0) Exit\n");

    for (int i = 0; i < portCount; i++)
    {
        printf("%d) Open %s\n", i + 1, ports[i].c_str());
    }

    int choice = getChoice(0);

    bool keepAsking = true;

    if (choice == 0)
    {
        keepAsking = false;
    }
    else if (choice > 0 && choice <= portCount)
    {
        choice--;
        auto portName = ports[choice];

        openPort(portName);
    }
    else
    {
        printf("Invalid choice: %d\n", choice);
    }

    printf("\n");

    return keepAsking;
}

/*******************************************************************************
 *
 ******************************************************************************/
int runConsole()
{
    while (printPorts())
    {
        // Continue until the user quits
    }

    return 0;
}

} // namespace CUtils
