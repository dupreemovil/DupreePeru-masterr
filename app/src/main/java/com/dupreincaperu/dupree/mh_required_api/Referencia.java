package com.dupreincaperu.dupree.mh_required_api;

/**
 * Created by cloudemotion on 1/9/17.
 */

public class Referencia {
    private String tipo_documento_id;
    private String cedula;
    private String nombre;//
    private String apellido;//
    private String celular;//
    private String telefono;//
    private String parentesco;//

    public Referencia(String tipo_documento_id, String cedula, String nombre, String apellido, String celular, String telefono, String parentesco) {
        this.tipo_documento_id = tipo_documento_id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.telefono = telefono;
        this.parentesco = parentesco;
    }

    public Referencia() {
    }

    public void setTipo_documento_id(String tipo_documento_id) {
        this.tipo_documento_id = tipo_documento_id;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTipo_documento_id() {
        return tipo_documento_id;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCelular() {
        return celular;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getParentesco() {
        return parentesco;
    }
}
