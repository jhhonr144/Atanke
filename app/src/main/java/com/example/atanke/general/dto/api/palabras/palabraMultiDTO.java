package com.example.atanke.general.dto.api.palabras;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "palabramulti")
public class palabraMultiDTO {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String fk_palabra;
    private String fk_multimedia;
    @Ignore
    public palabraMultiDTO() {}
    @Dao
    public interface palabraMultiDao {
        @Query("SELECT * FROM palabramulti")
        List<palabraMultiDTO> getAllpalabraMulti();
    }
}
