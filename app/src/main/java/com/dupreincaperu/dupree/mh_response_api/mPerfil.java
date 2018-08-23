package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class mPerfil {
    private String nombre;
    private String apellido;
    private String telefono;
    private String celular;
    private String correo;

    public mPerfil(String nombre, String apellido, String telefono, String celular, String correo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCelular() {
        return celular;
    }

    public String getCorreo() {
        return correo;
    }
}
