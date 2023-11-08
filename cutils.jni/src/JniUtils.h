#pragma once

#include <string>

#include <jni.h>

namespace CUtils
{

jlong getInvalidId();

/*******************************************************************************
 *
 ******************************************************************************/
std::string jstring_to_string(JNIEnv *env, const jstring jstr);

/*******************************************************************************
 *
 ******************************************************************************/
jstring string_to_jstring(JNIEnv *env, const std::string &str);

} // namespace CUtils
