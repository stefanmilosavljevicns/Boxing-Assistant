package com.example.boxing_assistant.DataBaseTimer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Time.class}, version = 2,exportSchema = false)
public abstract  class TimeDatabase extends RoomDatabase {
    private static TimeDatabase database;
    public abstract TImerDao mainDao();
    private static String DATABASE_NAME = "Time_Plans";
    public synchronized static TimeDatabase getInstance(Context context){
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), TimeDatabase.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }
}
