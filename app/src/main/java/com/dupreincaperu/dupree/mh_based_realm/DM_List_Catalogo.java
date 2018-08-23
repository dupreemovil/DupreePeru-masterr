package com.dupreincaperu.dupree.mh_based_realm;

import com.dupreincaperu.dupree.mh_response_api.Catalogo;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by cloudemotion on 5/9/17.
 */

public class DM_List_Catalogo extends RealmObject{

    RealmList<Catalogo> catalogoList;

    public RealmList<Catalogo> getCatalogoList() {
        return catalogoList;
    }

    public void setCatalogoList(RealmList<Catalogo> catalogoList) {
        this.catalogoList = catalogoList;
    }

}
