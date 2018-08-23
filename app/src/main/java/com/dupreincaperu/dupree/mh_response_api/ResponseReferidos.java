package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 2/9/17.
 */

public class ResponseReferidos {
    private String status;
    private boolean valid;
    private ListaReferidos result;
    private int code;

    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public ListaReferidos getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
