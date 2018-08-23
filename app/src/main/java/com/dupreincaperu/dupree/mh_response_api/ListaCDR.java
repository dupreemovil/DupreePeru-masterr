package com.dupreincaperu.dupree.mh_response_api;

import com.dupreincaperu.dupree.mh_required_api.RequiredIndex;

import java.util.List;

/**
 * Created by cloudemotion on 2/9/17.
 */

public class ListaCDR {
    private String title_1;
    private String title_2;
    private String title_3;
    private String title_4;
    private String title_5;
    private String title_6;
    private String title_7;
    private String title_8;
    public List<CDR> table;
    public RequiredIndex paginator;
    private String asesora;

    public String getTitle_1() {
        return title_1;
    }

    public String getTitle_2() {
        return title_2;
    }

    public String getTitle_3() {
        return title_3;
    }

    public String getTitle_4() {
        return title_4;
    }

    public String getTitle_5() {
        return title_5;
    }

    public String getTitle_6() {
        return title_6;
    }

    public String getTitle_7() {
        return title_7;
    }

    public String getTitle_8() {
        return title_8;
    }

    public List<CDR> getTable() {
        return table;
    }

    public RequiredIndex getPaginator() {
        return paginator;
    }

    public String getAsesora() {
        return asesora;
    }
}
