package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 14/9/17.
 */

public class ResponseUrlCatalogos {
    private List<ListaCatalogo> catalogos;

    public List<ListaCatalogo> getCatalogos() {
        return catalogos;
    }

    public class ListaCatalogo{
        private UrlCatalogo catalogo1;
        private UrlCatalogo catalogo2;
        private UrlCatalogo catalogo3;

        public UrlCatalogo getCatalogo1() {
            return catalogo1;
        }

        public UrlCatalogo getCatalogo2() {
            return catalogo2;
        }

        public UrlCatalogo getCatalogo3() {
            return catalogo3;
        }
    }
}
