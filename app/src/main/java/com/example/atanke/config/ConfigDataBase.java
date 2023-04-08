package com.example.atanke.config;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.registrarusuario.Dao.UsuarioDao;

@Database(entities = {UsuarioDTO.class}, version = 2)
public abstract class ConfigDataBase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();

    private static volatile ConfigDataBase INSTANCE;

    public static ConfigDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ConfigDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ConfigDataBase.class, "database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public static String getDatabaseName() {
        return "database";
    }

}
