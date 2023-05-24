package com.example.atanke.general.dto.api.lecturas;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturaTipoDTO implements Serializable {
    private int id;
    private String nombre;
}
