package com.example.atanke.traducirpalabras.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraducirPalabraRequest {
    private String data;
    private String fk_idioma;
}
