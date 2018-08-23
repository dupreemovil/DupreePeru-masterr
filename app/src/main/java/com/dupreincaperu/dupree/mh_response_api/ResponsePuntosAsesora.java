package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 2/9/17.
 */

public class ResponsePuntosAsesora {
    private String status;
    private boolean valid;
    @SerializedName("result")
    private ListaPuntos result;
    private int code;
    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public ListaPuntos getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
