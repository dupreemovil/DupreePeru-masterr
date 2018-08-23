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
import com.dupreincaperu.dupree.mh_response_api.Faltante;
import com.dupreincaperu.dupree.mh_response_api.ResponseFaltantes;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Faltantes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pedidos_Faltantes_Fragment extends Fragment {

    private final String TAG = "Pedidos_Faltantes_Frag";
    private final String BROACAST_FALTANTES_OBTAIN = "broadcast_faltantes_obtaint";
    private TextView tvCamp;

    ResponseFaltantes faltantesHttp;
    private MH_Adapter_Faltantes adapter_faltantes;
    private List<Faltante> listFaltante, listFilter;

    public Pedidos_Faltantes_Fragment() {
        // Required empty public constructor
    }


    CardView cardViewBackGround;
    //ResponseFaltantes responseFaltantes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pedidos_faltantes, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);

        RecyclerView rcvFaltantes = v.findViewById(R.id.rcvFaltantes);
        rcvFaltantes.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvFaltantes.setHasFixedSize(true);

        //faltantesHttp = getFaltantes();

        listFaltante = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listFaltante = faltantesHttp.getResult();
        listFilter.addAll(listFaltante);


        adapter_faltantes = new MH_Adapter_Faltantes(listFaltante, listFilter, getActivity());
        rcvFaltantes.setAdapter(adapter_faltantes);

        tvCamp = v.findViewById(R.id.tvCamp);
        tvCamp.setText("");
        //tvCamp.setText("CAMPAÑA: ".concat(faltantesHttp.getCampana()));

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkFaltantes();

        return v;
    }

    private void checkFaltantes(){
        new Http(getActivity()).getFaltantes(TAG, BROACAST_FALTANTES_OBTAIN);
    }

    private void updateView(){
        listFaltante.clear();
        listFilter.clear();
        listFaltante.addAll(faltantesHttp.getResult());
        listFilter.addAll(faltantesHttp.getResult());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvCamp.setText("CAMPAÑA: ".concat(faltantesHttp.getCampana()));

        adapter_faltantes.notifyDataSetChanged();
    }




    /*
    public ResponseFaltantes getFaltantes(){
        List<Faltante> faltantes = new ArrayList<>();

        faltantes.add(new Faltante(23165,"VESTIDO MANAURE T-XL",23165));
        faltantes.add(new Faltante(55244,"OVEROL ORJUELA 3 T-M",55244));
        faltantes.add(new Faltante(65537,"BLUSA ROUSH T-L-XXL",65537));
        faltantes.add(new Faltante(75892,"CHAQUETA MERCUR T-XL",75892));
        faltantes.add(new Faltante(77180,"CHAQUETA FELICIANO T-L",77180));
        faltantes.add(new Faltante(94924,"JEAN LUCELLY T-10-6",94924));
        faltantes.add(new Faltante(517616,"CAMISA FLINN T-XL",517616));
        faltantes.add(new Faltante(62324,"JEAN MAYORCA T-8",62324));
        faltantes.add(new Faltante(68848,"BOLSO FEMENINO ANTONIA",68848));
        faltantes.add(new Faltante(75908,"BLUSA CAMISERA BELEN T-XL",75908));
        faltantes.add(new Faltante(94924,"JEAN LUCELLY T-10-6",94924));
        faltantes.add(new Faltante(18074,"BLUSA NAIMA T-S",18074));
        faltantes.add(new Faltante(53952,"CORRECTOR DE POSTURA.",53952));
        faltantes.add(new Faltante(65537,"BLUSA ROUSH T-L-XXL",65537));
        faltantes.add(new Faltante(70598,"JOGGER WESH T-M",70598));
        faltantes.add(new Faltante(77083,"PANTALON GREEDER T-34",77083));
        faltantes.add(new Faltante(94924,"JEAN LUCELLY T-10-6",94924));

        return new ResponseFaltantes("Faltantes", true, faltantes, "201712", 200);
    }
    */

    public void filterFaltantes(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);
        adapter_faltantes.getmFilter().filter(textFilter);
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
                    case BROACAST_FALTANTES_OBTAIN:
                        String jsonFaltantes = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonFaltantes!=null){
                            faltantesHttp = new Gson().fromJson(jsonFaltantes, ResponseFaltantes.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }



}
