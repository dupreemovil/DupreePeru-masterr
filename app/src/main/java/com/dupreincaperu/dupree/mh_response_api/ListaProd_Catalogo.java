package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class ListaProd_Catalogo {
    private List<Catalogo> productos;
    private PaquetonesByCategory paquetones;
    @SerializedName("Ofertas")
    private List<Oferta> ofertas;
    private int estado_paqueton;
    private int estado_ofertas;

    public List<Catalogo> getProductos() {
        return productos;
    }

    public PaquetonesByCategory getPaquetones() {
        return paquetones;
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public int getEstado_paqueton() {
        return estado_paqueton;
    }

    public int getEstado_ofertas() {
        return estado_ofertas;
    }
}
