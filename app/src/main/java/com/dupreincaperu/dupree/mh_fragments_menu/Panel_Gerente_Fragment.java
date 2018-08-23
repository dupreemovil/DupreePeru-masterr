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

import com.dupreincaperu.dupree.MenuActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Campana;
import com.dupreincaperu.dupree.mh_response_api.PanelGteDetail;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_PanelGte;
import com.dupreincaperu.dupree.mh_dialogs.ListString;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredCampana;
import com.dupreincaperu.dupree.mh_response_api.ResponsePanelGte;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Panel_Gerente_Fragment extends Fragment {

    private final String TAG = "Panel_Gerente_Fragment";
    public static  final String BROACAST_GRTE_TYPE_LIST_CAMP="grte_type_list_camp";
    public static  final String BROACAST_GRTE_TYPE_LIST_CAMP_HTTP="grte_type_list_camp_http";
    public static  final String BROACAST_GRTE_TYPE_LIST_DETAIL_CAMP_HTTP="grte_type_list_detail_camp_http";

    private TextView txtSpnCamp;

    List<Campana> campHttp;
    List<PanelGteDetail> panelGteDetails;

    FloatingTextButton fabMessages;

    private MH_Adapter_PanelGte adapter_panelGte;
    public Panel_Gerente_Fragment() {
        // Required empty public constructor
    }


    RecyclerView rcvPanelGrnte;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_panel_gerente, container, false);
        rcvPanelGrnte = v.findViewById(R.id.rcvPanelGrnte);
        rcvPanelGrnte.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rcvPanelGrnte.setHasFixedSize(true);

        fabMessages = v.findViewById(R.id.fabMessages);

        fabMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MenuActivity) getActivity()).gotoMessages();
            }
        });

        campHttp = new ArrayList<>();
        panelGteDetails =new ArrayList<>();

        adapter_panelGte = new MH_Adapter_PanelGte(panelGteDetails, getActivity());
        rcvPanelGrnte.setAdapter(adapter_panelGte);

        txtSpnCamp = v.findViewById(R.id.txtSpnCamp);
        txtSpnCamp.setOnClickListener(mListenerClick);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkCampana();
        checkDetailCampana();

        return v;
    }



    View.OnClickListener mListenerClick =
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.txtSpnCamp:
                            showList(TAG, BROACAST_GRTE_TYPE_LIST_CAMP, getString(R.string.campana_title), getNameCampana(campHttp), txtSpnCamp.getText().toString());
                            break;
                    }
                }
            };

    public void showList(String idFragment, String object, String title, List<String> data, String itemSelected){
        ListString dialogList = new ListString();
        dialogList.loadData(idFragment, object, title, data, itemSelected);
        dialogList.show(getActivity().getSupportFragmentManager(),"mDialog");
    }


    public List<Campana> extractListCampana(String data){
        Type listType = new TypeToken<ArrayList<Campana>>(){}.getType();
        return new Gson().fromJson(data, listType);
    }

    private void addCampana(String jsonCampana){
        campHttp.clear();
        campHttp.add(new Campana("0", "-- útima --"));
        if(jsonCampana!=null)
            campHttp.addAll(extractListCampana(jsonCampana));
    }

    public void checkCampana(){
        String jsonCampana = mPreferences.getJSON_Campana(getActivity());
        if(jsonCampana!=null){
            addCampana(jsonCampana);
        } else {
            new Http(getActivity()).getCampanas(TAG, BROACAST_GRTE_TYPE_LIST_CAMP_HTTP);
        }
    }

    public List<PanelGteDetail> extractListDetailCampana(String data){
        Type listType = new TypeToken<ArrayList<PanelGteDetail>>(){}.getType();
        return new Gson().fromJson(data, listType);
    }

    private void addDetailCampana(String jsonDetailCampana){
        ResponsePanelGte responsePanelGte = new Gson().fromJson(jsonDetailCampana, ResponsePanelGte.class);

        panelGteDetails.clear();
        //campHttp.add(new Campana("0", "-- útima --"));
        if(jsonDetailCampana!=null) {
            //panelGteDetails.addAll(extractListDetailCampana(jsonDetailCampana));
            panelGteDetails.addAll(responsePanelGte.getListDetail().getPanelGteDetails());

            if(responsePanelGte.getListDetail().getCantidad_mensajes()!=null)
                fabMessages.setTitle(responsePanelGte.getListDetail().getCantidad_mensajes()+" Mensajes");

            adapter_panelGte.notifyDataSetChanged();
        }
    }

    public void  checkDetailCampana(){
        String jsonDetailCampana = mPreferences.getJSON_DetailCampana(getActivity());
        if(jsonDetailCampana!=null){
            addDetailCampana(jsonDetailCampana);
        } else {
            obtainDetailCamp("0");
        }
    }

    private void obtainDetailCamp(String campana){
        new Http(getActivity()).getDetailCampanas(new RequiredCampana(campana), TAG, BROACAST_GRTE_TYPE_LIST_DETAIL_CAMP_HTTP);
    }

    public  List<Campana> getCampanaHttp(){
        List<Campana> campanaList = new ArrayList<>();

        /*
        campanaList.add(new Campana("201712","201712"));
        campanaList.add(new Campana("201711","201711"));
        campanaList.add(new Campana("201710","201710"));
        campanaList.add(new Campana("201709","201709"));
        campanaList.add(new Campana("201708","201708"));
        campanaList.add(new Campana("201707","201707"));
        */

        return campanaList;
    }

    public List<String> getNameCampana(List<Campana> campHttp){
        List<String> respose = new ArrayList<>();

        for(int i=0; i<campHttp.size(); i++){
            respose.add(campHttp.get(i).getNombre());
        }
        return respose;
    }


    /*
    public List<PanelGteDetail> getPanel(){
        List<PanelGteDetail> respose = new ArrayList<>();

        respose.add(new PanelGteDetail("VENTA TOTAL","8591453","11.37%","faro_rojo.png"));
        respose.add(new PanelGteDetail("% CARTERA 21D","10618975","100%","faro_verde.png"));
        respose.add(new PanelGteDetail("% CARTERA 31D","10618975","100%","faro_rojo.png"));
        respose.add(new PanelGteDetail("PEDIDOS","37","9.46%","faro_rojo.png"));
        respose.add(new PanelGteDetail("INGRESOS","0","0%","faro_rojo.png"));
        respose.add(new PanelGteDetail("REINGRESOS","0","0%","faro_verde.png"));
        respose.add(new PanelGteDetail("% CONSECUTIVIDAD","82% Estimado","7.75%","faro_rojo.png"));
        respose.add(new PanelGteDetail(" %FALTANTE REAL","336539","3.92%","faro_verde.png"));

        return respose;
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

    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {
                    //Datos personales
                    case BROACAST_GRTE_TYPE_LIST_CAMP:
                        Log.i(TAG, "BROACAST_GRTE_TYPE_LIST_CAMP");
                        String campana = intent.getStringExtra(ListString.BROACAST_DATA);
                        if(campana!=null) {
                            txtSpnCamp.setText(campana);
                            obtainDetailCamp(campana);
                        }
                        break;
                    case BROACAST_GRTE_TYPE_LIST_CAMP_HTTP:
                        Log.i(TAG, "BROACAST_GRTE_TYPE_LIST_CAMP_HTTP");
                        addCampana(intent.getStringExtra(Http.BROACAST_DATA));
                        break;
                    case BROACAST_GRTE_TYPE_LIST_DETAIL_CAMP_HTTP:
                        Log.i(TAG, "BROACAST_GRTE_TYPE_LIST_DETAIL_CAMP_HTTP");
                        addDetailCampana(intent.getStringExtra(Http.BROACAST_DATA));
                        break;
                }
            }
        }
    }


}
