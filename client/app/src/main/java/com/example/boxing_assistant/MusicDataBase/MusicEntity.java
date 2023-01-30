package com.example.boxing_assistant.MusicDataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MusicEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Music_URL")
    private String musicurl;
    @ColumnInfo(name = "Music_name")
    private String musicname;
    public int getId() {
        return id;
    }

    public String getMusicname() {
        return musicname;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicurl() {
        return musicurl;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }
}
