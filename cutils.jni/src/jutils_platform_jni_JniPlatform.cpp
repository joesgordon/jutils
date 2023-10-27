
#include "jutils_platform_jni_JniPlatform.h"
#include "IPlatform.hpp"

using namespace CUtils;

JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniPlatform_initialize(
    JNIEnv *env, jobject jthis)
{
    auto platform = CUtils::getPlatform();

    return platform->initialize();
}

JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniPlatform_destroy(
    JNIEnv *env, jobject jthis)
{
    auto platform = CUtils::getPlatform();

    return platform->destroy();
}

JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniPlatform_listPorts(
    JNIEnv *env, jobject jthis, jobject jports)
{
    jboolean result = false;

    auto platform = CUtils::getPlatform();
    auto ports = platform->listSerialPorts();

    jclass listCls = env->GetObjectClass(jports);
    jmethodID clearMethod = env->GetMethodID(listCls, "clear", "()V");
    jmethodID addMethod =
        env->GetMethodID(listCls, "add", "(Ljava/lang/Object;)Z");

    jclass charsetClass = env->FindClass("java/nio/charset/Charset");
    jmethodID forName = env->GetStaticMethodID(charsetClass, "forName",
        "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
    jstring utf8 = env->NewStringUTF("UTF-8");
    jobject charset = env->CallStaticObjectMethod(charsetClass, forName, utf8);

    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID ctor = env->GetMethodID(
        stringClass, "<init>", "([BLjava/nio/charset/Charset;)V");

    for (auto p : ports)
    {
        int byteCount = (int)p.length();
        jbyteArray bytes = env->NewByteArray(byteCount);
        const char *cport = p.c_str();
        const signed char *csport =
            reinterpret_cast<const signed char *>(cport);
        const jbyte *pNativeMessage = static_cast<const jbyte *>(csport);

        env->SetByteArrayRegion(bytes, 0, byteCount, pNativeMessage);

        jstring jPort = reinterpret_cast<jstring>(
            env->NewObject(stringClass, ctor, bytes, charset));

        env->CallObjectMethod(jports, addMethod, jPort);
    }

    return result;
}