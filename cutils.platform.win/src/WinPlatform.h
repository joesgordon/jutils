#pragma once

#include "IPlatform.hpp"

namespace CUtils
{

class WinPlatform : public IPlatform
{
public:
    WinPlatform();

    virtual ~WinPlatform() override;

    virtual void initialize() override;

    virtual void destroy() override;

    virtual ISerialPort_ createSerialPort() override;

    virtual std::vector<std::string> listSerialPorts() override;
};

} // namespace CUtils
