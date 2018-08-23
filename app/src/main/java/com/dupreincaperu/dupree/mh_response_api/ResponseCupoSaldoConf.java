package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 21/8/17.
 */

public class ResponseCupoSaldoConf {
    private String status;
    private boolean valid;

    @SerializedName("result")
    private List<CupoSaldoConf> cupoSaldoConfList;
    private int code;

    private List<Raise> raise;

    public ResponseCupoSaldoConf(String status, boolean valid, List<CupoSaldoConf> cupoSaldoConfList, int code) {
        this.status = status;
        this.valid = valid;
        this.cupoSaldoConfList = cupoSaldoConfList;
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public boolean isValid() {
        return valid;
    }

    public List<CupoSaldoConf> getCupoSaldoConfList() {
        return cupoSaldoConfList;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
