package com.springboot.MyTodoList.model;

import javax.persistence.*;



@Entity
@Table(name = "usuarios") 
public class ToDoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_user")
    private int id;

    @Column(name = "ID_equipo")
    private int idEquipo;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Num_tel")
    private String numeroTelefono;

    @Column(name = "Correo")
    private String correo;

    @Column(name = "Rol")
    private String rol;

    @Column(name = "Verificado")
    private Boolean verificado;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }
}
