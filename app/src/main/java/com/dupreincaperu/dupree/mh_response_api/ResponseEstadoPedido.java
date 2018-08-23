package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 3/9/17.
 */

public class ResponseEstadoPedido {
    public static final String SHOW_PRDUCTS="1";

    private String status;
    private boolean valid;
    private ResultEdoPedido result;
    private int code;


    private List<Raise> raise;


    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public ResultEdoPedido getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
