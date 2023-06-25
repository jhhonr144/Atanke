package com.example.atanke.palabras.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class addFotoRequest {
    private String contenido;
    private String id;
    private String nombre;
}
