package com.dupreincaperu.dupree.mh_interface_api;

import com.dupreincaperu.dupree.mh_required_api.RequiredInscription_NEW;
import com.dupreincaperu.dupree.mh_response_api.CatalogoPremiosList;
import com.dupreincaperu.dupree.mh_response_api.ResponseBandeja;
import com.dupreincaperu.dupree.mh_response_api.ResponseCDR;
import com.dupreincaperu.dupree.mh_response_api.ResponseCampana;
import com.dupreincaperu.dupree.mh_response_api.ResponseCupoSaldoConf;
import com.dupreincaperu.dupree.mh_response_api.ResponseEstadoPedido;
import com.dupreincaperu.dupree.mh_response_api.ResponseFactura;
import com.dupreincaperu.dupree.mh_response_api.ResponseFaltantes;
import com.dupreincaperu.dupree.mh_response_api.ResponseLiquida;
import com.dupreincaperu.dupree.mh_response_api.ResponseListPreinscripcion;
import com.dupreincaperu.dupree.mh_response_api.ResponsePQR;
import com.dupreincaperu.dupree.mh_response_api.ResponsePagos;
import com.dupreincaperu.dupree.mh_response_api.ResponsePanelGte;
import com.dupreincaperu.dupree.mh_response_api.ResponsePerfil;
import com.dupreincaperu.dupree.mh_response_api.ResponseProd_Catalogo;
import com.dupreincaperu.dupree.mh_response_api.ResponsePuntosAsesora;
import com.dupreincaperu.dupree.mh_response_api.ResponseRedimir;
import com.dupreincaperu.dupree.mh_response_api.ResponseReferidos;
import com.dupreincaperu.dupree.mh_response_api.ResponseRetenidos;
import com.dupreincaperu.dupree.mh_response_api.ResponseAuth;
import com.dupreincaperu.dupree.mh_response_api.ResponseGeneric;
import com.dupreincaperu.dupree.mh_response_api.ResponsePanelAsesora;
import com.dupreincaperu.dupree.mh_response_api.ResponseUrlCatalogos;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by cloudemotion on 30/8/17.
 */

public interface iReport {
    @GET("reportes/campanas")
    Call<ResponseCampana> obtainCampanas();

    @GET("panel/catalogos")
    Call<ResponseUrlCatalogos> obtainUrlCatalogos();

    @GET("reportes/panel_gerente")
    Call<ResponsePanelGte> obtainPanelGrte(@Query("filters") String jsonCampana);

    @FormUrlEncoded
    @POST("reportes/datos_perfil")
    Call<ResponsePerfil> getPerfilUser(@Field("Params") String jsonCedula);//el parametro filter no va, solo que
    //la libreria no permite parametros vacios en POST

    @FormUrlEncoded
    @POST("panel/modifica_perfil")
    Call<ResponseAuth> EditPerfil(@Field("Params") String jsonPerfil);

    @GET("reportes/pase_pedidos")
    Call<ResponseRetenidos> obtainPedRetenidos(@Query("filters") String jsonCedula);

    @GET("reportes/cupo_saldo")
    Call<ResponseCupoSaldoConf> obtainCupoSaldoConf(@Query("filters") String jsonCedula);

    @GET("panel/lista_preinscripcion")
    Call<ResponseListPreinscripcion> obtainListPre(
            @Query("perfil") String perfil,
            @Query("index_pages") int index_pages,
            @Query("valor") String valor,
            @Query("token") String token
    );

    @FormUrlEncoded
    @POST("reportes/nombre")
    Call<ResponseGeneric> validateReferido(@Field("Params") String jsonValidaRef);

    @FormUrlEncoded
    @POST("reportes/cifin")
    Call<ResponseGeneric> validateCentralRiesgo(@Field("Params") String jsonValidaRef);

    @FormUrlEncoded
    @POST("panel/inscripcion")
    Call<ResponseGeneric> inscribir(@Field("Params") String jsonInscription);

    @FormUrlEncoded
    @POST("panel/actualiza_pre")
    Call<RequiredInscription_NEW> editInscription(@Field("Params") String jsonCedula);

    @GET("reportes/panel_asesora")
    Call<ResponsePanelAsesora> obtainPanelAsesora();

    @GET("pedidos/faltante")
    Call<ResponseFaltantes> obtainFaltantes();

    @GET("pedidos/premios")
    Call<ResponseRedimir> obtainRedimirIncentivos(
            @Query("filters") String jsonCedula, @Query("paginator") String pag
    );

    @GET("reportes/puntos")
    Call<ResponsePuntosAsesora> obtainPuntosAsesora(@Query("filters") String jsonCedula);

    @FormUrlEncoded
    @POST("pedidos/redime_premios")
    Call<ResponseGeneric> redimirPremios(@Field("Params") String jsonRedimir);

    @GET("reportes/referido")
    Call<ResponseReferidos> obtainIncentivosReferido(
            @Query("filters") String jsonCedula, @Query("paginator") String pag
    );

    @GET("reportes/cdr")
    Call<ResponseCDR> obtainCDR(
            @Query("filters") String jsonCedula, @Query("paginator") String pag
    );

    @GET("reportes/pqr")
    Call<ResponsePQR> obtainPQR(
            @Query("filters") String jsonCedula, @Query("paginator") String pag
    );

    @GET("reportes/pdf_factura")
    Call<ResponseFactura> obtainFacturas(@Query("filters") String jsonCedula);

    @GET("reportes/pagos")
    Call<ResponsePagos> obtainPagos(@Query("filters") String jsonCedula);

    //////////////PEDIDO
    @GET("pedidos/estado")
    Call<ResponseEstadoPedido> obtainEstadoPedido(@Query("filters") String jsonCedula);

    @FormUrlEncoded
    @POST("pedidos/confirma")
    Call<ResponseGeneric> confirmarPedido(@Field("Params") String jsonConfimPed);

    @FormUrlEncoded
    @POST("pedidos/liquida")
    Call<ResponseLiquida> liquidarPedido(@Field("Params") String jsonLiquidate);

    @GET("pedidos/productos")
    Call<ResponseProd_Catalogo> obtainProductos();

    @FormUrlEncoded
    @POST("panel/estado_prelider")
    Call<ResponseGeneric> aprobarPreinscripcion(@Field("Params") String jsonPerfil);

    //@FormUrlEncoded
    @POST("mensajes/consulta")
    Call<ResponseBandeja> obtainMessages();

    @FormUrlEncoded
    @POST("mensajes/actualiza")
    Call<ResponseGeneric> readMessages(@Field("Params") String jsonUsuario);

    @FormUrlEncoded
    @POST("mensajes/borrar")
    Call<ResponseGeneric> deleteMessages(@Field("Params") String jsonUsuario);

    @GET("panel/folletos")
    Call<CatalogoPremiosList> obtainCatalogoPremios();
}
