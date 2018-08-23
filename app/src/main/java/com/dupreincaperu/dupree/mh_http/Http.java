package com.dupreincaperu.dupree.mh_http;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.dupreincaperu.dupree.FullscreenActivity;
import com.dupreincaperu.dupree.MainActivity;
import com.dupreincaperu.dupree.MenuActivity;
import com.dupreincaperu.dupree.R;

import com.dupreincaperu.dupree.mh_required_api.RequiredApprovePreIns;
import com.dupreincaperu.dupree.mh_required_api.RequiredConfirmaPedido;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdMessaage;
import com.dupreincaperu.dupree.mh_required_api.RequiredIndex;
import com.dupreincaperu.dupree.mh_required_api.RequiredInscription_NEW;
import com.dupreincaperu.dupree.mh_required_api.RequiredRefreshToken;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister_NEW_2018;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister_NewAPI;
import com.dupreincaperu.dupree.mh_required_api.RequiredUsuario;
import com.dupreincaperu.dupree.mh_required_api.Required_IdMessages;
import com.dupreincaperu.dupree.mh_response_api.CatalogoPremiosList;
import com.dupreincaperu.dupree.mh_response_api.RequiredLiquidar;
import com.dupreincaperu.dupree.mh_response_api.RequiredRedimirPremios;
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
import com.dupreincaperu.dupree.mh_response_api.ResponsePanelAsesora;
import com.dupreincaperu.dupree.mh_response_api.ResponsePanelGte;
import com.dupreincaperu.dupree.mh_response_api.ResponsePerfil;
import com.dupreincaperu.dupree.mh_response_api.ResponseProd_Catalogo;
import com.dupreincaperu.dupree.mh_response_api.ResponsePuntosAsesora;
import com.dupreincaperu.dupree.mh_response_api.ResponseRedimir;
import com.dupreincaperu.dupree.mh_response_api.ResponseReferidos;
import com.dupreincaperu.dupree.mh_response_api.ResponseRetenidos;
import com.dupreincaperu.dupree.mh_response_api.ResponseUrlCatalogos;
import com.dupreincaperu.dupree.mh_response_api.ResponseVersion;
import com.dupreincaperu.dupree.mh_response_api.mPerfil;
import com.dupreincaperu.dupree.mh_interface_api.iAuth;
import com.dupreincaperu.dupree.mh_interface_api.iBanners;
import com.dupreincaperu.dupree.mh_interface_api.iBarrios;
import com.dupreincaperu.dupree.mh_interface_api.iFile;
import com.dupreincaperu.dupree.mh_interface_api.iReport;
import com.dupreincaperu.dupree.mh_required_api.RequiredAuth;
import com.dupreincaperu.dupree.mh_required_api.RequiredCampana;
import com.dupreincaperu.dupree.mh_required_api.RequiredCode;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_required_api.RequiredInscription;
import com.dupreincaperu.dupree.mh_required_api.RequiredListPre;
import com.dupreincaperu.dupree.mh_required_api.RequiredNewPwd;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister;
import com.dupreincaperu.dupree.mh_required_api.RequiredTermins;
import com.dupreincaperu.dupree.mh_required_api.RequiredTerminsGerente;
import com.dupreincaperu.dupree.mh_required_api.RequiredValidateRef;
import com.dupreincaperu.dupree.mh_response_api.ResponseAuth;
import com.dupreincaperu.dupree.mh_response_api.ResponseBanner;
import com.dupreincaperu.dupree.mh_response_api.ResponseBarrio;
import com.dupreincaperu.dupree.mh_response_api.ResponseGeneric;
import com.dupreincaperu.dupree.mh_utilities.DownloadFileAsyncTask;
import com.dupreincaperu.dupree.mh_utilities.MyDialoges;
import com.dupreincaperu.dupree.mh_utilities.ProgressDialogHorizontal;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
  Created by cloudemotion on 25/8/17
 */

public class Http {
    private String TAG = "Http-->";
    String baseURL="https://alcor.dupree.pe/dupreeWS/";
    //String baseURL="https://alcor2per.azzorti.net/dupreeWS/";
    //String baseURL="https://alcor.dupree.co/dupreeWS/";
    //String baseURL="https://alcor.azzorti.bo/azzortiWS/";
    Context myContext;

    Retrofit retrofit;
    public Http(Context myContext) {
        this.myContext = myContext;

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new AddCookiesInterceptor(myContext));
        client.addInterceptor(new ReceivedCookiesInterceptor(myContext));
        client.addInterceptor(new ConnectivityInterceptor(myContext));
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        client.writeTimeout(60, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();



    }

    /*
    public Http(String baseURL, Context myContext) {
        this.baseURL = baseURL;
        this.myContext = myContext;

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                //.addConverterFactory(GsonConverterFactory.create())
                //.client(client.build())
                .build();
    }*/

    private GenericListener genericListener;
    private boolean genericListenerON = false;
    public void setGenericListener(GenericListener genericListener) {
        this.genericListener = genericListener;
        genericListenerON = true;
    }
    public interface GenericListener {
        void onProcess(String message);
        void onFailed();
    }


