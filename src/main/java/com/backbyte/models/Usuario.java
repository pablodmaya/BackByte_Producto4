package com.backbyte.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_Usuario;

    @Column(name = "nombre_Usuario", unique = true, nullable = false)
    private String nombreUsuario;

    // Contraseña en texto claro (sin encriptar), necesaria para el registro
    @Column(name = "password", nullable = false)
    private String password;  // Este campo es el que recibes del formulario (sin encriptar)

    // Contraseña encriptada, que es la que utilizas para la autenticación
    @Column(name = "password_Encriptada", nullable = false)
    private String passwordEncriptada;  // Este es el campo que se usa para la autenticación

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo_Usuario;

    @Column(name = "es_cliente")
    private Boolean es_Cliente;

    public Usuario( String nombreUsuario, String password, String passwordEncriptada, String email, TipoUsuario tipo_Usuario, Boolean es_Cliente) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.email = email;
        this.tipo_Usuario = tipo_Usuario;
        this.es_Cliente = es_Cliente;
    }

    public Usuario() {

    }

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + tipo_Usuario.name());
    }

    @Override
    public String getPassword() {
        return passwordEncriptada;  // Se debe devolver la contraseña encriptada para la autenticación
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public Boolean getEs_Cliente() {
        return es_Cliente;
        }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    public void setEs_Cliente(Boolean es_Cliente) {
        this.es_Cliente = es_Cliente;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Integer getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(Integer id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordEncriptada() {
        return passwordEncriptada;
    }

    public void setPasswordEncriptada(String passwordEncriptada) {
        this.passwordEncriptada = passwordEncriptada;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoUsuario getTipo_Usuario() {
        return tipo_Usuario;
    }

    public void setTipo_Usuario(TipoUsuario tipo_Usuario) {
        this.tipo_Usuario = tipo_Usuario;
    }

}
