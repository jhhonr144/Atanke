package com.example.atanke.config.Converts;

import androidx.room.TypeConverter;

import com.example.atanke.general.dto.api.lecturas.LecturaPortadaDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaUserDTO;

public class ConvertersLecturaPortada {
    @TypeConverter
    public static LecturaUserDTO fromString(String value) {
        LecturaUserDTO l = new LecturaUserDTO();
        String[] ll= value.split(":\\|:");
        l.setId(Integer.parseInt(ll[0]));
        l.setName(ll[1]);
        return l;
    }

    @TypeConverter
    public static String toString(LecturaUserDTO user) {
        String l=user.getId()+":|:"+user.getName();
        return l;
    }
}
