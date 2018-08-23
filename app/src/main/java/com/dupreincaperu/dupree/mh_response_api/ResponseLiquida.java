package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 12/9/17.
 */

public class ResponseLiquida {
    private String status;
    private boolean valid;
    private String result;
    private String mensaje;
    private String total_pedido;
    private String codigo;
    private int code;

    private List<Raise> raise;


    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public String getResult() {
        return result;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getTotal_pedido() {
        return total_pedido;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
