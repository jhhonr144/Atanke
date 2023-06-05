package com.example.atanke.general.dto.api.palabras;

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

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "palabras_r")
public class BDPalabraRelacionDTO implements Serializable {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String palabra_id_1;
    private String palabra_id_2;
    private String relacion;
    private String created_at;
    private String updated_at;
    private int fk_user;
    private String estado;
    @Ignore
    public BDPalabraRelacionDTO() {}
    @Dao
    public interface PalabrasDao {
        @Query("SELECT * FROM palabras_r")
        List<BDPalabraRelacionDTO> getAllPalabra_r();
    }
}