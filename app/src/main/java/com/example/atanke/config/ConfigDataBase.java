package com.example.atanke.config;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.atanke.config.Converts.ConvertersLecturaContenido;
import com.example.atanke.config.Converts.ConvertersLecturaPortada;
import com.example.atanke.config.Converts.ConvertersLecturaTipo;
import com.example.atanke.config.Converts.ConvertersLecturaUser;
import com.example.atanke.general.Dao.BDLecturaDao;
import com.example.atanke.general.Dao.BDLecturaSesionDao;
import com.example.atanke.general.Dao.BDPalabraDao;
import com.example.atanke.general.Dao.ConfigDao;
import com.example.atanke.general.dto.ConfigDTO;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.general.Dao.UsuarioDao;
import com.example.atanke.general.dto.api.lecturas.AfotosDTO;
import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
import com.example.atanke.general.dto.api.palabras.BDPalabraRelacionDTO;
import com.example.atanke.general.dto.api.palabras.MultimediaDTO;
import com.example.atanke.general.dto.api.palabras.palabraMultiDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;

@Database(entities = {
    UsuarioDTO.class,
    ConfigDTO.class,
    BDLecturaDTO.class,
    BDLecturaSesionDTO.class,
    BDLecturaContenidoDTO.class,
    BDPalabraDTO.class,
    MultimediaDTO.class,
    BDPalabraRelacionDTO.class,
    AfotosDTO.class,
    palabraMultiDTO.class
}, version = 10)
@TypeConverters({
    ConvertersLecturaUser.class,
    ConvertersLecturaPortada.class,
    ConvertersLecturaTipo.class,
    ConvertersLecturaContenido.class})
public abstract class ConfigDataBase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract ConfigDao configDao();
    public abstract BDLecturaDao BDLecturaDao();
    public abstract BDLecturaSesionDao BDLecturaSesionDao();
    public abstract BDPalabraDao BDPalabraDao();

    private static volatile ConfigDataBase INSTANCE;

    public static ConfigDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ConfigDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ConfigDataBase.class, "database")
                            .fallbackToDestructiveMigration()
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
