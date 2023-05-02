package com.example.atanke.general.dto;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Entity(tableName = "usuarios")
public class UsuarioDTO {
   private String name;
   private String email;
   private Integer r_users_roles;
   private Integer r_users_estados;
   private String updated_at;
   private String created_at;
   @PrimaryKey(autoGenerate = false)
   private int id;
   private String token;
   private String image_path;
   @Ignore
   public UsuarioDTO() {}

   @Dao
   public interface UsuarioDao {
      @Query("SELECT * FROM usuarios")
      List<UsuarioDTO> getAllUsuarios();
   }

}
