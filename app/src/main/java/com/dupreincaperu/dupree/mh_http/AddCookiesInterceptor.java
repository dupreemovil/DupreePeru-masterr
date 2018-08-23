package com.dupreincaperu.dupree.mh_http;

import android.content.Context;
import android.util.Log;

import com.dupreincaperu.dupree.mh_utilities.mPreferences;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cloudemotion on 26/8/17.
 */

/**
 * This interceptor put all the Cookies in mPreferences in the Request.
 * Your implementation on how to get the mPreferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class AddCookiesInterceptor implements Interceptor {
    private static String TAG = "AddCookiesInter-->";
    Context ctx;

    public AddCookiesInterceptor(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e(TAG, "intercept: try add cookies");
        Request.Builder builder = chain.request().newBuilder();
        Set<String> preferences = mPreferences.getCookiesAuth(ctx);


        //////NUEVA VALIDACION, NO AGREGA COOKIES CUANDO NO ESTA LOGUEADO
        String jsonPerfil = mPreferences.getJSON_TypePerfil(ctx);
        if(jsonPerfil!=null){
            //Perfil perfil = new Gson().fromJson(jsonPerfil, Perfil.class);
            //if( perfil.equals(Perfil.ADESORA) || perfil.equals(Perfil.ADESORA) || perfil.equals(Perfil.ADESORA) )

            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.e("OkHttp", "Adding Header: Cookie = " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }






        return chain.proceed(builder.build());
    }
}
