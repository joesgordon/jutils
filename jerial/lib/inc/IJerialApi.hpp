#pragma once

#include "ISerialPort.hpp"

#include <vector>
#include <string>
#include <memory>

namespace Jerial
{

class IJerialApi
{
public:
    virtual ~IJerialApi() = 0;

    virtual void initialize() = 0;

    virtual ISerialPort_ createSerialPort() = 0;

    virtual std::vector<std::string> listSerialPorts() = 0;
};

typedef std::shared_ptr<IJerialApi> IJerialApi_;

IJerialApi_ getApi();

} // namespace Jerial