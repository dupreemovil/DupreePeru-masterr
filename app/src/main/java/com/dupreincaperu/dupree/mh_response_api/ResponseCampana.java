package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class ResponseCampana {
    private String status;
    private boolean valid;
    @SerializedName("result")
    private List<Campana> campanaList;
    private int code;
    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<Campana> getCampanaList() {
        return campanaList;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
