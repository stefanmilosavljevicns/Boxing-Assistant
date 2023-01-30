package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.Models.ModelTimer;

public class BasicTimerRepository {
    private static BasicTimerRepository instance;
    public ModelTimer modTime = new ModelTimer(1,0,0,0,0,0,0);
    public String titleSet;
    public Boolean musicPlayer = false;
    public Time timeSet;
    public static BasicTimerRepository getInstance(){
        if(instance == null){
            instance = new BasicTimerRepository();
        }
        return instance;
    }
    public MutableLiveData<Time> getTime(){
        MutableLiveData<Time> timeb = new MutableLiveData<>();
        timeb.setValue(timeSet);
        return timeb;
    }
    public MutableLiveData<Boolean> getMusic(){
        MutableLiveData<Boolean> music = new MutableLiveData<>();
        music.setValue(musicPlayer);
        return music;
    }
    public MutableLiveData<ModelTimer> getTimer(){
        MutableLiveData<ModelTimer> dataTimer = new MutableLiveData<>();
        dataTimer.setValue(modTime);

        return dataTimer;

    }
    public MutableLiveData<String> getTitle(){
        MutableLiveData<String> title = new MutableLiveData<>();
        title.setValue(titleSet);
        return  title;

    }

    private void setTimer(ModelTimer modTimer) {modTime = modTimer;}
}