    public void getBanners()
    {
        final iBanners service = retrofit.create(iBanners.class);

        Log.e(TAG, "getBanners()");

        Call<ResponseBanner> call = service.obtainBanner();


        call.enqueue(new Callback<ResponseBanner>() {
            @Override
            public void onResponse(Call<ResponseBanner> call, Response<ResponseBanner> response) {
                Log.e(TAG+"onResponse", call.request().url().toString());
                if (!response.isSuccessful()) {
                    Log.e(TAG+"onResponse", " : " + new Gson().toJson(response.body()));
                    ((FullscreenActivity) myContext).errorLoadInitData();
                } else {
                    //Toast.makeText(myContext, myContext.getResources().getString(R.string.operacion_exitosa), Toast.LENGTH_LONG).show();
                    Log.e(TAG+"onResponse", "-> " + new Gson().toJson(response.body()));
                    //Log.e(TAG+"onResponse", "-> version: " + response.body().getVersion());

                    ((FullscreenActivity) myContext).responseBanner(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBanner> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){

                }
                ((FullscreenActivity) myContext).errorLoadInitData();
            }
        });
    }

    public void getVersion()
    {
        showDialogWait();
        final iBanners service = retrofit.create(iBanners.class);
        Log.e(TAG, "getVersion()");
        Call<ResponseVersion> call = service.obtainVersion();
        call.enqueue(new Callback<ResponseVersion>() {
            @Override
            public void onResponse(Call<ResponseVersion> call, Response<ResponseVersion> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());
                if (!response.isSuccessful()) {
                    //Log.e(TAG+"onResponse", " : " + new Gson().toJson(response.body()));
                    if(genericListenerON){
                        genericListener.onFailed();
                    }
                } else {
                    //Toast.makeText(myContext, myContext.getResources().getString(R.string.operacion_exitosa), Toast.LENGTH_LONG).show();
                    Log.e(TAG+"onResponse", "-> " + new Gson().toJson(response.body()));
                    if(genericListenerON){
                        genericListener.onProcess(response.body().getVersion());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseVersion> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){

                }
                if(genericListenerON){
                    genericListener.onFailed();
                }
            }
        });
    }

    private BandejaListener bandejaListener;
    private boolean bandejaListenerON = false;
    public void setBandejaListener(BandejaListener bandejaListener) {
        this.bandejaListener = bandejaListener;
        bandejaListenerON = true;
    }

    public interface BandejaListener {
        void onProcess(ResponseBandeja responseBandeja);
        void onFailed();
    }

    public void obtainBandejaEntrada()
    {
        final iReport service = retrofit.create(iReport.class);

        //showDialogWait();
        Log.e("JSON obtainBandeja", "Params: ");

        /*ENVIANDO TOKEN EN LA CABECERA*/
        //Map<String, String> map = new HashMap<>();
        //map.put("Authorization", "jwt ".concat(Preferences.getToken(myContext)));
        Call<ResponseBandeja> call = service.obtainMessages();

        call.enqueue(new Callback<ResponseBandeja>() {
            @Override
            public void onResponse(Call<ResponseBandeja> call, Response<ResponseBandeja> response) {
                String msgError=null;
                Log.e("onResponse", call.request().url().toString());
                //stopDialogoWait();
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e("LOG", "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            Log.e(TAG, "sendChat-> TRY");
                            if(resp.getRaise()!=null)
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            else
                                msgError = (myContext.getResources().getString(R.string.http_error_desconocido));//msgError = resp.getMessage();
                        } catch (IOException | JsonSyntaxException e) {
                            Log.e(TAG, "sendChat-> CATCH");
                            msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                        }
                    } else {
                        Log.e(TAG, "200: "+new Gson().toJson(response.body()));
                        if(bandejaListenerON)
                            bandejaListener.onProcess(response.body());
                    }
                } else {
                    Log.e(TAG, "addMyFavorite-> ELSE");
                    msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                    //toastMSG(myContext.getResources().getString(R.string.error_desconocido));
                }

                if(msgError!=null) {
                    toastMSG(msgError);
                    if(code==401){
                        gotoMain();
                    } else {
                        if(bandejaListenerON)
                            bandejaListener.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBandeja> call, Throwable t) {

                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                }
                if(bandejaListenerON)
                    bandejaListener.onFailed();
            }
        });

    }

    public void readMessages(Required_IdMessages requiredIdMessaage)
    {
        final iReport service = retrofit.create(iReport.class);

        //showDialogWait();
        Log.e("JSON readMessages", "Params: "+new Gson().toJson(requiredIdMessaage));

        /*ENVIANDO TOKEN EN LA CABECERA*/
        Map<String, String> map = new HashMap<>();
        //map.put("Authorization", "jwt ".concat(Preferences.getToken(myContext)));
        Call<ResponseGeneric> call = service.readMessages(
                new Gson().toJson(requiredIdMessaage)
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                String msgError=null;
                Log.e("onResponse", call.request().url().toString());
                //stopDialogoWait();
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e("LOG", "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            Log.e(TAG, "sendChat-> TRY");
                            if(resp.getRaise()!=null)
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            else
                                msgError = (myContext.getResources().getString(R.string.http_error_desconocido));//msgError = resp.getMessage();
                        } catch (IOException | JsonSyntaxException e) {
                            Log.e(TAG, "sendChat-> CATCH");
                            msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                        }
                    } else {
                        Log.e(TAG, "200: "+new Gson().toJson(response.body()));
                        if(bandejaListenerON)
                            bandejaListener.onProcess(null);
                    }
                } else {
                    Log.e(TAG, "addMyFavorite-> ELSE");
                    msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                    //toastMSG(myContext.getResources().getString(R.string.error_desconocido));
                }

