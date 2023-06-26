#pragma once

#include <memory>
#include <string>

#include "SerialConfig.h"

namespace Jerial
{

class ISerialPort
{
public:
    virtual ~ISerialPort() = 0;

    explicit ISerialPort(const ISerialPort &) = delete;
    explicit ISerialPort(ISerialPort &&) noexcept = delete;
    ISerialPort &operator=(const ISerialPort &) = delete;
    ISerialPort &operator=(ISerialPort &&) noexcept = delete;

    virtual void initialize() = 0;

    virtual bool open(const std::string &device) = 0;

    virtual bool close() = 0;

    virtual void setConfig(const SerialConfig &config) = 0;

    virtual SerialConfig getConfig() = 0;
};

typedef std::shared_ptr<ISerialPort> ISerialPort_;

} // namespace Jerial
