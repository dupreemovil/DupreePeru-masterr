package com.dupreincaperu.dupree.mh_interface_api;

import com.dupreincaperu.dupree.mh_response_api.ResponseBarrio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by cloudemotion on 26/8/17.
 */

public interface iBarrios {
    @GET("panel/barrios/")
    Call<ResponseBarrio> obtainBarrios(@Query("id_ciudad") String id_ciudad);
}
