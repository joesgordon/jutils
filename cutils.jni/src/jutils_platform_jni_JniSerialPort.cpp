
#include "jutils_platform_jni_JniSerialPort.h"

#include "IPlatform.hpp"
#include "ISerialPort.hpp"

#include <map>

static std::map<jobject, CUtils::ISerialPort_> ports;

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_open(
    JNIEnv *env, jclass clz, jstring jname)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT void JNICALL Java_jutils_platform_jni_JniSerialPort_register(
    JNIEnv *env, jobject jthis, jobject jreadBuf, jobject jwriteBuf)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_close(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_isOpen(
    JNIEnv *env, jobject jthis)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_getConfig(
    JNIEnv *env, jobject jthis, jobject jconfig)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniSerialPort_setConfig(
    JNIEnv *env, jobject jthis, jobject jconfig)
{
    jboolean result = false;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT void JNICALL Java_jutils_platform_jni_JniSerialPort_setReadTimeout(
    JNIEnv *env, jobject jthis, jint millis)
{
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_read(
    JNIEnv *env, jobject jthis, jint length)
{
    jint result = -1;

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_jutils_platform_jni_JniSerialPort_write(
    JNIEnv *env, jobject jthis, jint length)
{
    jint result = -1;

    return result;
}
