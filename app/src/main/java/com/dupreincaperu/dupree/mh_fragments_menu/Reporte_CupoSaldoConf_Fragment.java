package com.dupreincaperu.dupree.mh_fragments_menu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.CupoSaldoConf;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_CupoSaldoConf_Fragment extends Fragment {

    private final String TAG = "CupoSaldoConf_Fragment";
    private final String BROACAST_CUPSALCONF_CONSULTA = "broadcast_cupsalconf_consulta";

    TextView tvCupo, tvSaldo, tvConferencia, tvNameAsesora;
    CardView cardViewBackGround, cardViewBackGround2;
    public Reporte_CupoSaldoConf_Fragment() {
        // Required empty public constructor
    }

    TextView tvNombreAsesora;
    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte_cupo_saldo_conf, container, false);

        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        cardViewBackGround2 = v.findViewById(R.id.cardViewBackGround2);
        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        cardViewBackGround2.setVisibility(View.INVISIBLE);
        tvCupo = v.findViewById(R.id.tvCupo);

        tvSaldo = v.findViewById(R.id.tvSaldo);
        tvConferencia = v.findViewById(R.id.tvConferencia);
        tvNameAsesora = v.findViewById(R.id.tvNameAsesora);

        /*cupoSaldoConf = getCupoSaldoConf();


        tvCupo.setText(cupoSaldoConf.getCupo());
        tvSaldo.setText("$".concat(cupoSaldoConf.getSaldo()));
        tvConferencia.setText(cupoSaldoConf.getConf_vent());
        */

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkCupoSaldoCOnf();

        return v;
    }

    private void checkCupoSaldoCOnf(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    /*
    public CupoSaldoConf getCupoSaldoConf(){
        CupoSaldoConf cupoSaldoConf;

        cupoSaldoConf = new CupoSaldoConf("$840,000","199,112","No olvide asistir a su proxima conferencia de ventas el dia 23 AGOSTO 10:00 A.M En SALON DE EVENTOS ACAPULCO - DIAGONAL 50SUR NO51-38 - .. Informacion adicional con su Gerente de Zona JEIMY AMANDA ROMERO Telefono 3208651428 Contacto (7797199)","MARIA DEL PILAR GARCIA CHALARCA / ZONA-117");

        return cupoSaldoConf;
    }
*/

    private void updateView(){
        cardViewBackGround.setVisibility(View.VISIBLE);
        cardViewBackGround2.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(cupoSaldoConf.getAsesora());

        tvCupo.setText(cupoSaldoConf.getCupo());
        tvSaldo.setText(cupoSaldoConf.getSaldo());
        tvConferencia.setText(cupoSaldoConf.getConf_vent());
        //tvNameAsesora.setText(cupoSaldoConf.getAsesora());
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

    public CupoSaldoConf extractCupSalConf(String data){
        return new Gson().fromJson(data, CupoSaldoConf.class);
    }




    CupoSaldoConf cupoSaldoConf;
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
                    case BROACAST_CUPSALCONF_CONSULTA:
                        String jsonCupoSalConf = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonCupoSalConf!=null){
                            cupoSaldoConf = new Gson().fromJson(jsonCupoSalConf, CupoSaldoConf.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getCupoSaldoCOnf(new RequiredIdenty(cedula),TAG,BROACAST_CUPSALCONF_CONSULTA);
    }

}
