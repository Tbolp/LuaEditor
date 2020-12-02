package com.tbolp.luaeditor;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

public class FontAwesomeButton extends AppCompatButton {

    private static Typeface awesome_font_;

    public FontAwesomeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesomeButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FontAwesomeButton(Context context){
        super(context);
        init();
    }

    private void init(){
        if(awesome_font_ == null) {
            awesome_font_ = Typeface.createFromAsset(getContext().getAssets(), "fonts/fontawesome-webfont.ttf");
        }
        setTypeface(awesome_font_);
    }
}
