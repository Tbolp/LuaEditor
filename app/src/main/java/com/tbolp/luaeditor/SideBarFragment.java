package com.tbolp.luaeditor;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class SideBarFragment extends Fragment {

    private ViewPager2 content_viewpage_;
    private ContentAdapter content_adapter_;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sidebar, container, false);
        content_viewpage_ = (ViewPager2)view.findViewById(R.id.content_viewpage);
        content_adapter_ = new ContentAdapter(this);
        content_viewpage_.setAdapter(content_adapter_);
        Button file_btn = (Button)view.findViewById(R.id.explorer_button);
        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_viewpage_.setCurrentItem(0);
            }
        });
        Button run_btn = (Button)view.findViewById(R.id.run_lua_button);
        run_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_viewpage_.setCurrentItem(1);
            }
        });
        return view;
    }

    private class ContentAdapter extends FragmentStateAdapter{

        public ContentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment ret = null;
            switch (position){
                case 0:
                    ret = new FolderFragment();
                    break;
                case 1:
                    ret = new DebugFragment();
                    break;
            }
            return ret;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
