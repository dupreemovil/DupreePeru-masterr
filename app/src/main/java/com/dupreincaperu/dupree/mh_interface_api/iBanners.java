package com.dupreincaperu.dupree.mh_interface_api;

import com.dupreincaperu.dupree.mh_response_api.ResponseBanner;
import com.dupreincaperu.dupree.mh_response_api.ResponseVersion;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cloudemotion on 25/8/17.
 */

public interface iBanners {
    @GET("panel/banner/")
    Call<ResponseBanner> obtainBanner();

    @GET("panel/version/")
    Call<ResponseVersion> obtainVersion();
}
