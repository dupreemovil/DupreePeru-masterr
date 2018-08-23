package com.dupreincaperu.dupree.mh_fragments_menu;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Retenido;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Retenidos;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_Retenidos_Fragment extends Fragment {

    private final String TAG = "Reporte_Retenidos_Frag";
    private final String BROACAST_S_DIALOG_CALL = "broadcast_simple_dialog_call";
    private final String BROACAST_RET_CONSULTA = "broadcast_ret_consulta";


    private static final int CONNECTION_REQUEST = 1;
    private static final int RC_CALL = 111;

    private List<Retenido> listRetenidos, listFilter;//, listSelected;
    private MH_Adapter_Retenidos adapter_retenidos;

    String phone;
    public Reporte_Retenidos_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte_retenidos, container, false);

        RecyclerView rcvRetenido = v.findViewById(R.id.rcvRetenido);
        rcvRetenido.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvRetenido.setHasFixedSize(true);

        listRetenidos = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listRetenidos = getRetenidos();
        listFilter.addAll(listRetenidos);
        adapter_retenidos = new MH_Adapter_Retenidos(listRetenidos, listFilter, getActivity());
        rcvRetenido.setAdapter(adapter_retenidos);

        adapter_retenidos.setRVOnCallClickListener(new MH_Adapter_Retenidos.CallClickListener() {
            @Override
            public void onCallClick(View v, int position) {
                phone = listFilter.get(position).getCelular();
                if(phone!= null && !phone.isEmpty() && !phone.equals("0")) {

                    testCall();
                }
            }
        });

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkRetenidos();
        return v;
    }

    public void checkRetenidos(){
        if(listRetenidos.size()<1){//OJO REFREZCAR CON PULL REFRESH
            new Http(getActivity()).getPedidosRetenidos(new RequiredIdenty(""),TAG,BROACAST_RET_CONSULTA);
        }
    }
    public void testCall(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.llamar), getString(R.string.desea_lammar));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_CALL);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void filterRetenidos(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);
        adapter_retenidos.getmFilter().filter(textFilter);
    }
    /*
    private List<Retenido> getRetenidos(){
        List<Retenido> listRetenidos = new ArrayList<>();

        listRetenidos.add(new Retenido("BOG2","142 ","20302797","ARIZA PEREZ FLOR MARIA","3123223948","12","$64,914","$69,544","$840,000","X"," "," "," "," "," ","$103,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","21094290","TORRES SUAREZ GLORIA MARIA","3175226426","35","$153,144","$52,383","$840,000","X"," "," "," "," "," ","$246,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","30346840","BONILLA RIVERA MARTHA","3138195912","5","$87,224","$0","$840,000"," "," "," "," "," ","X","$136,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","34329896","VILLALOBOS MUNOZ PAOLA ANDREA","3144748698","22","$191,955","$0","$480,020"," "," "," "," ","X"," ","$269,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","39530579","DUARTE AREVALO ANA ISABEL","3132207641","16","$95,164","$0","$480,000"," "," "," "," "," ","X","$151,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","39750408","RAMIREZ QUINONES MARCIA JACQUELINE","3223332218","60","$122,557","$0","$840,262"," "," "," "," "," ","X","$185,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","41211073","TORRES REY NERY","3212319889","0","$134,448","$0","$480,000"," "," "," "," ","X"," ","$203,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","41657974","HOLGUIN LOPEZ ANA RUTH","3112583466","13","$61,509","$0","$852,727"," "," "," ","X"," "," ","$95,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","46367109","HERNANDEZ MEDINA LUZ ANGELA","3012934485","6","$133,692","$125,846","$840,000","X"," "," "," "," "," ","$205,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","50892966","RAMOS AVILEZ LUZ DARY","3017194253","6","$221,212","$167,220","$840,000","X"," "," "," "," "," ","$348,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","51753824","GIL GAMEZ ELIZABETH","0         ","30","$151,842","$94,285","$840,000","X"," "," "," "," "," ","$238,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","51802289","VERA CASTILLO YENNY GICELA","3046302092","1","$111,339","$0","$600,009"," "," "," "," "," ","X","$174,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","51937041","CHARRIZ NARANJO BIRGELINA","3133949263","3","$19,492","$0","$840,096"," "," "," ","X"," "," ","$20,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52149701","NERY CARDENAS CLAUDIA LILIAN","3212095069","2","$194,277","$310,993","$720,000","X"," "," "," "," "," ","$296,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52353207","SEGURA OSPINA LISETH","3115680272","23","$77,518","$118,337","$840,000","X"," "," "," "," "," ","$104,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52446416","VERGARA FORERO ROSA MARILYN","3158423196","15","$132,431","$93,274","$840,000","X"," "," "," "," "," ","$197,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52561837","TAMAYO RUBIO MARGARITA MARIA","3194155727","18","$129,103","$193,974","$840,000","X"," "," "," "," "," ","$202,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52874327","PULIDO DEYSI LIZETH","3057465798","15","$220,242","$272,956","$1,217,979","X"," "," "," "," "," ","$296,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52880265","VARGAS BELTRAN ANDREA DEL PILAR","3133476905","51","$145,585","$39,980","$840,000","X"," "," "," "," "," ","$230,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","52890209","GAFARO RAMIREZ ANGELA LILIAN","3006037649","15","$137,557","$5,900","$840,000","X"," "," "," "," "," ","$200,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","60305647","PUERTO DIAZ NORALBA","3144550920","9","$259,360","$0","$848,060"," "," "," "," ","X"," ","$416,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","63492565","JAIMES GONZALEZ HILDA","3112677348","37","$107,683","$145,496","$920,010","X"," "," "," "," "," ","$169,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","65719018","BERNAL GARCIA MARIA PAULINA","3203466590","1","$134,362","$13,896","$600,000","X"," "," "," "," "," ","$202,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","1003746433","REYES GARZON EDNA ROCIO","3208851895","3","$0","$134,093","$840,000","X"," "," ","X"," "," ","$0"));
        listRetenidos.add(new Retenido("BOG2","142 ","1014273766","GARCIA GUEVARA MAYRA PAOLA","3142281943","22","$115,206","$0","$840,000"," "," "," "," "," ","X","$182,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","1022952353","ARANGO MARTINEZ OFELIA NAYIBE","3227166560","36","$151,784","$68,554","$840,000","X"," "," "," "," "," ","$231,000"));
        listRetenidos.add(new Retenido("BOG2","142 ","1023900539","MARTINEZ MURILLO CINDY PATRICIA","3127833598","2","$178,776","$358,677","$720,000","X"," "," "," "," "," ","$276,000"));

        return  listRetenidos;
    }
    */


    @AfterPermissionGranted(RC_CALL)
    private void callPhone() {
        //private void connect(int mode, String roomId, String nameCall, String numberCall, String idDevice) {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            makeCall();
        } else {
            EasyPermissions.requestPermissions(this, "Need some permissions", RC_CALL, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    private void makeCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
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

    public List<Retenido> extracttRetenidos(String data){
        Type listRetenido = new TypeToken<ArrayList<Retenido>>(){}.getType();
        return new Gson().fromJson(data, listRetenido);
    }

    private void addRetenidos(String jsonRetenidos){
        listRetenidos.clear();
        listFilter.clear();

        //campHttp.add(new Campana("0", "-- útima --"));
        if(jsonRetenidos!=null) {
            listRetenidos.addAll(extracttRetenidos(jsonRetenidos));
            listFilter.addAll(extracttRetenidos(jsonRetenidos));
            adapter_retenidos.notifyDataSetChanged();
        }
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
                    case BROACAST_S_DIALOG_CALL:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_CALL).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            callPhone();

                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_CALL).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_RET_CONSULTA:
                        String jsonRetenidos = intent.getStringExtra(Http.BROACAST_DATA);
                        addRetenidos(jsonRetenidos);
                        break;
                }
            }
        }
    }

}
