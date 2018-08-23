package com.dupreincaperu.dupree.mh_required_api;

/**
 * Created by cloudemotion on 14/9/17.
 */


public class RequiredApprovePreIns {
    public static final String APROBAR = "APR";
    public static final String RECHAZAR = "ANL";

    private String cedula;
    private String estado;

    public RequiredApprovePreIns(String cedula, String estado) {
        this.cedula = cedula;
        this.estado = estado;
    }
}