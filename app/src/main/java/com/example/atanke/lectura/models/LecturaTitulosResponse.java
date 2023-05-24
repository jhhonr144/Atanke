package com.example.atanke.lectura.models;

import com.example.atanke.general.dto.api.lecturas.BDLecturaDTO;
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
public class LecturaTitulosResponse {
    private BigDecimal id;
    private String mensaje;
    private Integer datos_len;
    private List<BDLecturaDTO> datos;
    private ErrorRegistroDTO errores;
}
