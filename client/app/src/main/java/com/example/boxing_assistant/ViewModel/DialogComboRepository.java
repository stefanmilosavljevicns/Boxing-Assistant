package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.boxing_assistant.DatabaseCombo.Combo;
import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.Models.ModelTimer;

import java.util.ArrayList;

public class DialogComboRepository {
    private static DialogComboRepository instance;
    public ArrayList<Model> dataSet = new ArrayList<>();
    public ModelTimer modTime = new ModelTimer(1,0,0,0,0,0,0);
    public String titleSet;
    public Boolean musicPlayer = false;
    public Combo databSet;
    public static DialogComboRepository getInstance(){
        if(instance == null){
        instance = new DialogComboRepository();
        }
        return instance;
    }
    public MutableLiveData<String> getTitle(){
        MutableLiveData<String> title = new MutableLiveData<>();
        title.setValue(titleSet);
        return  title;
    }
    public MutableLiveData<Combo> getDataB(){
        MutableLiveData<Combo> datab = new MutableLiveData<>();
        datab.setValue(databSet);
        return datab;
    }
    public MutableLiveData<Boolean> getMusic(){
        MutableLiveData<Boolean> music = new MutableLiveData<>();
        music.setValue(musicPlayer);
        return music;
    }
    public  MutableLiveData<ArrayList<Model>> getData(){
        MutableLiveData<ArrayList<Model>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
    public MutableLiveData<ModelTimer> getTimer(){
        MutableLiveData<ModelTimer> dataTimer = new MutableLiveData<>();
        dataTimer.setValue(modTime);

        return dataTimer;

    }

    private void setDatab(Combo combo){databSet = combo;}
    private void setTimer(ModelTimer modTimer) {modTime = modTimer;}
    private void setData(Model mod){
        dataSet.add(mod);
    }
    private void changeData(ArrayList<Model> mod){
        dataSet = mod;
    }
    private void clearData(){
        dataSet.clear();
    }

}
