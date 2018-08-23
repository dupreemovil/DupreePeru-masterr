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
import com.dupreincaperu.dupree.mh_response_api.CDR;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_cdr;
import com.dupreincaperu.dupree.mh_response_api.ListaCDR;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_CDR_Fragment extends Fragment {

    private final String TAG = "Reporte_CDR_Fragment";
    private final String BROACAST_CDR_ASESORA = "broadcast_cdr_asesora";
    private TextView tvCamp;
    
    private MH_Adapter_cdr adapter_cdr;
    private List<CDR> listCDR, listFilter;

    public Reporte_CDR_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_reporte_cdr, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        RecyclerView rcvCDR = v.findViewById(R.id.rcvCDR);
        rcvCDR.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvCDR.setHasFixedSize(true);
        

        listCDR = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listCDR = getCDR();
        //listFilter.addAll(listCDR);
        adapter_cdr = new MH_Adapter_cdr(listCDR, listFilter, getActivity());
        rcvCDR.setAdapter(adapter_cdr);

        //tvCamp = v.findViewById(R.id.tvCamp);
        //tvCamp.setText("CAMPAÑA: ".concat(faltantesHttp.getCampana()));

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkCDR();

        return v;
    }

    private void checkCDR(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        listCDR.clear();
        listFilter.clear();
        listCDR.addAll(listaCDR.getTable());
        listFilter.addAll(listaCDR.getTable());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(listaCDR.getAsesora());

        adapter_cdr.notifyDataSetChanged();
    }

    public void filterCDR(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);
        adapter_cdr.getmFilter().filter(textFilter);
    }

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

    ListaCDR listaCDR;
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
                    //Datos personales
                    case BROACAST_CDR_ASESORA:
                        String jsonCDR = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonCDR!=null){
                            listaCDR = new Gson().fromJson(jsonCDR, ListaCDR.class);
                            updateView();
                        }
                        break;

                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getCDR(new RequiredIdenty(cedula), TAG, BROACAST_CDR_ASESORA);
    }
    
    
    
}
