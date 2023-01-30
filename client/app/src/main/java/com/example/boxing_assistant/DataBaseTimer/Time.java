package com.example.boxing_assistant.DataBaseTimer;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Time {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Plan_Name")
    private String name;
    @ColumnInfo(name = "Number_of_rounds")
    private int roundNumber;
    @ColumnInfo(name = "Round_min")
    private int roundMin;
    @ColumnInfo(name = "Round_sec")
    private int roundSec;
    @ColumnInfo(name = "Rest_min")
    private int restMin;
    @ColumnInfo(name = "Rest_sec")
    private int restSec;
    @ColumnInfo(name = "Prepare_min")
    private int preparetMin;
    @ColumnInfo(name = "Prepare_sec")
    private int prepareSec;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPreparetMin() {
        return preparetMin;
    }

    public void setPreparetMin(int preparetMin) {
        this.preparetMin = preparetMin;
    }

    public int getPrepareSec() {
        return prepareSec;
    }

    public void setPrepareSec(int prepareSec) {
        this.prepareSec = prepareSec;
    }
}
