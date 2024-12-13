package com.backbyte.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Vehiculo;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "precio_Dia")
    private Double precioDia;

    @Enumerated(EnumType.STRING)  // Para que se almacene como un String en la base de datos
    @Column(name = "localizacion")
    private Ciudad localizacion;

    @Enumerated(EnumType.STRING)  // Para guardar el tipo como un String ("COCHE", "MOTO")
    @Column(name = "tipo_Vehiculo")
    private TipoVehiculo tipoVehiculo;

    public Integer getId_Vehiculo() {
        return id_Vehiculo;
    }

    public void setId_Vehiculo(Integer id_Vehiculo) {
        this.id_Vehiculo = id_Vehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(Double precioDia) {
        this.precioDia = precioDia;
    }

    public Ciudad getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Ciudad localizacion) {
        this.localizacion = localizacion;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
}
