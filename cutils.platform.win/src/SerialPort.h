#pragma once

#include <string>
#include <memory>
#include <vector>

#include "ISerialPort.hpp"
#include "SerialConfig.h"

namespace CUtils
{

class PortInfo;

typedef std::shared_ptr<PortInfo> PortInfo_;

class SerialPort : public CUtils::ISerialPort
{
public:
    SerialPort();

    virtual ~SerialPort() override;

    /***************************************************************************
     *
     **************************************************************************/
    explicit SerialPort(const SerialPort &) = delete;
    explicit SerialPort(SerialPort &&) noexcept = delete;
    SerialPort &operator=(const SerialPort &) = delete;
    SerialPort &operator=(SerialPort &&) noexcept = delete;

    /***************************************************************************
     *
     **************************************************************************/
    virtual bool open(const std::string &device) override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual bool close() override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual bool isOpen() const override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual std::string getDevice() const override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual void setTimeout(int32_t millis) override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual void setConfig(const SerialConfig &config) override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual SerialConfig getConfig() override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual int32_t read(void *buffer, int count) override;

    /***************************************************************************
     *
     **************************************************************************/
    virtual int32_t write(const void *buffer, int count) override;

    /***************************************************************************
     *
     **************************************************************************/
    static std::vector<std::string> listSerialPorts();
private:
    PortInfo_ port;
};

typedef std::shared_ptr<SerialPort> SerialPort_;

} // namespace CUtils
