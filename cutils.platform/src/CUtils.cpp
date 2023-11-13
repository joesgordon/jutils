
#include "CUtils.h"

#include <cstdint>
#include <cstdio>

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
void printBytes(const void *ptr, int count, int limit)
{
    const uint8_t *bptr = (const uint8_t *)ptr;

    int max = count < limit ? count : limit;

    for (int i = 0; i < max; i++)
    {
        if (i > 0)
        {
            printf(", ");
        }

        printf("%02X", bptr[i]);
    }
}

} // namespace CUtils
