package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class ProductoSend {
    private String id;
    private int cantidad;

    public ProductoSend(String id, int cantidad) {
        this.id = id;
        this.cantidad = cantidad;
    }

    public String getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }
}
