package com.example.atanke.palabras.models;

import com.example.atanke.general.dto.api.palabras.BDPalabraDTO;
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
public class PalabrasAddFotoResponse {
    private int id;
    private String mensaje;
}
