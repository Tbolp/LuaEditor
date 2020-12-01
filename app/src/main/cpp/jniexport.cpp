//
// Created by Tbolp on 2020/11/28.
//

#include <memory>
#include <sstream>
#include <iostream>
#include <jni.h>
#include "src/lua.hpp"

static int print_cpp(lua_State* L){
    int nargs = lua_gettop(L);

    for (int i=1; i <= nargs; i++) {
        if (lua_isstring(L, i)) {
            /* Pop the next arg using lua_tostring(L, i) and do your print */
            std::cout << lua_tostring(L,i);
        }
        else {
            /* Do something with non-strings if you like */
        }
    }

    std::cout << std::endl;
    return 0;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_tbolp_luaeditor_LuaInterpreter_luaL_1newState(JNIEnv *env, jobject thiz) {
    return (jlong)luaL_newstate();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tbolp_luaeditor_LuaInterpreter_lua_1close(JNIEnv *env, jobject thiz, jlong instance) {
    lua_close((lua_State*)instance);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_tbolp_luaeditor_LuaInterpreter_luaL_1openlibs(JNIEnv *env, jobject thiz, jlong instance) {
    lua_State * L= (lua_State*)instance;
    luaL_openlibs(L);
    lua_register(L, "print", &print_cpp);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_tbolp_luaeditor_LuaInterpreter_luaL_1dostring(JNIEnv *env, jobject thiz, jlong instance,
                                                       jstring content) {
    std::stringstream buffer;
    std::streambuf * old = std::cout.rdbuf(buffer.rdbuf());
    lua_State * L = (lua_State*)instance;
    auto ret = luaL_dostring(L, env->GetStringUTFChars(content, nullptr));
    if(ret == LUA_OK) {
        lua_pushstring(L, buffer.str().c_str());
    }
    std::cout.rdbuf(old);
    return ret;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tbolp_luaeditor_LuaInterpreter_lua_1tostring(JNIEnv *env, jobject thiz, jlong instance,
                                                      jlong index) {
    return env->NewStringUTF(lua_tostring((lua_State*)instance, index));
}

//extern "C"
//JNIEXPORT jstring


//JNICALL
//Java_com_tbolp_luaeditor_LuaInterpreter_DoString_1(JNIEnv *env, jobject thiz, jstring content) {
//    std::unique_ptr<lua_State, decltype(&lua_close)> stackmgr(luaL_newstate(), &lua_close);
//    lua_State* stack = stackmgr.get();
//    jstring ret = nullptr;
//    if(stack == nullptr){
//        ret = env->NewStringUTF("Create Lua Stack Error!!!");
//    }else {
//        luaL_openlibs(stack);
//        lua_register(stack, "print", &print_cpp);
//        std::stringstream buffer;
//        std::streambuf * old = std::cout.rdbuf(buffer.rdbuf());
//        std::cout << "stdcout" << std::endl;
//        fputs("fputs\n", stdout);
//        if (luaL_dostring(stack, env->GetStringUTFChars(content, nullptr)) == LUA_OK) {
//            auto out = buffer.str();
//            ret = nullptr;
//        } else {
//            ret = env->NewStringUTF(lua_tostring(stack, -1));
//        }
//    }
//    return ret;
//}
