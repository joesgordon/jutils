#pragma once

namespace CUtils
{

class ISignal
{
public:
    /***************************************************************************
     *
     **************************************************************************/
    ISignal();

    /***************************************************************************
     *
     **************************************************************************/
    explicit ISignal(const ISignal &) = delete;
    explicit ISignal(ISignal &&) noexcept = delete;
    ISignal &operator=(const ISignal &) = delete;
    ISignal &operator=(ISignal &&) noexcept = delete;

    /***************************************************************************
     *
     **************************************************************************/
    virtual ~ISignal() = 0;

    /***************************************************************************
     * Waits for a notification. Waits forever if < 0, polls for 0, and times
     * out according to the specified milliseconds if > 0.
     * Returns true if this signal has been notified during the timeout period
     * or prior to this function being called. Notification will clear by the
     * calling of this function.
     **************************************************************************/
    virtual bool wait(int timeout = -1) = 0;

    /***************************************************************************
     * Signals the timer to notify any thread waiting.
     **************************************************************************/
    virtual void notify() = 0;
};

} // namespace CUtils
