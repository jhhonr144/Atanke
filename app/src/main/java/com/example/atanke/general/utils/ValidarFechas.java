package com.example.atanke.general.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class ValidarFechas {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  boolean hanPasado7Dias(String fechaString) {
        LocalDateTime fecha = LocalDateTime.parse(fechaString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaHace7Dias = fechaActual.minusDays(7);
        return fecha.isBefore(fechaHace7Dias.atStartOfDay());
    }

    public static String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(calendar.getTime());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertDateTime(String inputDate) {
        Instant instant = Instant.parse(inputDate);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

}
