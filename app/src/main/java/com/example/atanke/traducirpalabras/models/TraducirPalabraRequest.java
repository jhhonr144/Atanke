package com.example.atanke.traducirpalabras.models;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraducirPalabraRequest {
    @SerializedName("data")
    @Getter @Setter
    private String data;
}
