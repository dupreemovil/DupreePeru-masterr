package com.dupreincaperu.dupree.mh_fragments_menu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_response_api.ListaPagos;
import com.dupreincaperu.dupree.mh_response_api.Pagosrealizados;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Pagos_Realiz;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_Pagos_Realizados_Fragment extends Fragment {

    private final String TAG = "Rep_Pagos_Realiz_Frag";
    private final String BROACAST_PAGOS_ASESORA = "broadcast_pagos_asesora";

    //ResponseFaltantes faltantesHttp;
    private MH_Adapter_Pagos_Realiz adapter_pagos;
    private List<Pagosrealizados> listPagos, listFilter;

    public Reporte_Pagos_Realizados_Fragment() {
        // Required empty public constructor
    }

    CardView cardViewBackGround;
    TextView tvNombreAsesora;
    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte_pagos_realizados, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        RecyclerView rcvPagosRealizados = v.findViewById(R.id.rcvPagosRealizados);
        rcvPagosRealizados.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvPagosRealizados.setHasFixedSize(true);

        //faltantesHttp = getFaltantes();

        listPagos = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listPagos = getResult();
        //listFilter.addAll(listPagos);
        adapter_pagos = new MH_Adapter_Pagos_Realiz(listPagos, listFilter, getActivity());
        rcvPagosRealizados.setAdapter(adapter_pagos);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkPagos();

        return v;
    }

    private void checkPagos(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        listPagos.clear();
        listFilter.clear();
        listPagos.addAll(listaPagos.getPago());
        listFilter.addAll(listaPagos.getPago());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(listaPagos.getAsesora());

        adapter_pagos.notifyDataSetChanged();
    }

    /*
    public List<Pagosrealizados> getResult(){
        List<Pagosrealizados> listPagos = new ArrayList<>();

        listPagos.add(new Pagosrealizados("07/27/2017","EFECTY",443800,1));
        listPagos.add(new Pagosrealizados("07/12/2017","EFECTY",254530,2));
        listPagos.add(new Pagosrealizados("06/24/2017","EFECTY",182600,3));

        return listPagos;
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        registerBroadcat();
        Log.i(TAG,"onResume()");
        //setSelectedItem(oldItem);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterBroadcat();
        Log.i(TAG,"onPause()");
    }

    public void registerBroadcat(){
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                localBroadcastReceiver,
                new IntentFilter(TAG));
    }

    public void unregisterBroadcat(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                localBroadcastReceiver);
    }

    ListaPagos listaPagos;
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            Log.i(TAG, "BroadcastReceiver");
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    case BROACAST_PAGOS_ASESORA:
                        String jsonPagos = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonPagos!=null){
                            listaPagos = new Gson().fromJson(jsonPagos, ListaPagos.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getPagosRealizados(new RequiredIdenty(cedula), TAG, BROACAST_PAGOS_ASESORA);
    }

}
