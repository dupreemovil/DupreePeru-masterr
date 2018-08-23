package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class PanelGteListDetail {
    @SerializedName("table")
    private List<PanelGteDetail> panelGteDetails;
    private int campana;
    private String cantidad_mensajes;

    public List<PanelGteDetail> getPanelGteDetails() {
        return panelGteDetails;
    }

    public int getCampana() {
        return campana;
    }

    public String getCantidad_mensajes() {
        return cantidad_mensajes;
    }

    public void setCantidad_mensajes(String cantidad_mensajes) {
        this.cantidad_mensajes = cantidad_mensajes;
    }
}
