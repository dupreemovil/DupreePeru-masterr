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
import com.dupreincaperu.dupree.mh_response_api.IncentivoRef;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_incentivo_referido;
import com.dupreincaperu.dupree.mh_response_api.ListaReferidos;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incentivos_Referido_Fragment extends Fragment {


    private final String TAG = "Incent_Referido_Frag";
    private final String BROACAST_INCENTIVOS_REFERIDO_ASESORA = "broadcast_incentivos_referido_asesora";

    private List<IncentivoRef> incentivoRefs, listFilter;
    private MH_Adapter_incentivo_referido adapter_incentivo_referido;
    public Incentivos_Referido_Fragment() {
        // Required empty public constructor
    }

    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    CardView cardViewBackGround;
    TextView tvNombreAsesora;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_incent_referido, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");
        RecyclerView rcvIncentREF = v.findViewById(R.id.rcvIncentREF);
        rcvIncentREF.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvIncentREF.setHasFixedSize(true);

        incentivoRefs = new ArrayList<>();
        listFilter = new ArrayList<>();
        //incentivoRefs = getResult();

        listFilter.addAll(incentivoRefs);

        adapter_incentivo_referido = new MH_Adapter_incentivo_referido(incentivoRefs, listFilter, getActivity());
        rcvIncentREF.setAdapter(adapter_incentivo_referido);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkReferidos();

        return v;
    }

    private void checkReferidos(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        incentivoRefs.clear();
        listFilter.clear();
        incentivoRefs.addAll(listaReferidos.getTable());
        listFilter.addAll(listaReferidos.getTable());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(listaReferidos.getAsesora());

        adapter_incentivo_referido.notifyDataSetChanged();
    }

    /*
    private List<IncentivoRef> getResult(){
        List<IncentivoRef> incentivoRefs = new ArrayList<>();
        
        incentivoRefs.add(new IncentivoRef("201712","57296599","LUCY MARINA BURGOS CAMARGO","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","57432879","AMARILIS ESTER CASTILLO ALGARIN","0","REFERIDO NO REALIZO PEDIDO"));
        incentivoRefs.add(new IncentivoRef("201712","57436648","DORIS CENTENO COBOS","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","59660394","SAIDY CENITH CARVAJAL CORTES","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","60424142","DORIS DURAN GOMEZ","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","63289416","MARTHA CECILIA PAEZ ORTIZ","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","63324171","MARIA DIOSLENI FIGUEROA GOMEZ","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","65555270","ANA LUCIA RODRIGUEZ PRADA","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","65746674","MARTHA LILIANA MANJARRES MORA","0","REFERIDO CON SALDO"));
        incentivoRefs.add(new IncentivoRef("201712","71656661","RAFAEL IGNACIO GARCIA VELASQUEZ","0","REFERIDO NO REALIZO PEDIDO"));
        incentivoRefs.add(new IncentivoRef("201712","7529035","CESAR ALFONSO RODELO CASTELLAR","0","REFERIDO NO REALIZO PEDIDO"));
        incentivoRefs.add(new IncentivoRef("201712","8048563","JOSE GREGORIO POLO DIAZ","0","REFERIDO NO REALIZO PEDIDO"));

        return incentivoRefs;

    }*/

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

    ListaReferidos listaReferidos;
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
                    case BROACAST_INCENTIVOS_REFERIDO_ASESORA:
                        String jsonIncentivo = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonIncentivo!=null){
                            listaReferidos = new Gson().fromJson(jsonIncentivo, ListaReferidos.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getIncentivosReferidos(new RequiredIdenty(cedula), TAG, BROACAST_INCENTIVOS_REFERIDO_ASESORA);
    }
}
