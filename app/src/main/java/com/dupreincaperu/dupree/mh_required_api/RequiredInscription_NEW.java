package com.dupreincaperu.dupree.mh_required_api;

import com.dupreincaperu.dupree.mh_response_api.Raise;

import java.util.List;

/**
 * Created by cloudemotion on 1/9/17.
 */

public class RequiredInscription_NEW {
    private long cedula;//

    private String nacimiento;//
    private String zona_seccion;//
    private String tipo_via;//
    private String numero1;//
    private String letra1;//
    private String bis;//
    private String numero2;//
    private String letra2;//
    private String numero3;//
    private String pcardinal;//
    private String complemento;//
    private String direccion_concatenada;//

    private String departamento;//
    private String id_ciudad;//
    private String name_ciudad;//
    private String barrio;//
    private String id_barrio;//

    private String direccion_envio;//
    private String id_barrio_envio;//

    private String telefono;//
    private String celular;//
    private String correo;//
    private String imei;

    private String referenciado_por;//
    private List<String> img_cedula;//
    private List<String> pagare;
    private List<Referencia> referencia;//

    //solo para edicion de inscrip rechazada
    private String referenciado_nombre;//

    public RequiredInscription_NEW(long cedula, String departamento, String zona_seccion, String id_ciudad,
                            String name_ciudad, String barrio, String id_barrio, String telefono,
                            String celular, String correo, String nacimiento, String imei, String tipo_via,
                            String numero1, String letra1, String bis, String numero2, String letra2,
                            String numero3, String pcardinal, String complemento, String direccion_concatenada,
                            String direccion_envio, String id_barrio_envio) {

        this.cedula = cedula;

        this.departamento = departamento;
        this.id_ciudad = id_ciudad;
        this.name_ciudad = name_ciudad;
        this.barrio = barrio;
        this.id_barrio = id_barrio;
        this.telefono = telefono;
        this.celular = celular;
        this.correo = correo;
        this.nacimiento = nacimiento;
        this.zona_seccion = zona_seccion;
        this.imei = imei;
        this.tipo_via = tipo_via;
        this.numero1 = numero1;
        this.letra1 = letra1;
        this.bis = bis;
        this.numero2 = numero2;
        this.letra2 = letra2;
        this.numero3 = numero3;
        this.pcardinal = pcardinal;
        this.complemento = complemento;
        this.direccion_concatenada = direccion_concatenada;
        this.direccion_envio = direccion_envio;
        this.id_barrio_envio = id_barrio_envio;
    }


    public void setDireccion_concatenada(String direccion_concatenada) {
        this.direccion_concatenada = direccion_concatenada;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public void setImg_cedula(List<String> img_cedula) {
        this.img_cedula = img_cedula;
    }

    public void setPagare(List<String> pagare) {
        this.pagare = pagare;
    }

    public void setReferenciado_por(String referenciado_por) {
        this.referenciado_por = referenciado_por;
    }

    public void setReferencia(List<Referencia> referencia) {
        this.referencia = referencia;
    }

    public long getCedula() {
        return cedula;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getZona_seccion() {
        return zona_seccion;
    }

    public String getTipo_via() {
        return tipo_via;
    }

    public String getNumero1() {
        return numero1;
    }

    public String getLetra1() {
        return letra1;
    }

    public String getBis() {
        return bis;
    }

    public String getNumero2() {
        return numero2;
    }

    public String getLetra2() {
        return letra2;
    }

    public String getNumero3() {
        return numero3;
    }

    public String getPcardinal() {
        return pcardinal;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getDireccion_concatenada() {
        return direccion_concatenada;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getId_ciudad() {
        return id_ciudad;
    }

    public String getName_ciudad() {
        return name_ciudad;
    }

    public String getBarrio() {
        return barrio;
    }

    public String getId_barrio() {
        return id_barrio;
    }

    public String getDireccion_envio() {
        return direccion_envio;
    }

    public String getId_barrio_envio() {
        return id_barrio_envio;
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

    public String getImei() {
        return imei;
    }

    public String getReferenciado_por() {
        return referenciado_por;
    }

    public List<String> getImg_cedula() {
        return img_cedula;
    }

    public List<String> getPagare() {
        return pagare;
    }

    public List<Referencia> getReferencia() {
        return referencia;
    }

    public String getReferenciado_nombre() {
        return referenciado_nombre;
    }

    int code;
    List<Raise> raise;

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
