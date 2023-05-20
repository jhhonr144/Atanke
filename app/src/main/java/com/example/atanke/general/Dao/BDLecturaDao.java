package com.example.atanke.general.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;

import java.util.List;


@Dao
public interface BDLecturaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BDLecturaDTO lecturaTitulo);

    @Query("SELECT * FROM lectura_titulo")
    List<BDLecturaDTO> getAllLectura_titulo();

    @Query("DELETE FROM lectura_titulo WHERE id=:id")
    void deleteById(String id);
    @Query("DELETE FROM lectura_titulo")
    void deleteAll();
}
