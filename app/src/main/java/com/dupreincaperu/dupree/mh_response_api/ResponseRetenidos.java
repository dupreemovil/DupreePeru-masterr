package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 30/8/17.
 */

public class ResponseRetenidos {
    private String status;
    private Boolean valid;
    @SerializedName("result")
    private listRetenidos listTitleRetenidos;
    private int code;

    private List<Raise> raise;

    public String getStatus() {
        return status;
    }

    public Boolean getValid() {
        return valid;
    }

    public listRetenidos getListTitleRetenidos() {
        return listTitleRetenidos;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }

    public class listRetenidos{
        @SerializedName("title_1")
        private String area;
        @SerializedName("title_2")
        private String zona;
        @SerializedName("title_3")
        private String cedula;
        @SerializedName("title_4")
        private String name;
        @SerializedName("title_5")
        private String celular;
        @SerializedName("title_6")
        private String numPedido;
        @SerializedName("title_7")
        private String neto;
        @SerializedName("title_8")
        private String saldo;
        @SerializedName("title_9")
        private String cupo;
        @SerializedName("title_10")
        private String cartera;
        @SerializedName("title_11")
        private String r_cupo;
        @SerializedName("title_12")
        private String code;
        @SerializedName("title_13")
        private String montoMinimo;
        @SerializedName("title_14")
        private String bloqueo;
        @SerializedName("title_15")
        private String minimoPublico;
        @SerializedName("title_16")
        private String totalPublico;
        @SerializedName("table")
        private List<Retenido> retenidoList;

        public List<Retenido> getRetenidoList() {
            return retenidoList;
        }
    }
}
