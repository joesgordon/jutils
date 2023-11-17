
#include "Signal.h"
#include "IPlatform.hpp"

#include <cstdio>

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
Signal::Signal() : handle(INVALID_HANDLE_VALUE), signaled(false)
{
    handle = CreateEvent(NULL, // default security attributes
        FALSE,                 // bManualReset: FALSE to auto-reset event
        FALSE,                 // initial state is nonsignaled
        NULL                   // object name
    );

    if (handle == nullptr)
    {
        printf("CreateEvent error: %d\n", (int)GetLastError());
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
Signal::~Signal()
{
    handle = INVALID_HANDLE_VALUE;
}

/*******************************************************************************
 *
 ******************************************************************************/
bool Signal::wait(int timeout)
{
    bool wasSignaled = signaled;

    signaled = false;

    if (wasSignaled)
    {
        ResetEvent(handle);
    }
    else if (timeout != 0)
    {
        DWORD dwTimeout = timeout < 0 ? INFINITE : (DWORD)timeout;
        DWORD result = WaitForSingleObject(handle, dwTimeout); // auto-resets

        if (result == WAIT_OBJECT_0)
        {
            signaled = false;
            wasSignaled = true;
        }
    }

    return wasSignaled;
}

/*******************************************************************************
 *
 ******************************************************************************/
void Signal::notify()
{
    bool wasSignaled = signaled;

    if (!wasSignaled)
    {
        signaled = true;

        if (!SetEvent(handle))
        {
            auto err = GetLastError();
            auto platform = getPlatform();
            auto errStr = platform->getError(err);

            printf("ERROR: SetEvent failed (%d): %s\n", err, errStr.c_str());
        }
    }
}

} // namespace CUtils
