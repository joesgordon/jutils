
#include "jutils_serial_JniSerialApi.h"

#include "IJerialApi.hpp"
#include "ISerialPort.hpp"

#include <map>

static std::map<jobject, Jerial::ISerialPort_> ports;

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_listPorts(
    JNIEnv *env, jclass clz, jobject obj)
{
    jboolean result = false;

    auto api = Jerial::getApi();

    api->initialize();
    api->destroy();

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_open(
    JNIEnv *env, jclass clz, jstring jname)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT void JNICALL Java_jutils_serial_JniSerialApi_register(
    JNIEnv *env, jobject jthis, jobject jreadBuf, jobject jwriteBuf)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_close(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_isOpen(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_getConfig(
    JNIEnv *env, jobject jthis, jobject jconfig)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_setConfig(
    JNIEnv *env, jobject jthis, jobject jconfig)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT void JNICALL Java_jutils_serial_JniSerialApi_setReadTimeout(
    JNIEnv *env, jobject jthis, jint millis)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_serial_JniSerialApi_read(
    JNIEnv *env, jobject jthis, jint length)
{
    jint result = -1;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_serial_JniSerialApi_write(
    JNIEnv *env, jobject jthis, jint length)
{
    jint result = -1;

    return result;
}
