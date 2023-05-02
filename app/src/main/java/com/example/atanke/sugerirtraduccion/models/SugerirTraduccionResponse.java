package com.example.atanke.sugerirtraduccion.models;

import com.example.atanke.general.dto.DataHeaderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SugerirTraduccionResponse {
    private DataHeaderDTO dataHeader;
    private String mensaje;
}
