package com.example.boxing_assistant.Models;

public class ModelTimer {
    private int numberOfRounds;
    private int roundMin;
    private int roundSec;
    private int restMin;
    private int restSec;
    private int prepareMin;
    private int prepareSec;

    public ModelTimer(int numberOfRounds, int roundMin, int roundSec, int restMin, int restSec, int prepareMin, int prepareSec) {
        this.numberOfRounds = numberOfRounds;
        this.roundMin = roundMin;
        this.roundSec = roundSec;
        this.restMin = restMin;
        this.restSec = restSec;
        this.prepareMin = prepareMin;
        this.prepareSec = prepareSec;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public int getRoundMin() {
        return roundMin;
    }

    public void setRoundMin(int roundMin) {
        this.roundMin = roundMin;
    }

    public int getRoundSec() {
        return roundSec;
    }

    public void setRoundSec(int roundSec) {
        this.roundSec = roundSec;
    }

    public int getRestMin() {
        return restMin;
    }

    public void setRestMin(int restMin) {
        this.restMin = restMin;
    }

    public int getRestSec() {
        return restSec;
    }

    public void setRestSec(int restSec) {
        this.restSec = restSec;
    }

    public int getPrepareMin() {
        return prepareMin;
    }

    public void setPrepareMin(int prepareMin) {
        this.prepareMin = prepareMin;
    }

    public int getPrepareSec() {
        return prepareSec;
    }

    public void setPrepareSec(int prepareSec) {
        this.prepareSec = prepareSec;
    }
}
