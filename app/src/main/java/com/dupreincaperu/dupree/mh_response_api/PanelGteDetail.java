package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 13/8/17.
 */

public class PanelGteDetail {
    private String campana;
    private String cantidad;
    private String porcentaje;
    private String status;



    public PanelGteDetail(String campana, String cantidad, String porcentaje, String status) {
        this.campana = campana;
        this.cantidad = cantidad;
        this.porcentaje = porcentaje;
        this.status = status;
    }

    public String getCampana() {
        return campana;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public String getStatus() {
        return status;
    }

}
