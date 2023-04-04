package com.example.atanke.registrarusuario.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.atanke.general.dto.UsuarioDTO;

import java.util.List;

@Dao
public interface  UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UsuarioDTO usuario);

    @Query("SELECT * FROM usuarios")
    List<UsuarioDTO> getAllUsers();
}
