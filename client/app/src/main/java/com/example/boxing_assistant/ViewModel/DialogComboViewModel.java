package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.Models.ModelTimer;

import java.util.ArrayList;

public class DialogComboViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Model>> name;
    private MutableLiveData<ModelTimer> timer;
    private MutableLiveData<Combo> data;
    private MutableLiveData<String> title;

    private MutableLiveData<Boolean> music;
    public void init(){
        if(name!= null){
            return;
        }
         DialogComboRepository dig;
        dig = DialogComboRepository.getInstance();
        name = dig.getData();
        timer = dig.getTimer();
        data = dig.getDataB();
        title = dig.getTitle();
        music = dig.getMusic();
    }
    public MutableLiveData<String> getTitle(){return title;}
    public MutableLiveData<ArrayList<Model>> getNameData(){
           return name;
    }
    public MutableLiveData<ModelTimer> getTimerData(){
        return timer;
    }
    public MutableLiveData<Combo> getDatab(){
        return data;
    }
    public MutableLiveData<Boolean> getMusic() {return music;}
}
