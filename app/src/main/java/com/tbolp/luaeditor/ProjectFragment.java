package com.tbolp.luaeditor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class ProjectFragment extends Fragment implements OnFolderSelectListener {

    private Button open_folder_ = null;
    private WebView project_tree_ = null;
    private FontAwesomeButton create_file_;
    private FontAwesomeButton create_dir_;
    private FontAwesomeButton delete_file_or_dir_;
    private String current_select_path_ = "";
    private String project_root_;
    private View project_title_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_project, container, false);
        open_folder_ = (Button) view.findViewById(R.id.openfolder);
        project_tree_ = (WebView) view.findViewById(R.id.project_view);
        project_title_ = view.findViewById(R.id.project_title);
        create_file_ = view.findViewById(R.id.create_file);
        create_dir_ = view.findViewById(R.id.create_directory);
        delete_file_or_dir_ = view.findViewById(R.id.delete_file_or_dir);
        project_title_.setVisibility(View.INVISIBLE);
        project_tree_.setVisibility(View.INVISIBLE);
        // 选择工程目录
        open_folder_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFolderFragment newFragment = new OpenFolderFragment(ProjectFragment.this::OnFolderSelect);
                newFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        // 创建文件
        create_file_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(ProjectFragment.this.current_select_path_);
                if (file.exists()) {
                    EditText edit = new EditText(ProjectFragment.this.getContext());
                    edit.setSingleLine(true);
                    // 提示输入文件名
                    new AlertDialog.Builder(ProjectFragment.this.getContext())
                            .setTitle(R.string.input_name)
                            .setView(edit)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String file_name = edit.getText().toString();
                                    if (file_name == "") {
                                        return;
                                    }
                                    // 产生文件位置
                                    String file_path = "";
                                    if (file.isDirectory()) {
                                        file_path = file.getAbsolutePath() + File.separator + file_name;
                                    } else {
                                        file_path = file.getParentFile().getAbsolutePath() + File.separator + file_name;
                                    }
                                    // 创建文件
                                    File new_file = new File(file_path);
                                    try {
                                        new_file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(ProjectFragment.this.getContext(), R.string.create_file_error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .create().show();
                }
            }
        });
        // 创建文件夹
        create_dir_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(ProjectFragment.this.current_select_path_);
                if (file.exists()) {
                    EditText edit = new EditText(ProjectFragment.this.getContext());
                    edit.setSingleLine(true);
                    // 提示输入文件夹名称
                    new AlertDialog.Builder(ProjectFragment.this.getContext())
                            .setTitle(R.string.input_name)
                            .setView(edit)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String file_name = edit.getText().toString();
                                    if (file_name == "") {
                                        return;
                                    }
                                    // 产生文件夹位置
                                    String file_path = "";
                                    if (file.isDirectory()) {
                                        file_path = file.getAbsolutePath() + File.separator + file_name;
                                    } else {
                                        file_path = file.getParentFile().getAbsolutePath() + File.separator + file_name;
                                    }
                                    // 创建文件夹
                                    File new_file = new File(file_path);
                                    if(!new_file.mkdir()) {
                                        Toast.makeText(ProjectFragment.this.getContext(), R.string.create_dir_error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .create().show();
                }
            }
        });
        // 删除文件夹或文件
        delete_file_or_dir_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 禁止删除根目录
                if(ProjectFragment.this.current_select_path_ == ProjectFragment.this.project_root_){
                    return;
                }
                File file = new File(ProjectFragment.this.current_select_path_);
                if(file.exists()){
                    new AlertDialog.Builder(ProjectFragment.this.getContext())
                            .setTitle(R.string.are_you_sure)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!file.delete()){
                                        Toast.makeText(ProjectFragment.this.getContext(), R.string.delete_error, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }).create().show();
                }
            }
        });
        return view;
    }

    @Override
    public void OnFolderSelect(String path) {
        open_folder_.setVisibility(View.INVISIBLE);
        project_root_ = path;
        // 开启webview调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        project_tree_.getSettings().setJavaScriptEnabled(true);
        project_tree_.addJavascriptInterface(this, "API");
        project_tree_.loadUrl("file:///android_asset/project_webpage/index.html");
        project_title_.setVisibility(View.VISIBLE);
        project_tree_.setVisibility(View.VISIBLE);
    }

    @JavascriptInterface
    public String getRoot() {
        File file = new File(project_root_);
        return file.getName();
    }

    @JavascriptInterface
    public String getRootFullPath() {
        File file = new File(project_root_);
        return file.getPath();
    }

    @JavascriptInterface
    public String listFile(String path) throws JSONException {
        File file = new File(path);
        File[] items = file.listFiles();
        JSONArray ret = new JSONArray();
        if (items != null) {
            for (File item : items) {
                JSONObject node = new JSONObject();
                node.put("name", item.getName());
                node.put("leaf", !item.isDirectory());
                ret.put(node);
            }
        }
        return ret.toString();
    }

    @JavascriptInterface
    public void setCurrentPath(String path) {
        SharedData data = new ViewModelProvider(getActivity()).get(SharedData.class);
        data.current_path.postValue(path);
        current_select_path_ = path;
    }
}
