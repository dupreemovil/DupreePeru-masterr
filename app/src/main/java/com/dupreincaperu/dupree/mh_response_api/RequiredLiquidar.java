package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class RequiredLiquidar {

    private String id_pedido;
    private List<String> paquetones;
    private List<ProductoSend> productos;
    private List<ProductoSend> ofertas;

    public RequiredLiquidar(String id_pedido, List<String> paquetones, List<ProductoSend> productos, List<ProductoSend> ofertas) {
        this.id_pedido = id_pedido;
        this.paquetones = paquetones;
        this.productos = productos;
        this.ofertas = ofertas;
    }

    public void setId_pedido(String id_pedido) {
        this.id_pedido = id_pedido;
    }

    public void setPaquetones(List<String> paquetones) {
        this.paquetones = paquetones;
    }

    public void setProductos(List<ProductoSend> productos) {
        this.productos = productos;
    }

    public void setOfertas(List<ProductoSend> ofertas) {
        this.ofertas = ofertas;
    }
}
