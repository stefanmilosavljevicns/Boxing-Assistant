package com.example.boxing_assistant.Models;

public class Model {
    private String title;
    private long time;

    public Model(String title, long time) {
        this.title = title;
        this.time = time;
    }
    public String getTitle(){
        return title;
    }
    public long getTime(){
        return time;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setTime(long time){
        this.time = time;
    }


}
