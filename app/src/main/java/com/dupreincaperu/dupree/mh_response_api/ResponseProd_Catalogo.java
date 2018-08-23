package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 4/9/17.
 */

public class ResponseProd_Catalogo {
    private String status;
    private boolean valid;
    private ListaProd_Catalogo result;
    private int code;

    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public ListaProd_Catalogo getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
