package com.example.atanke.general.dto;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

   @Ignore
   public UsuarioDTO() {}
}
