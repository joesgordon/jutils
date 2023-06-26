
#include "org_jutils_serial_JnaSerialPort.h"

JNIEXPORT jboolean JNICALL Java_org_jutils_serial_SerialPort_listPorts
(JNIEnv* env, jclass clz, jobject obj)
{
	jboolean status = false;

	return status;
}


JNIEXPORT jboolean JNICALL Java_org_jutils_serial_SerialPort_getPortConfig
(JNIEnv* env, jclass clz, jobject jcfg)
{
	jboolean status = false;

	jclass cfgClz = env->GetObjectClass(jcfg);
	jfieldID baudId = env->GetFieldID(cfgClz, "baudRate", "I");



	return status;
}
