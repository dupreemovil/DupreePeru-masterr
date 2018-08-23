package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class ResponsePanelGte {
    public String status;
    public Boolean valid;
    @SerializedName("result")
    public PanelGteListDetail listDetail;
    public int code;
    public List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public Boolean getValid() {
        return valid;
    }

    public PanelGteListDetail getListDetail() {
        return listDetail;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
