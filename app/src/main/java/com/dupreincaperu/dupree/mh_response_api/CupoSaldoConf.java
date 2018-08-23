package com.dupreincaperu.dupree.mh_response_api;

/**
 * Created by cloudemotion on 21/8/17.
 */

public class CupoSaldoConf {
    private String cupo;
    private String saldo;
    private String conf_vent;
    private String asesora;

    public CupoSaldoConf(String cupo, String saldo, String conf_vent, String asesora) {
        this.cupo = cupo;
        this.saldo = saldo;
        this.conf_vent = conf_vent;
        this.asesora = asesora;
    }

    public String getCupo() {
        return cupo;
    }

    public String getSaldo() {
        return saldo;
    }

    public String getConf_vent() {
        return conf_vent;
    }

    public String getAsesora() {
        return asesora;
    }
}
