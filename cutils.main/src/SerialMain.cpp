#include <cstdio>
#include <string>

#include "SerialConsole.h"

#include "IPlatform.hpp"

int main2(int argc, char *argv[])
{
    return CUtils::runConsole();
}

int main(int argc, char *argv[])
{
    std::string serialName = "COM4";

    auto api = CUtils::getPlatform();
    auto serial = api->createSerialPort();

    if (!serial->open(serialName))
    {
        printf("ERROR: Unable to open %s\n", serialName.c_str());
        return 1;
    }

    uint8_t bytes[100];
    int count = (int)sizeof(bytes);

    for (int i = 0; i < count; i++)
    {
        bytes[i] = (uint8_t)i;
    }

    for (int i = 0; i < 20; i++)
    {
        int32_t written_cnt = serial->write(bytes, count);

        if (written_cnt != count)
        {
            printf("ERROR: Wrote %d instead of %d on iteration %d\n", written_cnt, count, i);
            return 2;
        }
    }

    printf("Finished\n");

    return 0;
}