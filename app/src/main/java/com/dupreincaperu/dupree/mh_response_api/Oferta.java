package com.dupreincaperu.dupree.mh_response_api;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class Oferta extends RealmObject{
    @PrimaryKey
    private String id;
    private String name;
    private String valor;
    private String valor_descuento;
    private String url_img;
    private String page;
    private int cantidad;
    //control

    private int cantidadServer=0;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValor() {
        return valor;
    }

    public String getValor_descuento() {
        return valor_descuento;
    }

    public String getUrl_img() {
        return url_img;
    }

    public String getPage() {
        return page;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadServer() {
        return cantidadServer;
    }

    public void setCantidadServer(int cantidadServer) {
        this.cantidadServer = cantidadServer;
    }
}
