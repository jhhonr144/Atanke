package com.example.atanke.config.Converts;

import androidx.room.TypeConverter;

import com.example.atanke.general.dto.api.lecturas.LecturaPortadaDTO;
import com.example.atanke.general.dto.api.lecturas.LecturaUserDTO;

public class ConvertersLecturaUser {
    @TypeConverter
    public static LecturaPortadaDTO fromString(String value) {
        LecturaPortadaDTO l = new LecturaPortadaDTO();
        String[] ll= value.split(":\\|:");
        l.setId(Integer.parseInt(ll[0]));
        l.setMultimedia(ll[1]);
        return l;
    }

    @TypeConverter
    public static String toString(LecturaPortadaDTO user) {
        String l=user.getId()+":|:"+user.getMultimedia();
        return l;
    }


}
