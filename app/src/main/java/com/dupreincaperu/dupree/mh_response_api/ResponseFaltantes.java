package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 20/8/17.
 */

public class ResponseFaltantes {
    private String status;
    private boolean valid;
    private List<Faltante> result;
    private String campana;
    private int code;
    private List<Raise> raise;

    public ResponseFaltantes(String status, boolean valid, List<Faltante> result, String campana, int code) {
        this.status = status;
        this.valid = valid;
        this.result = result;
        this.campana = campana;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Faltante> getResult() {
        return result;
    }

    public String getCampana() {
        return campana;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
