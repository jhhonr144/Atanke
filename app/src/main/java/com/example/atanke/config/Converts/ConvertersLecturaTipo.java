package com.example.atanke.config.Converts;

import androidx.room.TypeConverter;

import com.example.atanke.general.dto.api.lecturas.LecturaPortadaDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaTipoDTO;

public class ConvertersLecturaTipo {
    @TypeConverter
    public static LecturaTipoDTO fromString(String value) {
        LecturaTipoDTO l = new LecturaTipoDTO();
        String[] ll= value.split(":\\|:");
        l.setId(Integer.parseInt(ll[0]));
        l.setNombre(ll[1]);
        return l;
    }

    @TypeConverter
    public static String toString(LecturaTipoDTO user) {
        String l=user.getId()+":|:"+user.getNombre();
        return l;
    }


}
