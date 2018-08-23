package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 27/8/17.
 */

public class ResponseAuth {
    private String status;
    private boolean valid;
    private String result;
    private List<Perfil> perfil;
    private int code;
    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public List<Raise> getRaise() {
        return raise;
    }

    public boolean isValid() {
        return valid;
    }

    public String getResult() {
        return result;
    }

    public List<Perfil> getPerfil() {
        return perfil;
    }

    public int getCode() {
        return code;
    }
}
