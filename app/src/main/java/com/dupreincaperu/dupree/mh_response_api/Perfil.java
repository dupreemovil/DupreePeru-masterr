package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 27/8/17.
 */

public class Perfil {
    public static final String ADESORA ="A";
    public static final String LIDER = "L";
    public static final String GERENTE_ZONA = "Z";
    public static final String GERENTE_REGION = "R";

    private String perfil;
    private String valor;
    private boolean catalogo;
    private String imagen_perfil;

    public String getPerfil() {
        return perfil;
    }

    public String getValor() {
        return valor;
    }

    public boolean isCatalogo() {
        return catalogo;
    }

    public String getImagen_perfil() {
        return imagen_perfil;
    }

    public void setImagen_perfil(String imagen_perfil) {
        this.imagen_perfil = imagen_perfil;
    }
}
