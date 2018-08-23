package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 3/9/17.
 */

public class DetalleProductos {
    private String nombre;
    private String id;
    private int cantidad;
    private String precio;
    private String page;

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public String getPage() {
        return page;
    }
}
