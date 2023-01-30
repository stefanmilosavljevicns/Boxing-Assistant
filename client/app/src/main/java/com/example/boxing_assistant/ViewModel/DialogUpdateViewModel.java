package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.boxing_assistant.Models.PrimitiveTimer;

import java.util.ArrayList;

public class DialogUpdateViewModel {
    private MutableLiveData<ArrayList<String>> name;
    private MutableLiveData<PrimitiveTimer> min;
    private MutableLiveData<ArrayList<String>> getCurrentName(){
        if(name == null){
            name = new MutableLiveData<>();
        }
        return name;
    }
    public MutableLiveData<ArrayList<String>> getNameData(){
        return name;
    }
}
