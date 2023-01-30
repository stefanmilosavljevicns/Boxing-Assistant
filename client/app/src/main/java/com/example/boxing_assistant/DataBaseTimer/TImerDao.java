package com.example.boxing_assistant.DataBaseTimer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface TImerDao {
    @Insert(onConflict = REPLACE)
    void insert(Time time);
    @Delete
    void delete(Time time);
    @Query("SELECT * FROM time")
    List<Time> getAllData();
    @Update
    void update(Time time);
    @Query("SELECT * FROM time WHERE id = :id")
    Time findByUserId(int id);
    @Query("SELECT Plan_Name FROM time")
    List<String> getAlltitles();
    @Query("SELECT id FROM time")
    List<Integer> getAllId();

}
