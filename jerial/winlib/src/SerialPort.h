#pragma once

#include <string>
#include <memory>

#include "SerialConfig.h"

namespace Jerial
{

class PortInfo;

typedef std::shared_ptr<PortInfo> PortInfo_;

class SerialPort
{
public:
    SerialPort();

    explicit SerialPort(const SerialPort &) = delete;
    explicit SerialPort(SerialPort &&) noexcept = delete;
    SerialPort &operator=(const SerialPort &) = delete;
    SerialPort &operator=(SerialPort &&) noexcept = delete;

    bool open(const std::string &device);

    void setConfig(const SerialConfig &config);

    SerialConfig getConfig();

private:
    PortInfo_ port;
};

typedef std::shared_ptr<SerialPort> SerialPort_;

} // namespace Jerial
