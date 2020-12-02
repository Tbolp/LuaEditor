package com.tbolp.luaeditor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import lombok.Getter;

interface OnFolderSelectListener {
    public void OnFolderSelect(String path);
}


public class OpenFolderFragment extends DialogFragment {

    private OnFolderSelectListener listener_;
    private TextView dir_path_;
    private ListView dir_list_;
    private ArrayAdapter<String> adapter_;
    private String current_path_ = "";

    public  OpenFolderFragment(OnFolderSelectListener listener){
        super();
        listener_= listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;
        int height = size.y;

        window.setLayout((int) (width * 0.75), (int) (height* 0.75));
        window.setGravity(Gravity.CENTER);

        Button positiveButton = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.openfolder, null, false);
        dir_path_ = view.findViewById(R.id.dirpath_textview);
        File external_file = Environment.getExternalStorageDirectory();
        current_path_ = external_file.getAbsolutePath();
        dir_path_.setText(current_path_);
        dir_list_ = view.findViewById(R.id.dir_listview);
        adapter_ = new DirAdapter(getActivity());
        File[] files = external_file.listFiles();
        adapter_.clear();
        adapter_.add("..");
        if(files != null) {
            for (int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    adapter_.add(files[i].getName());
                }
            }
        }
        dir_list_.setAdapter(adapter_);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener_ != null){
                    listener_.OnFolderSelect(current_path_);
                }
            }
        });
        return builder.create();
    }

    private class DirAdapter extends ArrayAdapter<String>{
        public DirAdapter(Context context){
            super(context, 0, new ArrayList<>());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView dir_name = new TextView(getContext());
            dir_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            dir_name.setText(getItem(position));
            dir_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp_path = current_path_ + File.separator +  getItem(position);
                    File file = new File(temp_path);
                    if(file.exists() == false){
                        return;
                    }
                    try {
                        current_path_ = file.getCanonicalPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dir_path_.setText(current_path_);
                    File[] files = file.listFiles();
                    adapter_.clear();
                    adapter_.add("..");
                    if(files != null) {
                        for (int i = 0; i < files.length; ++i) {
                            if (files[i].isDirectory()) {
                                adapter_.add(files[i].getName());
                            }
                        }
                    }
                }
            });
            return dir_name;
        }
    }
}
