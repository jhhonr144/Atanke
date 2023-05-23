package com.example.atanke.general.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;

import java.util.List;


@Dao
public interface BDLecturaSesionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BDLecturaSesionDTO lecturaSesion);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertc(BDLecturaContenidoDTO lecturaContenido);

    @Query("SELECT * FROM lectura_sesion WHERE fk_lectura=:id")
    List<BDLecturaSesionDTO> getSelect_fk_lectura(String id);

    @Query("SELECT * FROM lectura_contenido WHERE fk_sesion=:id ORDER BY posicion ASC")
    List<BDLecturaContenidoDTO> getSelect_fk_sesion(String id);
    @Query("SELECT * FROM lectura_sesion WHERE id=:id")
    List<BDLecturaSesionDTO> getSelect_Id(String id);

    @Query("DELETE FROM lectura_sesion WHERE fk_lectura=:id")
    void deleteByfk_lectura(String id);

    @Query("DELETE FROM lectura_sesion WHERE id=:id")
    void deleteByid(String id);


}
