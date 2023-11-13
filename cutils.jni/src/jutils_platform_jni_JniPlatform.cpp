
#include "jutils_platform_jni_JniPlatform.h"
#include "IPlatform.hpp"

#include "JniUtils.h"

using std::string;

using CUtils::string_to_jstring;

using CUtils::IPlatform_;
using CUtils::ISerialPort_;

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniPlatform_initialize(
    JNIEnv *env, jobject jthis)
{
    auto platform = CUtils::getPlatform();

    // printf("INFO: Initializing platform\n");
    // fflush(stdout);

    return platform->initialize();
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_jutils_platform_jni_JniPlatform_destroy(
    JNIEnv *env, jobject jthis)
{
    bool result = false;

    printf("DEBUG: JNI Destroy - Entered\n");

    auto platform = CUtils::getPlatform();

    printf("DEBUG: JNI Destroy - Platform received\n");

    if (platform)
    {
        result = platform->destroy();

        printf("DEBUG: JNI Destroy - Platform destroyed\n");
    }

    printf("DEBUG: JNI Destroy - Exiting\n");

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
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

    env->CallObjectMethod(jports, clearMethod);

    for (string p : ports)
    {
        jstring jPort = string_to_jstring(env, p);

        env->CallObjectMethod(jports, addMethod, jPort);
    }

    env->DeleteLocalRef(listCls);

    return result;
}

/*******************************************************************************
 *
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_jutils_platform_jni_JniPlatform_getInvalidId(
    JNIEnv *, jobject)
{
    return CUtils::getInvalidId();
}
