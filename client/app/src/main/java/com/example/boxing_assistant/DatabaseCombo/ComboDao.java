package com.example.boxing_assistant.DatabaseCombo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

    @Dao
    public interface ComboDao {
        @Insert(onConflict =  REPLACE)
        void insert(Combo mainCombo);
        @Delete
        void delete(Combo mainCombo);
        @Query("SELECT * FROM Combo")
        List<Combo> getAllUsers();
        @Update
        void update(Combo combo);
        @Query("SELECT * FROM Combo WHERE ID = :id")
        Combo findByUserId(int id);
        @Query("SELECT Plan_Name FROM Combo")
        List<String> getAllNames();

    }

