package com.dupreincaperu.dupree.mh_fragments_menu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.dupreincaperu.dupree.mh_response_api.ListaPuntos;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.PtosByCamp;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_puntos_asesora;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incentivos_ConsultaPtos_Fragment extends Fragment {


    private final String TAG = "Reporte_CDR_Fragment";
    private final String BROACAST_PUNTOS_ASESORA = "broadcast_puntos_asesora";
    private TextView tvCamp;

    private MH_Adapter_puntos_asesora adapter_puntos_asesora;
    private List<PtosByCamp> listPtosAsesora, listFilter;

    public Incentivos_ConsultaPtos_Fragment() {
        // Required empty public constructor
    }

    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    private CircleImageView profile_image;
    private TextView nameAsesora, ptsEfectivos, ptsRedimidos, ptsDisponibles, ptsPendientes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_incent_consulta_ptos, container, false);

        profile_image = v.findViewById(R.id.profile_image);
        profile_image.setVisibility(View.INVISIBLE);

        nameAsesora = v.findViewById(R.id.nameAsesora);
        nameAsesora.setText("");
        ptsEfectivos = v.findViewById(R.id.ptsEfectivos);
        ptsEfectivos.setText("");
        ptsRedimidos = v.findViewById(R.id.ptsRedimidos);
        ptsRedimidos.setText("");
        ptsDisponibles = v.findViewById(R.id.ptsDisponibles);
        ptsDisponibles.setText("");
        ptsPendientes = v.findViewById(R.id.ptsPendientes);
        ptsPendientes.setText("");

        RecyclerView rcvPuntosAsesora = v.findViewById(R.id.rcvPuntosAsesora);
        rcvPuntosAsesora.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvPuntosAsesora.setHasFixedSize(true);

        listPtosAsesora = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listPtosAsesora = getPuntosAsesora();
        listFilter.addAll(listPtosAsesora);


        adapter_puntos_asesora = new MH_Adapter_puntos_asesora(listPtosAsesora, listFilter, getActivity());
        rcvPuntosAsesora.setAdapter(adapter_puntos_asesora);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkPuntos();

        return v;
    }

    private void checkPuntos(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        listPtosAsesora.clear();
        listFilter.clear();
        listPtosAsesora.addAll(listaPuntos.getList());
        listFilter.addAll(listaPuntos.getList());

        profile_image.setVisibility(View.VISIBLE);
        nameAsesora.setText(listaPuntos.getResume().getAsesora());

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);

        ptsEfectivos.setText("Efectivos: ".concat(formatter.format(listaPuntos.getResume().getEfectivos())));
        ptsRedimidos.setText("Redimidos: ".concat(formatter.format(listaPuntos.getResume().getRedimidos())));
        ptsDisponibles.setText("Disponibles: ".concat(formatter.format(listaPuntos.getResume().getDisponibles())));
        ptsPendientes.setText("Pendientes: ".concat(formatter.format(Float.parseFloat(listaPuntos.getResume().getPendientes_Pago()))));

        adapter_puntos_asesora.notifyDataSetChanged();
    }

    /*
    public List<PtosByCamp> getPuntosAsesora(){
        List<PtosByCamp> ptosByCamps = new ArrayList<>();

        ptosByCamps.add(new PtosByCamp("201712","0","0","0","0","0","0","0","0","0","248","0","PEN"));
        ptosByCamps.add(new PtosByCamp("201711","573","0","0","573","0","0","0","0","0","0","490","OK "));
        ptosByCamps.add(new PtosByCamp("201710","278","0","0","278","0","0","0","0","0","0","0","OK "));
        ptosByCamps.add(new PtosByCamp("201709","234","0","0","234","0","0","0","0","0","0","0","OK "));

        return ptosByCamps;
    }
    */

    public void filterCDR(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);
        adapter_puntos_asesora.getmFilter().filter(textFilter);
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

    ListaPuntos listaPuntos;
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
                    case BROACAST_PUNTOS_ASESORA:
                        String jsonPuntos = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonPuntos!=null){
                            listaPuntos = new Gson().fromJson(jsonPuntos, ListaPuntos.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getPuntosAsesora(new RequiredIdenty(cedula), TAG, BROACAST_PUNTOS_ASESORA);
    }
}
