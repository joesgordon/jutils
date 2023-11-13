
#include "jutils_platform_jni_JniSerialPort.h"

#include "IPlatform.hpp"
#include "ISerialPort.hpp"
#include "JniUtils.h"
#include "JniSerialPort.h"
#include "CUtils.h"

#include <map>

using std::map;
using std::string;

using namespace CUtils;

const char *PORT_ID = "portId";
const char *BIN_EN = "binaryModeEnabled";
const char *BAUD_RATE = "baudRate";
const char *SIZE_BITS = "size";
const char *PARITY = "parity";
const char *STOP_BITS = "stopBits";
const char *CTS_EN = "ctsEnabled";
const char *DSR_EN = "dsrEnabled";
const char *DTR_CTRL = "dtrControl";
const char *RTS_CTRL = "rtsControl";
const char *SW_OUT = "swFlowOutputEnabled";
const char *SW_IN = "swFlowInputEnabled";

/*******************************************************************************
 *
 ******************************************************************************/
JniSerialPort *getId(JNIEnv *env, jobject jSerialPort)
{
    return getTPointer<JniSerialPort>(env, jSerialPort, PORT_ID);
}

/*******************************************************************************
 *
 ******************************************************************************/
void setId(JNIEnv *env, jobject jSerialPort, JniSerialPort *jsp)
{
    setTPointer<JniSerialPort>(env, jSerialPort, PORT_ID, jsp);
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_open(
    JNIEnv *env, jobject jthis, jstring jname)
{
    jboolean result = false;
    IPlatform_ platform = CUtils::getPlatform();
    ISerialPort_ port = platform->createSerialPort();
    string device = jstring_to_string(env, jname);

    // printf("INFO: Trying to open %s\n", device.c_str());
    // fflush(stdout);

    if (port->open(device))
    {
        JniSerialPort *info = new JniSerialPort();

        info->port = port;

        setId(env, jthis, info);

        // printf("INFO: Opened port %s\n", device.c_str());
        // fflush(stdout);

        result = true;
    }
    // else
    // {
    //     printf("ERROR: Unable to open port %s\n", device.c_str());
    //     fflush(stdout);
    // }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_close(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        info->port->close();
        info->port.reset();

        delete info;

        setId(env, jthis, nullptr);
    }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_isOpen(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        result = info->port->isOpen();
    }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_getConfig(
    JNIEnv *env, jobject jthis, jlong jid, jobject jconfig)
{
    jboolean result = false;

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        SerialParams config = info->port->getConfig();

        setBoolean(env, jconfig, BIN_EN, config.binaryModeEnabled);
        setInt(env, jconfig, BAUD_RATE, config.baudRate);
        setInt(env, jconfig, SIZE_BITS, (int)config.size);
        setInt(env, jconfig, PARITY, (int)config.parity);
        setInt(env, jconfig, STOP_BITS, (int)config.stopBits);
        setBoolean(env, jconfig, CTS_EN, config.ctsEnabled);
        setBoolean(env, jconfig, DSR_EN, config.dsrEnabled);
        setInt(env, jconfig, DTR_CTRL, (int)config.dtrControl);
        setInt(env, jconfig, RTS_CTRL, (int)config.rtsControl);
        setBoolean(env, jconfig, SW_OUT, config.swFlowOutputEnabled);
        setBoolean(env, jconfig, SW_IN, config.swFlowInputEnabled);
    }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_setConfig(
    JNIEnv *env, jobject jthis, jobject jconfig)
{
    jboolean result = false;

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        SerialParams config;

        config.binaryModeEnabled = getBoolean(env, jconfig, BIN_EN);
        config.baudRate = getInt(env, jconfig, BAUD_RATE);
        config.size = (WordSize)getInt(env, jconfig, SIZE_BITS);
        config.parity = (Parity)getInt(env, jconfig, PARITY);
        config.stopBits = (StopBits)getInt(env, jconfig, STOP_BITS);
        config.ctsEnabled = getBoolean(env, jconfig, CTS_EN);
        config.dsrEnabled = getBoolean(env, jconfig, DSR_EN);
        config.dtrControl = (DtrControl)getInt(env, jconfig, DTR_CTRL);
        config.rtsControl = (RtsControl)getInt(env, jconfig, RTS_CTRL);
        config.swFlowOutputEnabled = getBoolean(env, jconfig, SW_OUT);
        config.swFlowInputEnabled = getBoolean(env, jconfig, SW_IN);

        info->port->setConfig(config);
    }

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT void JNICALL Java_jutils_platform_jni_JniSerialPort_setReadTimeout(
    JNIEnv *env, jobject jthis, jint jmillis)
{
    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        info->port->setTimeout(jmillis);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_read(
    JNIEnv *env, jobject jthis, jbyteArray jbuf, jint joffset, jint jlength)
{
    jint result = -1;

    if (nullptr == jbuf || joffset < 0 || jlength < 0)
    {
        return result;
    }

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info && joffset > -1 && jlength > -1)
    {
        int remaining = env->GetArrayLength(jbuf) - joffset;
        int length = remaining < jlength ? remaining : jlength;
        uint8_t *buffer = new uint8_t[length];
        result = info->port->read(buffer, length);

        if (result > 0)
        {
            jbyte *buf = (jbyte *)env->GetPrimitiveArrayCritical(jbuf, nullptr);
            jbyte *pbuf = &buf[joffset];
            memcpy(pbuf, buffer, result);
            env->ReleasePrimitiveArrayCritical(jbuf, buf, 0);

            // printf("DEBUG: Read %d bytes: ", result);
            // printBytes(buffer, result);
            // printf("\n");
            // fflush(stdout);
        }

        delete[] buffer;
    }
    // else
    // {
    //     printf("ERROR: Unable to read bytes: invalid info pointer\n");
    // }

    // fflush(stdout);

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_write(
    JNIEnv *env, jobject jthis, jbyteArray jbuf, jint joffset, jint jlength)
{
    jint result = -1;

    if (nullptr == jbuf || joffset < 0 || jlength < 0)
    {
        return result;
    }

    int bufLen = env->GetArrayLength(jbuf);
    int remaining = bufLen - joffset;
    int length = remaining < jlength ? remaining : jlength;
    uint8_t *buffer = new uint8_t[length];
    jbyte *buf = (jbyte *)env->GetPrimitiveArrayCritical(jbuf, nullptr);
    jbyte *pbuf = &buf[joffset];
    memcpy(buffer, pbuf, length);
    env->ReleasePrimitiveArrayCritical(jbuf, buf, 0);

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        result = info->port->write(buffer, length);

        // if (result > 0)
        // {
        //     printf("DEBUG: Wrote %d bytes:", result);
        //     printBytes(buffer, result);
        //     printf("\n");
        //     fflush(stdout);
        // }
    }
    // else
    // {
    //     printf("ERROR: Unable to write bytes: invalid info pointer\n");
    // }

    // fflush(stdout);

    delete[] buffer;

    return result;
}
