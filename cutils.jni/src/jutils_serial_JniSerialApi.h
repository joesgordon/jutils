/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class jutils_serial_JniSerialApi */

#ifndef _Included_jutils_serial_JniSerialApi
#define _Included_jutils_serial_JniSerialApi
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    listPorts
 * Signature: (Ljava/util/List;)Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_listPorts
  (JNIEnv *, jclass, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    open
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_open
  (JNIEnv *, jobject, jstring);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    register
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_jutils_serial_JniSerialApi_register
  (JNIEnv *, jobject, jobject, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    close
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_close
  (JNIEnv *, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    isOpen
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_isOpen
  (JNIEnv *, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    getConfig
 * Signature: (Ljutils/serial/SerialConfig;)Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_getConfig
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    setConfig
 * Signature: (Ljutils/serial/SerialConfig;)Z
 */
JNIEXPORT jboolean JNICALL Java_jutils_serial_JniSerialApi_setConfig
  (JNIEnv *, jobject, jobject);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    setReadTimeout
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_jutils_serial_JniSerialApi_setReadTimeout
  (JNIEnv *, jobject, jint);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    read
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_jutils_serial_JniSerialApi_read
  (JNIEnv *, jobject, jint);

/*
 * Class:     jutils_serial_JniSerialApi
 * Method:    write
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_jutils_serial_JniSerialApi_write
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif