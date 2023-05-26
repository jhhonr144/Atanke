package com.example.atanke.general.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.general.dto.api.palabras.MultimediaDTO;
import com.example.atanke.palabras.models.letraGruop;

import java.util.List;


@Dao
public interface BDPalabraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BDPalabraDTO palabra);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertm(MultimediaDTO contenido);

    @Query("SELECT * FROM palabras")
    List<BDPalabraDTO> getAllPalabras();


    @Query("DELETE FROM palabras")
    void dellAll();

    @Query("SELECT count(*) cantidad,letra FROM palabras GROUP BY letra")
    List<letraGruop> getGroup();

    @Query("SELECT * FROM palabras WHERE letra=:letra ORDER BY palabra ASC")
    List<BDPalabraDTO> getPalabra(String letra);
}
