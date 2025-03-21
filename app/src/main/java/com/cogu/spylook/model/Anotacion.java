package com.cogu.spylook.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Anotacion {
    private int id;
    private LocalDate fecha;
    private String descripcion;
    private Contacto contacto;
}
