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
@Entity(tableName = "config")
public class ConfigDTO {
   @PrimaryKey(autoGenerate = false)
   private int id;
   private String nombre;
   private String info;
   private String fecha;
   @Ignore
   public ConfigDTO() {}

   @Dao
   public interface ConfigDao {
      @Query("SELECT * FROM config")
      List<ConfigDTO> getAllconfig();
   }

   @Dao
   public interface ConfigWhereDao {
      @Query("SELECT * FROM config WHERE nombre=:valor")
      List<ConfigDTO> getAllconfig(String valor);
   }
}
