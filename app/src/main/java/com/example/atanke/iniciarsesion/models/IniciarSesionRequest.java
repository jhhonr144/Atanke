package com.example.atanke.iniciarsesion.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IniciarSesionRequest {
    private String email;
    private String password;
}
