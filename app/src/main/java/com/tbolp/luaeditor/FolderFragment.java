package com.tbolp.luaeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FolderFragment extends Fragment implements OnFolderSelectListener{

    private Button open_folder_ = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.project, container, false);
        open_folder_ = (Button)view.findViewById(R.id.openfolder);
        open_folder_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFolderFragment newFragment = new OpenFolderFragment(FolderFragment.this::OnFolderSelect);
                newFragment.show(getActivity().getSupportFragmentManager(), "");
            }
        });
        return view;
    }

    @Override
    public void OnFolderSelect(String path) {
        open_folder_.setVisibility(View.INVISIBLE);
    }
}
