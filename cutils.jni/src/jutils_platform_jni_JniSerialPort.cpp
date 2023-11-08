
#include "jutils_platform_jni_JniSerialPort.h"

#include "IPlatform.hpp"
#include "ISerialPort.hpp"
#include "JniUtils.h"
#include "JniSerialPort.h"

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
void printBytes(void *ptr, int count)
{
    uint8_t *bptr = (uint8_t *)ptr;
    for (int i = 0; i < count; i++)
    {
        if (i > 0)
        {
            printf(", ");
        }

        printf("%02X", bptr[i]);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
jboolean getBoolean(JNIEnv *env, jobject jobj, const char *name)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "Z");
    jboolean value = env->GetBooleanField(jobj, id);

    env->DeleteLocalRef(jcls);

    return value;
}

/*******************************************************************************
 *
 ******************************************************************************/
void setBoolean(JNIEnv *env, jobject jobj, const char *name, jboolean value)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "Z");

    env->SetBooleanField(jobj, id, value);

    env->DeleteLocalRef(jcls);
}

/*******************************************************************************
 *
 ******************************************************************************/
jint getInt(JNIEnv *env, jobject jobj, const char *name)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "I");
    jint value = env->GetIntField(jobj, id);

    env->DeleteLocalRef(jcls);

    return value;
}

/*******************************************************************************
 *
 ******************************************************************************/
void setInt(JNIEnv *env, jobject jobj, const char *name, jint value)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "I");

    env->SetIntField(jobj, id, value);

    env->DeleteLocalRef(jcls);
}

/*******************************************************************************
 *
 ******************************************************************************/
jlong getLong(JNIEnv *env, jobject jobj, const char *name)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "J");
    jlong value = env->GetLongField(jobj, id);

    env->DeleteLocalRef(jcls);

    return value;
}

/*******************************************************************************
 *
 ******************************************************************************/
void setLong(JNIEnv *env, jobject jobj, const char *name, jlong value)
{
    jclass jcls = env->GetObjectClass(jobj);
    jfieldID id = env->GetFieldID(jcls, name, "J");

    env->SetLongField(jobj, id, value);

    env->DeleteLocalRef(jcls);
}

/*******************************************************************************
 *
 ******************************************************************************/
JniSerialPort *getId(JNIEnv *env, jobject jSerialPort)
{
    jlong value = getLong(env, jSerialPort, PORT_ID);

    return reinterpret_cast<JniSerialPort *>(value);
}

/*******************************************************************************
 *
 ******************************************************************************/
void setId(JNIEnv *env, jobject jSerialPort, JniSerialPort *jsp)
{
    jlong value = reinterpret_cast<jlong>(jsp);

    setLong(env, jSerialPort, PORT_ID, value);
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
    JNIEnv *env, jobject jthis, jint millis)
{
    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        info->port->setTimeout(millis);
    }
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_read(
    JNIEnv *env, jobject jthis, jbyteArray jbuf, jint joffset, jint length)
{
    jint result = -1;

    jbyte *buf = (jbyte *)env->GetPrimitiveArrayCritical(jbuf, nullptr);

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        jbyte *pbuf = &buf[joffset];
        result = info->port->read(pbuf, length);

        if (result > 0)
        {
            printf(
                "DEBUG: Read %d bytes: ", result);
            printBytes(pbuf, result);
            printf("\n");
        }
    }
    // else
    // {
    //     printf("ERROR: Unable to read bytes: invalid info pointer\n");
    // }

    fflush(stdout);

    env->ReleasePrimitiveArrayCritical(jbuf, buf, 0);

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_write(
    JNIEnv *env, jobject jthis, jbyteArray jbuf, jint joffset, jint length)
{
    jint result = -1;

    jbyte *buf = (jbyte *)env->GetPrimitiveArrayCritical(jbuf, nullptr);

    JniSerialPort *info = getId(env, jthis);

    if (nullptr != info)
    {
        void *pbuf = &buf[joffset];
        result = info->port->write(pbuf, length);

        printf("DEBUG: Wrote %d bytes:", result);
        printBytes(pbuf, result);
        printf("\n");
    }
    // else
    // {
    //     printf("ERROR: Unable to write bytes: invalid info pointer\n");
    // }

    fflush(stdout);

    env->ReleasePrimitiveArrayCritical(jbuf, buf, 0);

    return result;
}
