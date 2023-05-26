package com.example.atanke.general.dto.api.palabras;

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
@Entity(tableName = "palabras")
public class BDPalabraDTO implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String palabra;
    private String letra;
    private String pronunciar;
    private String created_at;
    private String updated_at;
    private int fk_user;
    private int fk_idioma;
    private String estado;
    private int multilent;
    @Ignore
    private List<MultimediaDTO> multimedia;
    public BDPalabraDTO() {}
    @Dao
    public interface PalabrasDao {
        @Query("SELECT * FROM palabras")
        List<UsuarioDTO> getAllPalabras();
    }
}
/*
      "estado": "aprobado",
      "multilent": 0,
      "multimedia": []
* */