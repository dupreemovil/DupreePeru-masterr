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
import com.dupreincaperu.dupree.mh_response_api.PQR;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.ResponsePQR;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_SeguimientoPQR;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_SegPetQueRec_Fragment extends Fragment {

    private final String TAG = "Reporte_PQR_Frag";
    private final String BROACAST_PQR_ASESORA = "broadcast_pqr_asesora";

    //ResponseFaltantes faltantesHttp;
    private MH_Adapter_SeguimientoPQR adapterPQR;
    private List<PQR> listPQR, listFilter;

    public Reporte_SegPetQueRec_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_reporte_seg_pet_que_rec, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        RecyclerView rcvSeguimientoPQR = v.findViewById(R.id.rcvSeguimientoPQR);
        rcvSeguimientoPQR.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvSeguimientoPQR.setHasFixedSize(true);

        //faltantesHttp = getFaltantes();

        listPQR = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listPQR = getResult();
        listFilter.addAll(listPQR);


        adapterPQR = new MH_Adapter_SeguimientoPQR(listPQR, listFilter, getActivity());
        rcvSeguimientoPQR.setAdapter(adapterPQR);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkPQR();

        return v;
    }

    private void checkPQR(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        listPQR.clear();
        listFilter.clear();
        listPQR.addAll(listaPQR.getResult());
        listFilter.addAll(listaPQR.getResult());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(listaPQR.getAsesora());

        adapterPQR.notifyDataSetChanged();
    }

    /*
    public List<PQR> getResult(){
        List<PQR> listPQR = new ArrayList<>();

        listPQR.add(new PQR("1621732","2017-08-01 12:21:53","jacqueline_dominguez","GESTION ADICIONAL","GESTION ADICIONAL","Asesora indica que llevo unos productos a la reunión de canjes y pero a&uacute;n no se ha visto reflejado el descuento se aclara la fecha y los d&iacute;as para que ingrese el descuento ya que la reunión fue el d&iacute;a 26/07/2017"));

        return listPQR;
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

    ResponsePQR listaPQR;
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
                    case BROACAST_PQR_ASESORA:
                        String jsonPQR = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonPQR!=null){
                            listaPQR = new Gson().fromJson(jsonPQR, ResponsePQR.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getPQR(new RequiredIdenty(cedula), TAG, BROACAST_PQR_ASESORA);
    }

}
