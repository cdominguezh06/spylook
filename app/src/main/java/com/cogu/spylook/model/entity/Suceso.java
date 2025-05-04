package com.cogu.spylook.model.entity;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Suceso {
    private int id;
    private LocalDate fecha;
    private String descripcion;
    private Contacto causante;
}
