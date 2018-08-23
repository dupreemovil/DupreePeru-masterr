package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 14/8/17.
 */

public class SendPremios {
    private String codigo;
    private String cantidad;

    public SendPremios(String codigo, String cantidad) {
        this.codigo = codigo;
        this.cantidad = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
