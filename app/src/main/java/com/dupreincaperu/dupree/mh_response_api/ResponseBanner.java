package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 25/8/17.
 */

public class ResponseBanner {
    private List<ImgBanner> banner;
    private List<TipoVia> tipo_via;
    private List<Departamento> departamentos;
    private String img_catalogo;
    private String url_chat;
    private String version;

    public List<ImgBanner> getBanner() {
        return banner;
    }

    public List<TipoVia> getTipo_via() {
        return tipo_via;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public String getImg_catalogo() {
        return img_catalogo;
    }

    public String getUrl_chat() {
        return url_chat;
    }

    public String getVersion() {
        return version;
    }
}
