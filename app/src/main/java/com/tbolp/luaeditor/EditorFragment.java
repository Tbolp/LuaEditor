package com.tbolp.luaeditor;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbolp.luaeditor.databinding.FragmentEditorBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class EditorFragment extends Fragment {

    public final ObservableField<String> file_name = new ObservableField<>();
    public final ObservableField<String> file_content = new ObservableField<>();
    private FontAwesomeButton run_file_;
    private SharedData share_data_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditorBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editor, container, false);
        View view = binding.getRoot();
        FontAwesomeButton save_file = view.findViewById(R.id.save_file);
        run_file_ = view.findViewById(R.id.run_file);
        share_data_ = new ViewModelProvider(getActivity()).get(SharedData.class);
        // 保存文件
        save_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditorFragment.this.saveFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        binding.setVm(this);
        // 打开文件
        share_data_.current_path.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                File file = new File(s);
                if (file.isFile() && file.canRead()) {
                    EditorFragment.this.file_name.set(file.getName());
                    BufferedReader reader = null;
                    try {
                        // 读取文件内容并显示
                        reader = new BufferedReader(new FileReader(file));
                        StringBuffer content = new StringBuffer();
                        while (reader.ready()) {
                            content.append(reader.readLine());
                            if (reader.ready()) {
                                content.append('\n');
                            }
                        }
                        EditorFragment.this.file_content.set(content.toString());
                        reader.close();
                        // lua文件显示运行控件
                        if (file.getName().endsWith(".lua")) {
                            EditorFragment.this.run_file_.setVisibility(View.VISIBLE);
                        } else {
                            EditorFragment.this.run_file_.setVisibility(View.INVISIBLE);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 运行文件
        return view;
    }

    private void saveFile() throws IOException {
        File file = new File(share_data_.current_path.getValue());
        if (file.exists() && file.canWrite()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(EditorFragment.this.file_content.get());
            writer.flush();
        }
    }
}