package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.boxing_assistant.Models.PrimitiveTimer;

import java.util.ArrayList;

public class DialogRepository {
    private static DialogRepository instance;
    public ArrayList<String> dataSet = new ArrayList<>();
    public PrimitiveTimer prim = new PrimitiveTimer(0,0);
    public static DialogRepository getInstance(){
        if(instance == null){
            instance = new DialogRepository();
        }
        return instance;
    }
    public MutableLiveData<PrimitiveTimer> getTimer(){
        MutableLiveData<PrimitiveTimer> data = new MutableLiveData<>();
        data.setValue(prim);
        return data;
    }
    public MutableLiveData<ArrayList<String>> getData(){
        MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
    public void clear(){dataSet.clear();prim.setMin(0);prim.setSec(0);}
}
