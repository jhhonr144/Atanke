package com.example.atanke.registrarusuario.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRegistroDTO {
    private List<String> name;
    private List<String> email;
    private List<String> password;

}
