
#include "JniUtils.h"

using std::string;

static jlong INVALID_ID = reinterpret_cast<jlong>(nullptr);

namespace CUtils
{

/*******************************************************************************
 *
 ******************************************************************************/
jlong getInvalidId()
{
    return INVALID_ID;
}

/*******************************************************************************
 *
 ******************************************************************************/
string jstring_to_string(JNIEnv *env, const jstring jstr)
{
    if (!jstr)
    {
        return "";
    }

    const jclass stringClass = env->GetObjectClass(jstr);
    const jmethodID getBytes =
        env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray)env->CallObjectMethod(
        jstr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t)env->GetArrayLength(stringJbytes);
    jbyte *pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string str = std::string((char *)pBytes, length);

    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);
    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);

    return str;
}

/*******************************************************************************
 *
 ******************************************************************************/
jstring string_to_jstring(JNIEnv *env, const string &str)
{
    jclass charsetClass = env->FindClass("java/nio/charset/Charset");
    jmethodID forName = env->GetStaticMethodID(charsetClass, "forName",
        "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
    jstring utf8 = env->NewStringUTF("UTF-8");
    jobject charset = env->CallStaticObjectMethod(charsetClass, forName, utf8);

    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID ctor = env->GetMethodID(
        stringClass, "<init>", "([BLjava/nio/charset/Charset;)V");

    int byteCount = (int)str.length();
    jbyteArray bytes = env->NewByteArray(byteCount);
    const char *cport = str.c_str();
    const signed char *csport = reinterpret_cast<const signed char *>(cport);
    const jbyte *pNativeMessage = static_cast<const jbyte *>(csport);

    env->SetByteArrayRegion(bytes, 0, byteCount, pNativeMessage);

    jstring jstr = reinterpret_cast<jstring>(
        env->NewObject(stringClass, ctor, bytes, charset));

    return jstr;
}

} // namespace CUtils
