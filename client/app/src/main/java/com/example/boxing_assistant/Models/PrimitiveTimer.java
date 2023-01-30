package com.example.boxing_assistant.Models;

public class PrimitiveTimer {
    private int min;
    private int sec;

    public PrimitiveTimer(int min, int sec) {
        this.min = min;
        this.sec = sec;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }
}
