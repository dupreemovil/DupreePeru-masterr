package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class ResponsePerfil {
    private String status;
    private Boolean valid;
    @SerializedName("result")
    private List<mPerfil> perfilList;
    private int code;

    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public Boolean getValid() {
        return valid;
    }

    public List<mPerfil> getPerfilList() {
        return perfilList;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
