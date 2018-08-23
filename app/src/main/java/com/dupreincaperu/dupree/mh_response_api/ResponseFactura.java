package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 2/9/17.
 */

public class ResponseFactura {
    private String status;
    private boolean valid;
    private List<Factura> result;
    private String asesora;
    private int code;

    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Factura> getResult() {
        return result;
    }

    public String getAsesora() {
        return asesora;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
