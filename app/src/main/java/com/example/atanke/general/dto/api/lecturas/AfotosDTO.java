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
@Entity(tableName = "fotos")
public class AfotosDTO implements Serializable {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String url;
    private String contenido;
    private int fk;
    @Ignore
    public AfotosDTO() {}

    @Dao
    public interface fotos {
        @Query("SELECT * FROM lectura_titulo")
        List<AfotosDTO> getAllFotos();
    }
}
