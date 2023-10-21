#pragma once

#include <cstdint>

#include "WordSize.h"
#include "Parity.h"
#include "StopBits.h"
#include "DtrControl.h"
#include "RtsControl.h"

namespace Jerial
{

/*******************************************************************************
 *
 ******************************************************************************/
class SerialConfig
{
public:
    /**  */
    bool binaryModeEnabled;
    /**  */
    uint32_t baudRate;
    /**  */
    WordSize size;
    /**  */
    Parity parity;
    /**  */
    StopBits stopBits;
    /** Clear-to-Send */
    bool ctsEnabled;
    /** Data-Set-Ready */
    bool dsrEnabled;
    /** Data-Terminal-Ready */
    DtrControl dtrControl;
    /** Request-to-Send */
    RtsControl rtsControl;
    /** Software flow control xon/xoff output */
    bool swFlowOutputEnabled;
    /** Software flow control xon/xoff input */
    bool swFlowInputEnabled;

    /***************************************************************************
     *
     **************************************************************************/
    SerialConfig();
};

} // namespace Jerial
