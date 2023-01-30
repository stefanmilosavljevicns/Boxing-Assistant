package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boxing_assistant.Models.PrimitiveTimer;

import java.util.ArrayList;

public class DialogViewModel extends ViewModel {
    private MutableLiveData<ArrayList<String>> name;
    private MutableLiveData<PrimitiveTimer> min;


    public void init() {
        if (name != null) {
            return;
        }
        DialogRepository dig;
        dig = DialogRepository.getInstance();
        name = dig.getData();
        min = dig.getTimer();
    }

    public MutableLiveData<ArrayList<String>> getNameData() {
        return name;
    }

    public MutableLiveData<PrimitiveTimer> getTimerData() {
        return min;
    }
}

