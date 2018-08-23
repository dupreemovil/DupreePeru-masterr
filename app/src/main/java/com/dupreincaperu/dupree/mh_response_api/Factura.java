package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 21/8/17.
 */

public class Factura {
    private String campana;
    private String factura;
    private String fecha;
    private String link;
    private int fila;

    public Factura(String campana, String factura, String fecha, String link, int fila) {
        this.campana = campana;
        this.factura = factura;
        this.fecha = fecha;
        this.link = link;
        this.fila = fila;
    }

    public String getCampana() {
        return campana;
    }

    public String getFactura() {
        return factura;
    }

    public String getFecha() {
        return fecha;
    }

    public String getLink() {
        return link;
    }

    public int getFila() {
        return fila;
    }
}
