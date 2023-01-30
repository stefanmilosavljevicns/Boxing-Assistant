package com.example.boxing_assistant.MusicDataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.boxing_assistant.DataBaseTimer.Time;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface MusicDao {
    @Insert (onConflict = REPLACE)
    void insert(MusicEntity music);
    @Delete
    void delete(MusicEntity music);
    @Update
    public void updateSong(MusicEntity musicEntity);
    @Query("SELECT * FROM musicentity")
    List<MusicEntity> getAll();
    @Query("SELECT * FROM musicentity WHERE id = :id")
    MusicEntity findByUserId(int id);

}
