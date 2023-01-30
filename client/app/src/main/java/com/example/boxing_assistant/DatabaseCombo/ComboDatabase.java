package com.example.boxing_assistant.DatabaseCombo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Combo.class},version = 2,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class ComboDatabase extends RoomDatabase {
    private static ComboDatabase database;
    public abstract ComboDao mainDao();
    private static String DATABASE_NAME = "Workout_Plans";

    public synchronized static ComboDatabase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), ComboDatabase.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }



}
