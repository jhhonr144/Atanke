package com.example.atanke.config.Converts;

import androidx.room.TypeConverter;

import com.google.gson.reflect.TypeToken;
import com.example.atanke.general.dto.api.sesiones.BDLecturaContenidoDTO;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;

public class ConvertersLecturaContenido {
    @TypeConverter
    public static String fromList(List<BDLecturaContenidoDTO> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<BDLecturaContenidoDTO> toList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<BDLecturaContenidoDTO>>() {}.getType();
        return gson.fromJson(json, type);
    }

   /* @TypeConverter
    public static BDLecturaContenidoDTO fromString(String value) {
        BDLecturaContenidoDTO l = new BDLecturaContenidoDTO();
        String[] ll= value.split("::");
        l.setId(Integer.parseInt(ll[0]));
        l.setContenido(ll[1]);
        l.setPosicion(Integer.parseInt(ll[2]));
        l.setFk_tipo(Integer.parseInt(ll[3]));
        l.setFk_sesion(Integer.parseInt(ll[4]));
        l.setCreated_at(ll[5]);
        l.setUpdated_at(ll[6]);
        LecturaTipoDTO tipoContenido = ConvertersLecturaTipo.fromString(ll[7]);
        l.setTipo_contenido(tipoContenido);
        return l;
    }

    @TypeConverter
    public static String toString(BDLecturaContenidoDTO user) {
        String l=user.getId()+"::"+user.getContenido()+"::"+user.getPosicion()
                +"::"+user.getFk_tipo()+"::"+user.getFk_sesion()+"::"+user.getCreated_at()
                +"::"+user.getUpdated_at()+"::"+ConvertersLecturaTipo.toString(user.getTipo_contenido());
        return l;
    }*/
}
