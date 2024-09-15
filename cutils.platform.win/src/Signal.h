#pragma once

#define _WINSOCKAPI_ // stops windows.h including winsock.h
#include <Windows.h>

#include "ISignal.hpp"

#include <atomic>

namespace CUtils
{

class Signal : public ISignal
{
public:
    /***************************************************************************
     *
     **************************************************************************/
    Signal();

    /***************************************************************************
     *
     **************************************************************************/
    explicit Signal(const Signal &) = delete;
    explicit Signal(Signal &&) noexcept = delete;
    Signal &operator=(const Signal &) = delete;
    Signal &operator=(Signal &&) noexcept = delete;

    /***************************************************************************
     *
     **************************************************************************/
    virtual ~Signal() override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual bool wait(int timeout = -1) override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual void notify() override;

private:
    /**  */
    HANDLE handle;
    /**  */
    std::atomic<bool> signaled;
};

} // namespace CUtils
