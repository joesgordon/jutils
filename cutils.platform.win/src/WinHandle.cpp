
#include "WinHandle.h"

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
WinHandle::WinHandle() : handle(INVALID_HANDLE_VALUE)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
WinHandle::~WinHandle()
{
    if (handle != nullptr && handle != INVALID_HANDLE_VALUE)
    {
        CloseHandle(handle);
    }
}

} // namespace CUtils
