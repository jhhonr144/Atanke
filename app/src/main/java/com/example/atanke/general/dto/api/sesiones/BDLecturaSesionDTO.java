package com.example.atanke.general.dto.api.sesiones;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaPortadaDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaTipoDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaUserDTO;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "lectura_sesion")
public class BDLecturaSesionDTO implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String nombre;
    private int posicion;
    private int fk_lectura;
    private String created_at;
    private String updated_at;
    private int contenidos;
    @Ignore
    private List<BDLecturaContenidoDTO> contenido_lecturas;

    public BDLecturaSesionDTO() {}

    @Dao
    public interface LecturaSesionDao {
        @Query("SELECT * FROM lectura_sesion")
        List<BDLecturaSesionDTO> getAllLecturaSesion();
    }
}
