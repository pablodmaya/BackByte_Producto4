package com.backbyte.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Coche extends Vehiculo {

    private Integer plazas;
    private String color;
    private Integer puertas;

    public Integer getPlazas() {
        return plazas;
    }

    public void setPlazas(Integer plazas) {
        this.plazas = plazas;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPuertas() {
        return puertas;
    }

    public void setPuertas(Integer puertas) {
        this.puertas = puertas;
    }
}
