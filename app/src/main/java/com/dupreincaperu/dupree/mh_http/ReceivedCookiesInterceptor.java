package com.dupreincaperu.dupree.mh_http;

import android.content.Context;
import android.util.Log;

import com.dupreincaperu.dupree.mh_utilities.mPreferences;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by cloudemotion on 26/8/17.
 */

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the mPreferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    private static String TAG = "ReceivedCookiesInter-->";
    Context ctx;

    public ReceivedCookiesInterceptor(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        Log.e(TAG, "intercept: try obtain cookies");

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Set<String> cookies = new HashSet<>();
            String token;
            for (String header : originalResponse.headers("Set-Cookie")) {
                //filtrar solo el que me interesa
                if(header.substring(0,3).equals("SID")) {
                    //Trae toda la trama y me interesa solo el SID
                    String[] separatedCookie = header.split(";");
                    cookies.add(separatedCookie[0]);
                    mPreferences.setCookiesAuth(cookies, ctx);

                    String[] separatedToken = separatedCookie[0].split("=");
                    token= separatedToken[1];
                    mPreferences.setTokenSesion(token, ctx);
                    Log.e(TAG, "Extract Header TRUE: cookies = " + separatedCookie[0]);
                    Log.e(TAG, "Extract Header TRUE: token = " + token);
                    break;//solo toma un SID, y omite los repetidos
                } else {
                    Log.e(TAG, "Extract Header FALSE: cookies = " + header);
                }
            }

        }
        /*
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            Set<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
                Log.e(TAG, "Extract Header: cookies = " + header);
            }
            mPreferences.setCookiesAuth(cookies, ctx);
        }
        */

        return originalResponse;
    }
}
