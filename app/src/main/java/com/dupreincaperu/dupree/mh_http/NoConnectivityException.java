package com.dupreincaperu.dupree.mh_http;

import java.io.IOException;

/**
 * Created by cloudemotion on 2/13/18.
 */

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
