package com.example.atanke.perfilusuario.models;

import com.example.atanke.general.dto.DataHeaderDTO;
import com.example.atanke.general.dto.UsuarioDTO;
import com.example.atanke.registrarusuario.models.ErrorRegistroDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuarioResponse {
    private BigDecimal id;
    private String mensaje;
    private Integer datos_len;
    private UsuarioDTO datos;
    private ErrorRegistroDTO errores;
}
