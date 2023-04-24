package com.example.atanke.sugerirtraduccion.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SugerirTraduccionRequest {
    private int codIdioma;
    private String palabra;
    private String traduccion;
    private String pronunciacion;
    private int  fkUser;
}
