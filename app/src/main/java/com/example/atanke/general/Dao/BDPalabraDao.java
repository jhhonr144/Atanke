package com.example.atanke.general.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraRelacionDTO;
import com.example.atanke.general.dto.api.palabras.MultimediaDTO;
import com.example.atanke.palabras.models.letraGruop;
import com.example.atanke.palabras.models.palabrasRelacion;

import java.util.List;


@Dao
public interface BDPalabraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BDPalabraDTO palabra);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertm(MultimediaDTO contenido);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertr(BDPalabraRelacionDTO relacion);

    @Query("SELECT * FROM palabras")
    List<BDPalabraDTO> getAllPalabras();

    @Query("SELECT p1.palabra palabra, p2.palabra palabra1 ,p1.id,p1.multilent count,p1.pronunciar,p2.pronunciar pronunciar1  " +
            "FROM palabras_r r " +
            "INNER JOIN palabras p1 ON r.palabra_id_1 = p1.id OR r.palabra_id_2 = p1.id " +
            "INNER JOIN palabras p2 ON r.palabra_id_1 = p2.id OR r.palabra_id_2 = p2.id " +
            "WHERE p1.fk_idioma =1 and p1.id<>p2.id ")
    List<palabrasRelacion> getAllPalabras2();
    @Query("DELETE FROM palabras")
    void dellAll();

    @Query("SELECT COUNT(*) AS cantidad, p1.letra AS letra " +
            "FROM palabras_r r " +
            "INNER JOIN palabras p1 ON r.palabra_id_1 = p1.id OR r.palabra_id_2 = p1.id " +
            "INNER JOIN palabras p2 ON r.palabra_id_1 = p2.id OR r.palabra_id_2 = p2.id " +
            "WHERE p1.fk_idioma = 1 AND p1.id <> p2.id " +
            "GROUP BY p1.letra; ")
    List<letraGruop> getGroup();

    @Query("SELECT * FROM palabras WHERE letra=:letra ORDER BY palabra ASC")
    List<BDPalabraDTO> getPalabra(String letra);
    @Query("SELECT p1.palabra palabra, p2.palabra palabra1 ,p1.id,p1.multilent count,p1.pronunciar,p2.pronunciar pronunciar1 " +
            "FROM palabras_r r " +
            "INNER JOIN palabras p1 ON r.palabra_id_1 = p1.id OR r.palabra_id_2 = p1.id " +
            "INNER JOIN palabras p2 ON r.palabra_id_1 = p2.id OR r.palabra_id_2 = p2.id " +
            "WHERE p1.fk_idioma =1 and p1.id<>p2.id and p1.letra=:letra ")
    List<palabrasRelacion> getPalabra2(String letra);

    @Query("DELETE FROM palabras_r")
    void dellAllRelacion();
}
