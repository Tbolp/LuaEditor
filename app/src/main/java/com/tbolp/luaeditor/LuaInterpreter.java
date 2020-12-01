package com.tbolp.luaeditor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lombok.Getter;

import static java.lang.System.loadLibrary;

class LuaRet{
    @Getter
    public boolean ret;
    @Getter
    public String message;
};

public class LuaInterpreter {
    static {
        loadLibrary("lua-5.4.1");
    }

    LuaInterpreter(){
        lua_stack_ = luaL_newState();
        if(lua_stack_ == 0){
            throw new ExceptionInInitializerError();
        }
        luaL_openlibs(lua_stack_);
    }

    public void Close(){
        lua_close(lua_stack_);
    }

    public LuaRet DoString(String content){
        LuaRet ret = new LuaRet();
        if(luaL_dostring(lua_stack_, content) == 0){
            ret.ret = true;
        }else{
            ret.ret = false;
        }
        ret.message = lua_tostring(lua_stack_, -1);
        return ret;
    }

    private long lua_stack_;

    private native long luaL_newState();
    private native void luaL_openlibs(long instance);
    private native void lua_close(long instance);
    private native long luaL_dostring(long instance, String content);
    private native String lua_tostring(long instance, long index);

}
