package com.backbyte.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Alquiler {
    public Integer getId_Alquiler() {
        return id_Alquiler;
    }

    public void setId_Alquiler(Integer id_Alquiler) {
        this.id_Alquiler = id_Alquiler;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFecha_Inicio() {
        return fecha_Inicio;
    }

    public void setFecha_Inicio(Date fecha_Inicio) {
        this.fecha_Inicio = fecha_Inicio;
    }

    public Date getFecha_Fin() {
        return fecha_Fin;
    }

    public void setFecha_Fin(Date fecha_Fin) {
        this.fecha_Fin = fecha_Fin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Alquiler;

    @ManyToOne
    @JoinColumn(name = "id_Vehiculo")  // Establece la relación con Vehiculo
    private Vehiculo vehiculo;  // Relación con la clase Vehiculo

    @ManyToOne
    @JoinColumn(name = "id_Cliente")  // Relación con la clase Cliente
    private Cliente cliente;  // Relación con la clase Cliente

    @Column(name = "fecha_inicio")
    private Date fecha_Inicio;

    @Column(name = "fecha_fin")
    private Date fecha_Fin;

    @Transient // Si no quieres persistir este campo en la base de datos
    private Double precioTotal;


}
