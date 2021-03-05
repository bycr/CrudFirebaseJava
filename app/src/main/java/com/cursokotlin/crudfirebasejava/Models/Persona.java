package com.cursokotlin.crudfirebasejava.Models;

//paso 2 luego de la interfaz grafica
public class Persona {
    //2.1 declaramos variables del modelo
    private String idpersona;
    private String nombres;
    private String telefono;

    //2.2 implemetamos los setters , getters y tostring
    public String getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(String idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return nombres;
    }
}