                if(msgError!=null) {
                    toastMSG(msgError);
                    if(code==401){
                        gotoMain();
                    } else {
                        if(bandejaListenerON)
                            bandejaListener.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                }
                if(bandejaListenerON)
                    bandejaListener.onFailed();
            }
        });

    }

    public void deleteMessages(Required_IdMessages requiredIdMessaage)
    {
        final iReport service = retrofit.create(iReport.class);

        //showDialogWait();
        Log.e("JSON deleteMessages", "Params: "+new Gson().toJson(requiredIdMessaage));

        /*ENVIANDO TOKEN EN LA CABECERA*/
        Map<String, String> map = new HashMap<>();
        //map.put("Authorization", "jwt ".concat(Preferences.getToken(myContext)));
        Call<ResponseGeneric> call = service.deleteMessages(
                new Gson().toJson(requiredIdMessaage)
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                String msgError=null;
                Log.e("onResponse", call.request().url().toString());
                //stopDialogoWait();
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e("LOG", "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            Log.e(TAG, "sendChat-> TRY");
                            if(resp.getRaise()!=null)
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            else
                                msgError = (myContext.getResources().getString(R.string.http_error_desconocido));//msgError = resp.getMessage();
                        } catch (IOException | JsonSyntaxException e) {
                            Log.e(TAG, "sendChat-> CATCH");
                            msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                        }
                    } else {
                        Log.e(TAG, "200: "+new Gson().toJson(response.body()));
                        if(bandejaListenerON)
                            bandejaListener.onProcess(null);
                    }
                } else {
                    Log.e(TAG, "addMyFavorite-> ELSE");
                    msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                    //toastMSG(myContext.getResources().getString(R.string.error_desconocido));
                }

                if(msgError!=null) {
                    toastMSG(msgError);
                    if(code==401){
                        gotoMain();
                    } else {
                        if(bandejaListenerON)
                            bandejaListener.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                }
                if(bandejaListenerON)
                    bandejaListener.onFailed();
            }
        });

    }

    public void getUrlCatalogos(final boolean animation)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getUrlCatalogos()");

        Call<ResponseUrlCatalogos> call = service.obtainUrlCatalogos();

        if(animation)
            showDialogWait();

        call.enqueue(new Callback<ResponseUrlCatalogos>() {
            @Override
            public void onResponse(Call<ResponseUrlCatalogos> call, Response<ResponseUrlCatalogos> response) {
                if(animation)
                    stopDialogoWait();

                Log.e(TAG+"onResponse", call.request().url().toString());
                if (!response.isSuccessful()) {
                    Log.e(TAG+"onResponse", " : " + new Gson().toJson(response.body()));
                    ((FullscreenActivity) myContext).errorLoadInitData();
                } else {
                    //Toast.makeText(myContext, myContext.getResources().getString(R.string.operacion_exitosa), Toast.LENGTH_LONG).show();
                    Log.e(TAG+"onResponse", "-> " + new Gson().toJson(response.body()));
                    ((FullscreenActivity) myContext).responseFileCatalogos(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseUrlCatalogos> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }

                if(animation)
                    stopDialogoWait();

                ((FullscreenActivity) myContext).errorLoadInitData();


            }
        });
    }

    public void getBarrios(String id_ciudad, final String tagFragment, final String objectFragment)
    {
        final iBarrios service = retrofit.create(iBarrios.class);

        Log.e(TAG, "getBarrios(), city = "+id_ciudad);

        Call<ResponseBarrio> call = service.obtainBarrios(id_ciudad);

        showDialogWait();
        call.enqueue(new Callback<ResponseBarrio>() {
            @Override
            public void onResponse(Call<ResponseBarrio> call, Response<ResponseBarrio> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());
                if (!response.isSuccessful()) {
                    Log.e(TAG+"onResponse", " : " + new Gson().toJson(response.body()));
                } else {
                    Log.e(TAG+"onResponse", "-> " + new Gson().toJson(response.body()));
                    publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getBarrios()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBarrio> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void Auth(final RequiredAuth requiredAuth)
    {
        final iAuth service = retrofit.create(iAuth.class);
        Log.e("ESTAAA",requiredAuth.toString());

        Log.e(TAG+"JSON Auth", "Params: "+new Gson().toJson(requiredAuth));
        Call<ResponseAuth> call = service.auth(
                new Gson().toJson(requiredAuth)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseAuth resp = new Gson().fromJson(jsonInString, ResponseAuth.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"onResponse", "-> " + new Gson().toJson(response.body()));
                        ((MainActivity) myContext).successfulAuth(response.body());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null)
                    msgToast(msgError);
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void notifyForgot(RequiredIdenty requiredIdenty)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON Forgot", "Params: "+new Gson().toJson(requiredIdenty));
        Call<ResponseGeneric> call = service.notifyForgot(
                new Gson().toJson(requiredIdenty)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        ((MainActivity) myContext).successfulNotifyForgot(response.body());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null)
                    msgToast(msgError);
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void validateCode(final RequiredCode requiredCode)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON COde", "Params: "+new Gson().toJson(requiredCode));
        Call<ResponseGeneric> call = service.validateCode(
                new Gson().toJson(requiredCode)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());


                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        ((MainActivity) myContext).successfulValidateCode(response.body(), requiredCode.getCodigo());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void refreshTokenFCM(final RequiredRefreshToken requiredRefreshToken)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON tokenFCM", "Params: "+new Gson().toJson(requiredRefreshToken));
        Call<ResponseGeneric> call = service.refreshToken(
                new Gson().toJson(requiredRefreshToken)
        );

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());


                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        //((MainActivity) myContext).successfulValidateCode(response.body(), requiredCode.getCodigo());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        //gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    //toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void validateNewPwd(RequiredNewPwd requiredNewPwd)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON Password", "Params: "+new Gson().toJson(requiredNewPwd));
        Call<ResponseGeneric> call = service.validateCode(
                new Gson().toJson(requiredNewPwd)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        ((MainActivity) myContext).successfulNewPwd(response.body());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public static  final String BROACAST_DATA="broacast_data";

    public void register_main(RequiredRegister_NEW_2018 requiredRegister)
    {
        final iAuth service = retrofit.create(iAuth.class);

        //showDialogWait();
        Log.e("JSON obtainBandeja", "Params: "+ new Gson().toJson(requiredRegister));

        Call<ResponseGeneric> call = service.vuelveteAsesora(
                new Gson().toJson(requiredRegister)
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                String msgError=null;
                Log.e("onResponse", call.request().url().toString());
                //stopDialogoWait();
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e("LOG", "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            Log.e(TAG, "sendChat-> TRY");
                            if(resp.getRaise()!=null)
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            else
                                msgError = (myContext.getResources().getString(R.string.http_error_desconocido));//msgError = resp.getMessage();
                        } catch (IOException | JsonSyntaxException e) {
                            Log.e(TAG, "sendChat-> CATCH");
                            msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                        }
                    } else {
                        Log.e(TAG, "200: "+new Gson().toJson(response.body()));
                        if(genericListenerON)
                            genericListener.onProcess(response.body().getResult());
                    }
                } else {
                    Log.e(TAG, "addMyFavorite-> ELSE");
                    msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                    //toastMSG(myContext.getResources().getString(R.string.error_desconocido));
                }

                if(msgError!=null) {
                    toastMSG(msgError);
                    if(code==401){
                        gotoMain();
                    } else {
                        if(genericListenerON)
                            genericListener.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    //toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
                if(genericListenerON)
                    genericListener.onFailed();
            }
        });
    }

    public void getCatalogoPremios()
    {
        final iReport service = retrofit.create(iReport.class);

        //showDialogWait();
        Log.e("JSON getCatalogoPremios", "Params: "/*+ new Gson().toJson(requiredRegister)*/);

        Call<CatalogoPremiosList> call = service.obtainCatalogoPremios();

        call.enqueue(new Callback<CatalogoPremiosList>() {
            @Override
            public void onResponse(Call<CatalogoPremiosList> call, Response<CatalogoPremiosList> response) {
                String msgError=null;
                Log.e("onResponse", call.request().url().toString());
                //stopDialogoWait();
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e("LOG", "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            Log.e(TAG, "sendChat-> TRY");
                            if(resp.getRaise()!=null)
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            else
                                msgError = (myContext.getResources().getString(R.string.http_error_desconocido));//msgError = resp.getMessage();
                        } catch (IOException | JsonSyntaxException e) {
                            Log.e(TAG, "sendChat-> CATCH");
                            msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                        }
                    } else {
                        Log.e(TAG, "200: "+new Gson().toJson(response.body()));
                        if(genericListenerON)
                            genericListener.onProcess(new Gson().toJson(response.body()));
                    }
                } else {
                    Log.e(TAG, "addMyFavorite-> ELSE");
                    msgError = (myContext.getResources().getString(R.string.http_error_desconocido));
                    //toastMSG(myContext.getResources().getString(R.string.error_desconocido));
                }

                if(msgError!=null) {
                    toastMSG(msgError);
                    if(code==401){
                        gotoMain();
                    } else {
                        if(genericListenerON)
                            genericListener.onFailed();
                    }
                }
            }

            @Override
            public void onFailure(Call<CatalogoPremiosList> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    //toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
                if(genericListenerON)
                    genericListener.onFailed();
            }
        });
    }

    public void register(RequiredRegister requiredRegister, final String tagFragment, final String objectFragment, final String error)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON Password", "Params: "+new Gson().toJson(requiredRegister));
        Call<ResponseGeneric> call = service.preinscripcion(
                new Gson().toJson(requiredRegister)
        );
        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {

                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));
                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        /*
                        if(mActivity==ACTIVITY_MAIN) {
                            ((MainActivity) myContext).successfulRegister(response.body());
                        } else if(mActivity==ACTIVITY_REGISTER) {
                            ((MenuActivity) myContext).successfulRegister(response.body());
                        }
                        */
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);

                }

                if(msgError!=null) {
                    msgToast(msgError);

                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void register_new(RequiredRegister_NewAPI requiredRegister, final String tagFragment, final String objectFragment, final String error)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON Password", "Params: "+new Gson().toJson(requiredRegister));
        Call<ResponseGeneric> call = service.preinscripcion(
                new Gson().toJson(requiredRegister)
        );
        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {

                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));
                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        /*
                        if(mActivity==ACTIVITY_MAIN) {
                            ((MainActivity) myContext).successfulRegister(response.body());
                        } else if(mActivity==ACTIVITY_REGISTER) {
                            ((MenuActivity) myContext).successfulRegister(response.body());
                        }
                        */
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);

                }

                if(msgError!=null) {
                    msgToast(msgError);

                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void termins(RequiredTermins requiredTermins, final String tagFragment, final String objectFragment, final String error)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON termins", "Params: "+new Gson().toJson(requiredTermins));
        Call<ResponseGeneric> call = service.termins(
                new Gson().toJson(requiredTermins)
        );

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));
                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        /*)
                        //((MainActivity) myContext).successfulTermins(response.body());
                        if(mActivity==ACTIVITY_MAIN) {
                            ((MainActivity) myContext).successfulTermins(response.body());
                        } else if(mActivity==ACTIVITY_REGISTER) {
                            ((MenuActivity) myContext).successfulTermins(response.body());
                        }
                        */
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void uploadImage(String filePath, final String tagFragment, final String objectFragment)
    {
        final iFile service = retrofit.create(iFile.class);

        File file = new File(filePath);

        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(filePath));
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getName()));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        Log.e("JSON", "name: "+name.toString());

        Call<ResponseGeneric> call = service.uploadImage(
                body/*,
                name*/
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        //((MenuActivity) myContext).successUploadImage(response.body());
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));

                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    private File convertFileFromBitmap(Bitmap bitmap){
        //create a file to write bitmap data
        try {
            File f = new File(myContext.getCacheDir(), Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            f.createNewFile();
            //Convert bitmap to byte array
            //Bitmap bitmap = bitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            return f;
        }catch (IOException e){
            return null;
        }

    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        int scaleFactor = 1;//potencias de 2 (1= mitad , 4 , carta parte, 8 octaba parte)

        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            //scaleFactor = Math.max(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        return bitmap;
    }

    public void terminsGerente(RequiredTerminsGerente requiredTermins, final String tagFragment, final String objectFragment, final String error)
    {
        final iAuth service = retrofit.create(iAuth.class);

        Log.e(TAG+"JSON termins", "Params: "+new Gson().toJson(requiredTermins));
        Call<ResponseGeneric> call = service.terminsGerente(
                new Gson().toJson(requiredTermins)
        );

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));
                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getCampanas(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getCampanas()");

        Call<ResponseCampana> call = service.obtainCampanas();

        //showDialogWait();
        call.enqueue(new Callback<ResponseCampana>() {
            @Override
            public void onResponse(Call<ResponseCampana> call, Response<ResponseCampana> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseCampana resp = new Gson().fromJson(jsonInString, ResponseCampana.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getCampanaList()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getCampanaList()));
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);

                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCampana> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPerfilUser(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getPerfilUser()");

        Call<ResponsePerfil> call = service.getPerfilUser("");

        //showDialogWait();
        call.enqueue(new Callback<ResponsePerfil>() {
            @Override
            public void onResponse(Call<ResponsePerfil> call, Response<ResponsePerfil> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePerfil resp = new Gson().fromJson(jsonInString, ResponsePerfil.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getPerfilList()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getPerfilList().get(0)));
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);

                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePerfil> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void editarPerfil(mPerfil mPerfil, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "editarPerfil()");
        Log.e(TAG+"JSON", ": "+new Gson().toJson(mPerfil));

        Call<ResponseAuth> call = service.EditPerfil(new Gson().toJson(mPerfil));

        showDialogWait();
        call.enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseAuth resp = new Gson().fromJson(jsonInString, ResponseAuth.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);

                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getDetailCampanas(RequiredCampana campana, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getDetailCampanas()");

        Call<ResponsePanelGte> call = service.obtainPanelGrte(new Gson().toJson(campana));

        showDialogWait();
        call.enqueue(new Callback<ResponsePanelGte>() {
            @Override
            public void onResponse(Call<ResponsePanelGte> call, Response<ResponsePanelGte> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePanelGte resp = new Gson().fromJson(jsonInString, ResponsePanelGte.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()/*.getListDetail().getPanelGteDetails()*/));
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePanelGte> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPedidosRetenidos(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getPedidosRetenidos()");

        Call<ResponseRetenidos> call = service.obtainPedRetenidos(new Gson().toJson(requiredIdenty));

        showDialogWait();
        call.enqueue(new Callback<ResponseRetenidos>() {
            @Override
            public void onResponse(Call<ResponseRetenidos> call, Response<ResponseRetenidos> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseRetenidos resp = new Gson().fromJson(jsonInString, ResponseRetenidos.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getListTitleRetenidos().getRetenidoList()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetenidos> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getCupoSaldoCOnf(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getCupoSaldoCOnf()");

        Call<ResponseCupoSaldoConf> call = service.obtainCupoSaldoConf(new Gson().toJson(requiredIdenty));

        showDialogWait();
        call.enqueue(new Callback<ResponseCupoSaldoConf>() {
            @Override
            public void onResponse(Call<ResponseCupoSaldoConf> call, Response<ResponseCupoSaldoConf> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseCupoSaldoConf resp = new Gson().fromJson(jsonInString, ResponseCupoSaldoConf.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getCupoSaldoConfList().get(0)));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCupoSaldoConf> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getListaPre(RequiredListPre requiredListPre, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getCupoSaldoCOnf()");

        Call<ResponseListPreinscripcion> call = service.obtainListPre(
                requiredListPre.getPerfil(),
                requiredListPre.getIndex_pages(),
                requiredListPre.getValor(),
                requiredListPre.getToken());

        showDialogWait();
        call.enqueue(new Callback<ResponseListPreinscripcion>() {
            @Override
            public void onResponse(Call<ResponseListPreinscripcion> call, Response<ResponseListPreinscripcion> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseListPreinscripcion resp = new Gson().fromJson(jsonInString, ResponseListPreinscripcion.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseListPreinscripcion> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void validateReferido(RequiredValidateRef requiredValidateRef, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "validateReferido()");

        Call<ResponseGeneric> call = service.validateReferido(new Gson().toJson(requiredValidateRef));

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void aprobarPreinscriccion(RequiredApprovePreIns requiredApprovePreIns, final String tagFragment, final String objectFragment, final String error)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "aprobarPreinscriccion() "+new Gson().toJson(requiredApprovePreIns));

        Call<ResponseGeneric> call = service.aprobarPreinscripcion(new Gson().toJson(requiredApprovePreIns));

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void validateCentralRiesgo(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "validateReferido()");

        Call<ResponseGeneric> call = service.validateCentralRiesgo(new Gson().toJson(requiredIdenty));

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void inscribir_NEW(RequiredInscription_NEW requiredInscription, final String tagFragment, final String objectFragment, final String error)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "inscribir(): "+new Gson().toJson(requiredInscription));

        Call<ResponseGeneric> call = service.inscribir(new Gson().toJson(requiredInscription));

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    publishError(tagFragment, error);
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }


    public void inscribir(RequiredInscription requiredInscription, final String tagFragment, final String objectFragment, final String error)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "inscribir(): "+new Gson().toJson(requiredInscription));

        Call<ResponseGeneric> call = service.inscribir(new Gson().toJson(requiredInscription));

        //showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    publishError(tagFragment, error);
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getDataEditInscripcion(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment, final String error)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getDataEditInscripcion(): "+new Gson().toJson(requiredIdenty));

        Call<RequiredInscription_NEW> call = service.editInscription(new Gson().toJson(requiredIdenty));

        //showDialogWait();
        call.enqueue(new Callback<RequiredInscription_NEW>() {
            @Override
            public void onResponse(Call<RequiredInscription_NEW> call, Response<RequiredInscription_NEW> response) {
                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    publishError(tagFragment, error);
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<RequiredInscription_NEW> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    toastMSG(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPanelAsesora(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getPanelAsesora()");

        Call<ResponsePanelAsesora> call = service.obtainPanelAsesora();

        showDialogWait();
        call.enqueue(new Callback<ResponsePanelAsesora>() {
            @Override
            public void onResponse(Call<ResponsePanelAsesora> call, Response<ResponsePanelAsesora> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());
                Log.e("SERGIO","PRUEBA RESPONSE");
                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePanelAsesora resp = new Gson().fromJson(jsonInString, ResponsePanelAsesora.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getPanelAsesora()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePanelAsesora> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                    Log.e("ASESORA",call.request().toString());
                }
            }
        });

    }



    public void getFaltantes(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getFaltante()");

        Call<ResponseFaltantes> call = service.obtainFaltantes();

        showDialogWait();
        call.enqueue(new Callback<ResponseFaltantes>() {
            @Override
            public void onResponse(Call<ResponseFaltantes> call, Response<ResponseFaltantes> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseFaltantes resp = new Gson().fromJson(jsonInString, ResponseFaltantes.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFaltantes> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getRedimirPremios(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getRedimirPremios()");

        Call<ResponseRedimir> call = service.obtainRedimirIncentivos(
                new Gson().toJson(new RequiredIdenty("")),
                new Gson().toJson(new RequiredIndex("1"))
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseRedimir>() {
            @Override
            public void onResponse(Call<ResponseRedimir> call, Response<ResponseRedimir> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseRedimir resp = new Gson().fromJson(jsonInString, ResponseRedimir.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRedimir> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void redimirPremios(RequiredRedimirPremios requiredRedimirPremios, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "redimirPremios(): "+new Gson().toJson(requiredRedimirPremios));

        Call<ResponseGeneric> call = service.redimirPremios(new Gson().toJson(requiredRedimirPremios));

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPuntosAsesora(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getPuntosAsesora(), requiredIdenty: "+new Gson().toJson(requiredIdenty));

        Call<ResponsePuntosAsesora> call = service.obtainPuntosAsesora(new Gson().toJson(requiredIdenty));

        showDialogWait();
        call.enqueue(new Callback<ResponsePuntosAsesora>() {
            @Override
            public void onResponse(Call<ResponsePuntosAsesora> call, Response<ResponsePuntosAsesora> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePuntosAsesora resp = new Gson().fromJson(jsonInString, ResponsePuntosAsesora.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePuntosAsesora> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getIncentivosReferidos(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getIncentivosReferidos()");

        Call<ResponseReferidos> call = service.obtainIncentivosReferido(
                new Gson().toJson(requiredIdenty),
                new Gson().toJson(new RequiredIndex("1")));

        showDialogWait();
        call.enqueue(new Callback<ResponseReferidos>() {
            @Override
            public void onResponse(Call<ResponseReferidos> call, Response<ResponseReferidos> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseReferidos resp = new Gson().fromJson(jsonInString, ResponseReferidos.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReferidos> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getCDR(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getIncentivosReferidos()");

        Call<ResponseCDR> call = service.obtainCDR(
                new Gson().toJson(requiredIdenty),
                new Gson().toJson(new RequiredIndex("1")));

        showDialogWait();
        call.enqueue(new Callback<ResponseCDR>() {
            @Override
            public void onResponse(Call<ResponseCDR> call, Response<ResponseCDR> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseCDR resp = new Gson().fromJson(jsonInString, ResponseCDR.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCDR> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPQR(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getIncentivosReferidos()");

        Call<ResponsePQR> call = service.obtainPQR(
                new Gson().toJson(requiredIdenty),
                new Gson().toJson(new RequiredIndex("1")));

        showDialogWait();
        call.enqueue(new Callback<ResponsePQR>() {
            @Override
            public void onResponse(Call<ResponsePQR> call, Response<ResponsePQR> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePQR resp = new Gson().fromJson(jsonInString, ResponsePQR.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePQR> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getFactura(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getFactura()");

        Call<ResponseFactura> call = service.obtainFacturas(
                new Gson().toJson(requiredIdenty)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseFactura>() {
            @Override
            public void onResponse(Call<ResponseFactura> call, Response<ResponseFactura> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseFactura resp = new Gson().fromJson(jsonInString, ResponseFactura.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseFactura> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getPagosRealizados(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getFactura()");

        Call<ResponsePagos> call = service.obtainPagos(
                new Gson().toJson(requiredIdenty)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponsePagos>() {
            @Override
            public void onResponse(Call<ResponsePagos> call, Response<ResponsePagos> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponsePagos resp = new Gson().fromJson(jsonInString, ResponsePagos.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePagos> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getEstadoPedidos(RequiredIdenty requiredIdenty, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getEstadoPedidos()");

        Call<ResponseEstadoPedido> call = service.obtainEstadoPedido(
                new Gson().toJson(requiredIdenty)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseEstadoPedido>() {
            @Override
            public void onResponse(Call<ResponseEstadoPedido> call, Response<ResponseEstadoPedido> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseEstadoPedido resp = new Gson().fromJson(jsonInString, ResponseEstadoPedido.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {

                        Log.e(TAG + "JSON response", ": " + "111111111");

                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body().getResult().getProductos()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseEstadoPedido> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void confirmarPedidoById(RequiredConfirmaPedido requiredConfirmaPedido, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "confirmarPedidoById()");

        Call<ResponseGeneric> call = service.confirmarPedido(
                new Gson().toJson(requiredConfirmaPedido)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public static final String CODE_DEBAJJO_MONTO="E001";
    public static String CODE_OK="S001";
    public void liquidarPedido(final RequiredLiquidar requiredLiquidar, final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "liquidarPedido(): "+new Gson().toJson(requiredLiquidar));

        Call<ResponseLiquida> call = service.liquidarPedido(
                new Gson().toJson(requiredLiquidar)
        );

        showDialogWait();
        call.enqueue(new Callback<ResponseLiquida>() {
            @Override
            public void onResponse(Call<ResponseLiquida> call, Response<ResponseLiquida> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseLiquida resp = new Gson().fromJson(jsonInString, ResponseLiquida.class);

                            if(code==404 && resp.getCodigo()!=null) {
                                if(resp.getCodigo().equals(CODE_DEBAJJO_MONTO)){
                                    //msgError = resp.getMensaje();
                                    NumberFormat formatter = NumberFormat.getInstance(Locale.US);

                                    ((MenuActivity) myContext).snackBar
                                            (
                                            "Total: "
                                            .concat("$".concat(formatter.format(Float.parseFloat(resp.getTotal_pedido()))))
                                            .concat(". ")
                                            .concat(resp.getMensaje())
                                            );
                                }
                            } else {
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            }

                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {

                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLiquida> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getProductosCatalogo(final boolean animation)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getProductosCatalogo()");

        Call<ResponseProd_Catalogo> call = service.obtainProductos();

        if(animation)
            showDialogWait();
        call.enqueue(new Callback<ResponseProd_Catalogo>() {
            @Override
            public void onResponse(Call<ResponseProd_Catalogo> call, Response<ResponseProd_Catalogo> response) {
                if(animation)
                    stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {


                        try {
                            String jsonInString = response.errorBody().string();
                            //Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseProd_Catalogo resp = new Gson().fromJson(jsonInString, ResponseProd_Catalogo.class);

                            if(code==404){
                                ((FullscreenActivity) myContext).responseCatalogo(resp.getResult());
                            }else {
                                msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                            }

                        } catch (IOException | JsonSyntaxException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            //publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                            ((FullscreenActivity) myContext).responseCatalogo(response.body().getResult());
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    ((FullscreenActivity) myContext).errorLoadInitData();
                    //msgToast(msgError);
                }
            }

            @Override
            public void onFailure(Call<ResponseProd_Catalogo> call, Throwable t) {
                if(animation)
                    stopDialogoWait();

                ((FullscreenActivity) myContext).errorLoadInitData();

                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void getProductosCatalogo_broadcast(final String tagFragment, final String objectFragment)
    {
        final iReport service = retrofit.create(iReport.class);

        Log.e(TAG, "getProductosCatalogo()");

        Call<ResponseProd_Catalogo> call = service.obtainProductos();

        showDialogWait();
        call.enqueue(new Callback<ResponseProd_Catalogo>() {
            @Override
            public void onResponse(Call<ResponseProd_Catalogo> call, Response<ResponseProd_Catalogo> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseProd_Catalogo resp = new Gson().fromJson(jsonInString, ResponseProd_Catalogo.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        Log.e(TAG + "JSON response", ": " + new Gson().toJson(response.body()));
                        //((MenuActivity) myContext).responseHttpCampana(response.body().getCampanaList());
                        if (response.body().getCode() == 404) {//un detalle envia 200 con error 404
                            msgError = response.body().getRaise().get(0).getField().concat(". ").concat(response.body().getRaise().get(0).getError());
                        } else {
                            publishResultRegister(tagFragment, objectFragment, new Gson().toJson(response.body().getResult()));
                        }
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProd_Catalogo> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void downloadFile(final String tagFragment, final String objectFragment, final String error, String urlFile, final String fileName, final String fileDirectory){

        iFile service = retrofit.create(iFile.class);

        final ProgressDialogHorizontal dialogHorizontal = new ProgressDialogHorizontal(myContext);
        dialogHorizontal.showProgressDialog("Descargando archivo");

        service.getFile(urlFile).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, response.message());
                if(!response.isSuccessful()){
                    Log.e(TAG, "Something's gone wrong");
                    return;
                }

                //long fileSize = response.body().contentLength();

                //Log.e("fileSize", String.valueOf(fileSize));


                DownloadFileAsyncTask downloadFileAsyncTask = new DownloadFileAsyncTask(myContext, tagFragment, objectFragment, error, dialogHorizontal, fileName, fileDirectory);
                downloadFileAsyncTask.execute(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
                publishError(tagFragment, error);
                dialogHorizontal.dismissProgressDialog();
            }
        });
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show();
    }

    private void showDialogWait(){
        MyDialoges.showProgressDialog(myContext, myContext.getResources().getString(R.string.msg_espere));
    }

    public static void stopDialogoWait(){
        MyDialoges.dismissProgressDialog();
    }

    /**
     * BROADCAST
     * @param tagFragment
     * @param objectFragment
     * @param data
     */
    private void publishResultRegister(String tagFragment, String objectFragment, String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(myContext).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));
    }

    /////////////////// ENLACES ENTREA FRAGMENTOS Y ACTIVITIES /////////////////////
    public void enlaceLoadCamera(String tagFragment, String objectFragment){
        ((MenuActivity) myContext).takeImage(tagFragment, objectFragment).show();
    }

    public void enlaceInscription(String nombre, String cedula, boolean modeEdit){
        ((MenuActivity) myContext).gotoFragmInscription(nombre, cedula, modeEdit);
    }

    public void enlaceUpdateList(){
        ((MenuActivity) myContext).updateListPre();
    }

    /////////////////// ENLACES ENTREA FRAGMENTOS Y ACTIVITIES /////////////////////

    File file;
    public class fileFromBitmap extends AsyncTask<Void, Integer, String> {

        Context context;
        Bitmap bitmap;
        //String path_external = Environment.getExternalStorageDirectory() + File.separator + "dupree_temporary_file.jpg";

        public fileFromBitmap(Bitmap bitmap, Context context) {
            this.bitmap = bitmap;
            this.context= context;
            //Log.e("JSON", "11111111111: "+new Gson().toJson(filePath));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before executing doInBackground
            // update your UI
            // exp; make progressbar visible
        }

        @Override
        protected String doInBackground(Void... params) {

            //Log.e("JSON", "22222222: "+new Gson().toJson(filePath));
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            //si la quieres en discofile = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            file = new File(myContext.getCacheDir() + "dupree_temporary_file.jpg");//en cache
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // back to main thread after finishing doInBackground
            // update your UI or take action after
            // exp; make progressbar gone

            //sendFile(file);
            uploadImage_file2(file);

        }
    }

    String filePath,  tagFragment, objectFragment;
    public void uploadImage_file(String filePath, final String tagFragment, final String objectFragment)
    {
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(filePath));
        this.filePath=filePath;
        this.tagFragment=tagFragment;
        this.objectFragment=objectFragment;

        new fileFromBitmap(resizeBitmap(filePath, 960, 1200), myContext.getApplicationContext()).execute();
    }

    public void uploadImage_bitmap(Bitmap fileBitmap, final String tagFragment, final String objectFragment)
    {
        //Log.e("JSON", "uploadChatImage: "+new Gson().toJson(filePath));
        //this.filePath=filePath;
        this.tagFragment=tagFragment;
        this.objectFragment=objectFragment;

        new fileFromBitmap(fileBitmap, myContext.getApplicationContext()).execute();
    }

    public void uploadImage_file2(File file)
    {
        final iFile service = retrofit.create(iFile.class);
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getPath()));
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getName()));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), reqFile);
        //RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        //Log.e("JSON", "name: "+name.toString());

        Call<ResponseGeneric> call = service.uploadImage(
                body/*,
                name*/
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        //((MenuActivity) myContext).successUploadImage(response.body());
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));

                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    if(code==501){
                        gotoMain();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                stopDialogoWait();
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void uploadImage_File(File file, final String tagFragment, final String objectFragment, final String error)
    {
        //showDialogWait();//solo lo muestra en la primera imagen que se sube
        final iFile service = retrofit.create(iFile.class);

        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getPath()));
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getName()));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        Log.e("JSON", "name: "+name.toString());

        Call<ResponseGeneric> call = service.uploadImage(
                body/*,
                name*/
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {

                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        //((MenuActivity) myContext).successUploadImage(response.body());
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));

                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    public void uploadImage_Profile(File file, final String tagFragment, final String objectFragment, final String error)
    {
        //showDialogWait();//solo lo muestra en la primera imagen que se sube
        final iFile service = retrofit.create(iFile.class);

        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getPath()));
        Log.e("JSON", "uploadChatImage: "+new Gson().toJson(file.getName()));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload_test");

        Log.e("JSON", "name: "+name.toString());

        Call<ResponseGeneric> call = service.uploadImageProfile(
                body/*,
                name*/
        );

        call.enqueue(new Callback<ResponseGeneric>() {
            @Override
            public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {

                //stopDialogoWait();
                Log.e(TAG+"onResponse", call.request().url().toString());

                String msgError=null;
                int code=response.code();
                Log.e("code", String.valueOf(code) );
                if(code==200 || code==400 || code==401 || code==404 || code==501) {
                    if (!response.isSuccessful()) {
                        try {
                            String jsonInString = response.errorBody().string();
                            Log.e(TAG, "Retrofit Response : " + jsonInString);
                            ResponseGeneric resp = new Gson().fromJson(jsonInString, ResponseGeneric.class);

                            msgError = resp.getRaise().get(0).getField().concat(". ").concat(resp.getRaise().get(0).getError());
                        } catch (IOException e) {
                            msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                        }
                    } else {
                        //((MenuActivity) myContext).successUploadImage(response.body());
                        Log.e(TAG+"JSON response", ": "+new Gson().toJson(response.body().getResult()));

                        publishResultRegister(tagFragment, objectFragment, response.body().getResult());
                    }
                } else {
                    msgError = myContext.getResources().getString(R.string.http_error_desconocido);
                }

                if(msgError!=null) {
                    msgToast(msgError);
                    publishError(tagFragment, error);
                    if(code==501){
                        gotoMain();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                //stopDialogoWait();
                publishError(tagFragment, error);
                if(checkIdDataMovileAvailable(call.request().url().toString(), t)){
                    msgToast(myContext.getResources().getString(R.string.http_error_desconocido));
                }
            }
        });

    }

    private void publishError(String tagFragment, String error){
        Log.i(TAG,"publishResult: "+error);
        LocalBroadcastManager.getInstance(myContext).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, error)
                        .putExtra(BROACAST_DATA, error));
    }

    private void gotoMain(){
        mPreferences.cerrarSesion(myContext);
        myContext.startActivity(new Intent(myContext, MainActivity.class));
        if(((MenuActivity)myContext)!=null){
            ((MenuActivity)myContext).finish();
        }
    }

    private void toastMSG(String msg){
        Log.e(TAG, "toast, "+msg);
        Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show();
    }

    private boolean checkIdDataMovileAvailable(String url, Throwable t){
        Log.e(TAG+"onFailure", url);
        Log.e(TAG+"onFailure", t.getMessage());

        boolean noDataAvailable = (t instanceof NoConnectivityException);
        if(noDataAvailable){
            msgToast(myContext.getResources().getString(R.string.http_datos_no_disponibles));
        }
        return !noDataAvailable;
    }
}
