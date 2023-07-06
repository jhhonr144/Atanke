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
@Entity(tableName = "multimedia")
public class MultimediaDTO {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String descripcion;
    private String multimedia;
    private String fk_user;
    private String fk_tm;
    @Ignore
    private palabraMultiDTO pivot;
    public MultimediaDTO() {}
    @Dao
    public interface MultimediaDao {
        @Query("SELECT * FROM multimedia")
        List<BDLecturaContenidoDTO> getAllMultimedia();
    }
}
