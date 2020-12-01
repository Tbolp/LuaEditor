package com.tbolp.luaeditor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class FolderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View)inflater.inflate(R.layout.folder, container, false);
        Button open_folder_ = (Button)view.findViewById(R.id.openfolder);
        open_folder_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFolderFragment newFragment = new OpenFolderFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "");

            }
        });
        return view;
    }
}
