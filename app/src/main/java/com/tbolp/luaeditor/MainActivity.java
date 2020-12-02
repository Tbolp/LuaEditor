package com.tbolp.luaeditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import lombok.Getter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.tbolp.luaeditor.databinding.ActivityMainBinding;

class RunLua implements Runnable{
    RunLua(LuaInterpreter lua, String content, ObservableField<String> output){
        lua_ = lua;
        content_ = content;
        output_ = output;
    }

    public void run(){
        String[] lines = content_.split("[\r]\n");
        int i = 0;
        while(!Thread.currentThread().isInterrupted() && i < lines.length){
            LuaRet ret = lua_.DoString(lines[i]);
            output_ .set(output_.get() + ret.message);
            if(!ret.ret){
                break;
            }
            ++i;
        }
    }

    private final String content_;
    private LuaInterpreter lua_;
    private ObservableField<String> output_;
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        this.input.set("print(\"test\")");
        this.output.set("");
        binding.setVm(this);
        lua_ = new LuaInterpreter();
    }

    @Override
    public void onClick(View v) {
        if(lua_run_task_ == null){
            this.output.set("");
            lua_run_task_ = new Thread(new RunLua(this.lua_, this.input.get(), this.output));
            lua_run_task_.start();
        }else{
            lua_run_task_.interrupt();
            lua_run_task_ = null;
        }
    }

    private Thread lua_run_task_ = null;
    private LuaInterpreter lua_ = null;

    public final ObservableField<String> input = new ObservableField<>();
    public final ObservableField<String> output = new ObservableField<>();

}