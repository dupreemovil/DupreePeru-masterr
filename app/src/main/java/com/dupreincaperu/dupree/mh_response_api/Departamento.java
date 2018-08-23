package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 25/8/17.
 */

public class Departamento {
    private String id_dpto;
    private String name_dpto;
    private List<Ciudad> ciudades;

    public String getId_dpto() {
        return id_dpto;
    }

    public String getName_dpto() {
        return name_dpto;
    }

    public List<Ciudad> getCiudades() {
        return ciudades;
    }
}
