package com.example.atanke.general.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idError;
    private String descError;
    private String tipoError;
}