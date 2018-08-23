package com.dupreincaperu.dupree.mh_response_api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cloudemotion on 2/9/17.
 */

public class ListaPuntos {
    @SerializedName("resume")
    private ResumePuntos resume;
    @SerializedName("list")
    private List<PtosByCamp> list;

    public ResumePuntos getResume() {
        return resume;
    }

    public List<PtosByCamp> getList() {
        return list;
    }
}
