#pragma once

#include "IPlatform.hpp"

namespace CUtils
{

class WinJerialApi : public IJerialApi
{
public:
    WinJerialApi();

    virtual ~WinJerialApi() override;

    virtual void initialize() override;

    virtual void destroy() override;

    virtual ISerialPort_ createSerialPort() override;

    virtual std::vector<std::string> listSerialPorts() override;
};

} // namespace CUtils
