package com.example.atanke.general.dto.api.lecturas;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import com.example.atanke.general.dto.UsuarioDTO;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "lectura_titulo")
public class BDLecturaDTO implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String nombre;
    private int fk_portada;
    private int fk_tipo;
    private int user_id;
    private String created_at;
    private String updated_at;
    private String descripcion;
    private int cantidad_sesiones;
    private LecturaUserDTO user;
    private LecturaPortadaDTO portada;
    private LecturaTipoDTO tipo;
    @Ignore
    public BDLecturaDTO() {}

    @Dao
    public interface LectutaTituloDao {
        @Query("SELECT * FROM lectura_titulo")
        List<UsuarioDTO> getAllLectutaTitulo();
    }
}
