package com.dupreincaperu.dupree.mh_interface_api;

import android.util.Log;

import com.dupreincaperu.dupree.mh_response_api.ResponseAuth;
import com.dupreincaperu.dupree.mh_response_api.ResponseGeneric;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by cloudemotion on 27/8/17.
 */

public interface iAuth {
    //login/authenticate/
    @FormUrlEncoded
    @POST("login/authenticate")
    Call<ResponseAuth> auth(@Field("Params") String dataAuth);


    @FormUrlEncoded
    @POST("panel/olvido_contrasena")
    Call<ResponseGeneric> notifyForgot(@Field("Params") String identy);

    @FormUrlEncoded
    @POST("panel/valida_codigo")
    Call<ResponseGeneric> validateCode(@Field("Params") String codigo);

    @FormUrlEncoded
    @POST("panel/valida_contrasena")
    Call<ResponseGeneric> validatePassword(@Field("Params") String pwd);

    @FormUrlEncoded
    @POST("panel/pre_inscripcion")
    Call<ResponseGeneric> preinscripcion(@Field("Params") String preinscripcion);

    @FormUrlEncoded
    @POST("panel/vuelvete_asesora")
    Call<ResponseGeneric> vuelveteAsesora(@Field("Params") String preinscripcion);


    @FormUrlEncoded
    @POST("panel/terminos")
    Call<ResponseGeneric> termins(@Field("Params") String termins);

    @FormUrlEncoded
    @POST("panel/terminos_gerente")
    Call<ResponseGeneric> terminsGerente(@Field("Params") String termins);

    @FormUrlEncoded
    @POST("panel/firebase")
    Call<ResponseGeneric> refreshToken(@Field("Params") String firebase);
}
