package com.example.atanke.lectura.models;

import com.example.atanke.general.dto.api.sesiones.BDLecturaSesionDTO;
import com.example.atanke.registrarusuario.models.ErrorRegistroDTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturaSesionResponse {
    private BigDecimal id;
    private String mensaje;
    private Integer datos_len;
    private List<BDLecturaSesionDTO> datos;
    private ErrorRegistroDTO errores;
}
