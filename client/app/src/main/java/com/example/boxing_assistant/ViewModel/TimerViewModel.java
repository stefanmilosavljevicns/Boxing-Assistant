package com.example.boxing_assistant.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.Models.ModelTimer;

public class TimerViewModel extends ViewModel {
    private BasicTimerRepository dig;
    private MutableLiveData<ModelTimer> timer;
    private MutableLiveData<Time> time;
    private MutableLiveData<String> title;
    private MutableLiveData<Boolean> music;
    public void init(){
        if(timer != null){
            return;
        }
        dig = BasicTimerRepository.getInstance();
        music = dig.getMusic();
        timer = dig.getTimer();
        time = dig.getTime();
        title = dig.getTitle();
    }
    public MutableLiveData<ModelTimer> getTimerData(){
        return timer;
    }
    public MutableLiveData<Time> getTime() {return  time;}
    public MutableLiveData<String> getTitle() {return  title;}
    public MutableLiveData<Boolean> getMusic() {return music;}
}
