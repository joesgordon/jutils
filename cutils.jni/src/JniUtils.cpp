
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
void *getPointer(JNIEnv *env, jobject jobj, const char *name)
{
    jlong value = getLong(env, jobj, name);

    return reinterpret_cast<void *>(value);
}

/*******************************************************************************
 *
 ******************************************************************************/
void setPointer(JNIEnv *env, jobject jobj, const char *name, void *value)
{
    jlong longValue = reinterpret_cast<jlong>(value);

    setLong(env, jobj, name, longValue);
}

} // namespace CUtils
