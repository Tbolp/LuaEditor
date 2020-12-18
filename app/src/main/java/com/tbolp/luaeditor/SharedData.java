package com.tbolp.luaeditor;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedData extends ViewModel {
    public MutableLiveData<String> current_path = new MutableLiveData<>();
}
