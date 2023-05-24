package com.example.atanke.general.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValidarFechas {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  boolean hanPasado7Dias(String fechaString) {
        LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaHace7Dias = fechaActual.minusDays(7);
        return fecha.isBefore(fechaHace7Dias.atStartOfDay());
    }

}