package com.dupreincaperu.dupree.mh_response_api;

import java.util.List;

/**
 * Created by cloudemotion on 28/8/17.
 */

public class ResponseGeneric {
    String status;
    Boolean valid;
    String result;
    int code;
    List<Raise> raise;


    public String getStatus() {
        return status;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getResult() {
        return result;
    }

    public int getCode() {
        return code;
    }

    public List<Raise> getRaise() {
        return raise;
    }
}
