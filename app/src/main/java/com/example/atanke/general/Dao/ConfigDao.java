package com.example.atanke.general.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.ConfigDTO;

import java.util.List;

@Dao
public interface ConfigDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConfigDTO config);

    @Query("SELECT * FROM config")
    List<ConfigDTO> getAllconfig();
}
