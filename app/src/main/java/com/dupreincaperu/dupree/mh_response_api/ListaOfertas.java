package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 12/9/17.
 */

public class ListaOfertas {
    private List<Oferta> productos;
    private String activo;

    public List<Oferta> getProductos() {
        return productos;
    }

    public String getActivo() {
        return activo;
    }
}
