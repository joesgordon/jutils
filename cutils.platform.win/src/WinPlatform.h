#pragma once

#include "IPlatform.hpp"

namespace CUtils
{

class WinPlatform : public IPlatform
{
public:
    WinPlatform();

    virtual ~WinPlatform() override;

    virtual bool initialize() override;

    virtual bool destroy() override;

    virtual ISerialPort_ createSerialPort() override;

    virtual std::vector<std::string> listSerialPorts() override;
};

} // namespace CUtils
