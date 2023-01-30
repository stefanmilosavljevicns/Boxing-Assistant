package com.example.boxing_assistant.MusicDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MusicEntity.class},version = 3,exportSchema = false)
public abstract class MusicDataBase extends RoomDatabase {
    private static  MusicDataBase database;
    public  abstract  MusicDao mainDao();
    private static String DATABASE_NAME = "Music_Table";
    public synchronized  static MusicDataBase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),MusicDataBase.class,DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }
}
