package com.example.atanke.general.dto.api.sesiones;


import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaTipoDTO;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "lectura_contenido")
public class BDLecturaContenidoDTO implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String contenido;
    private int posicion;
    private int fk_tipo;
    private int fk_sesion;
    private String created_at;
    private String updated_at;
    private LecturaTipoDTO tipo_contenido;

    @Ignore
    public BDLecturaContenidoDTO() {}
    @Dao
    public interface LecturaContenidoDao {
        @Query("SELECT * FROM lectura_contenido")
        List<BDLecturaContenidoDTO> getAllLecturaContenido();
    }
}
