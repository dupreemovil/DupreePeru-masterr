package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 3/9/17.
 */

public class ListaProductos {
    private List<DetalleProductos> productos;
    private String precio_total;

    public List<DetalleProductos> getProductos() {
        return productos;
    }

    public String getPrecio_total() {
        return precio_total;
    }
}
