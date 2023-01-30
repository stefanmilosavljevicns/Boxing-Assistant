package com.example.boxing_assistant.API;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetWorkoutByNamePatch {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("moves")
    public ArrayList<String> moves;
    @SerializedName("movesTime")
    public ArrayList<Long> movesTime;
    @SerializedName("roundNumber")
    public int roundNumber;
    @SerializedName("roundMin")
    public int roundMin;
    @SerializedName("roundSec")
    public int roundSec;
    @SerializedName("restMin")
    public int restMin;
    @SerializedName("restSec")
    public int restSec;
    @SerializedName("prepareMin")
    public int prepareMin;
    @SerializedName("prepareSec")
    public int prepareSec;

    public GetWorkoutByNamePatch(String id, String name, ArrayList<String> moves, ArrayList<Long> movesTime, int roundNumber, int roundMin, int roundSec, int restMin, int restSec, int prepareMin, int prepareSec) {
        this.id = id;
        this.name = name;
        this.moves = moves;
        this.movesTime = movesTime;
        this.roundNumber = roundNumber;
        this.roundMin = roundMin;
        this.roundSec = roundSec;
        this.restMin = restMin;
        this.restSec = restSec;
        this.prepareMin = prepareMin;
        this.prepareSec = prepareSec;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<String> moves) {
        this.moves = moves;
    }

    public ArrayList<Long> getMovesTime() {
        return movesTime;
    }

    public void setMovesTime(ArrayList<Long> movesTime) {
        this.movesTime = movesTime;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
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
