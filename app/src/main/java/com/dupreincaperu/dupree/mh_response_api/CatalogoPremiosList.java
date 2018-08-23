package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class CatalogoPremiosList {
    private List<Folleto> folleto;

    public List<Folleto> getFolleto() {
        return folleto;
    }

    public class Folleto {
        private String code;//se usa en la db
        private String name;
        private String image;
        private String url;
        private String pdf;


        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getUrl() {
            return url;
        }

        public String getPdf() {
            return pdf;
        }
    }
}
