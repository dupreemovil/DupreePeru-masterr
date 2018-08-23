package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 1/9/17.
 */

public class ListPremios {
    private List<Premios> premios;
    private int puntos_premio;

    public List<Premios> getPremios() {
        return premios;
    }

    public int getPuntos_premio() {
        return puntos_premio;
    }

    public void setPuntos_premio(int puntos_premio) {
        this.puntos_premio = puntos_premio;
    }
}
