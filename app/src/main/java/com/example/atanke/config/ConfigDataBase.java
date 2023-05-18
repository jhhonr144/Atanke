package com.example.atanke.config;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.Dao.UsuarioDao;

@Database(entities = {UsuarioDTO.class, ConfigDTO.class}, version = 2)
public abstract class ConfigDataBase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract ConfigDao configDao();

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
