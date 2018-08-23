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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.ImageZoomActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Redimir2;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_response_api.ListPremios;
import com.dupreincaperu.dupree.mh_response_api.Premios;
import com.dupreincaperu.dupree.mh_response_api.RequiredRedimirPremios;
import com.dupreincaperu.dupree.mh_response_api.SendPremios;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incentivos_Redimir_Fragment extends Fragment {

    private final String TAG = "Incnt_Redimir_Frag";
    private final String BROACAST_REDIMIR_OBTAIN = "broadcast_redimir_obtaint";
    private final String BROACAST_REDIMIR_SENDED = "broadcast_redimir_sended";
    private final String BROACAST_S_DIALOG_ADD_PREMIO = "broadcast_agregar_premio";
    private final String BROACAST_S_DIALOG_REMOVER_PREMIO = "broadcast_remover_premio";

    private final int ADD=0;
    private final int REMOVE=1;
    private final int INCREASE=2;
    private final int DECREASE=3;

    private List<Premios> listPremios, listFilter;
    private MH_Adapter_Redimir2 adapter_redimir;

    private int oldPuntos=0;
    public Incentivos_Redimir_Fragment() {
        // Required empty public constructor
    }

    ListPremios resultPremios;
    TextView tvTotalPuntos;
    Button btnRedimir;
    List<SendPremios> sendPremiosList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_incent_redimir, container, false);
        // Inflate the layout for this fragment

        RecyclerView rcvRedimir = v.findViewById(R.id.rcvRedimir);
        rcvRedimir.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvRedimir.setHasFixedSize(true);

        sendPremiosList = new ArrayList<>();
        listFilter = new ArrayList<>();
        listPremios = new ArrayList<>();
        adapter_redimir = new MH_Adapter_Redimir2(listPremios, listFilter, getActivity());
        rcvRedimir.setAdapter(adapter_redimir);

        adapter_redimir.setRVOnImageClickListener(new MH_Adapter_Redimir2.ImageClickListener() {
            @Override
            public void onClickImage(View v, int position) {
                Log.e(TAG,"adapter_redimir.setRVOnItemClickListener");
                gotoZoomImage(listFilter.get(position).getImagen());
            }
        });

        adapter_redimir.setRVOnItemClickListener(new MH_Adapter_Redimir2.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
                //gotoZoomImage(listPremios.get(position).getImagen());
            }
        });

        adapter_redimir.setRVOnDecreaseClickListener(new MH_Adapter_Redimir2.DecreaseClickListener() {
            @Override
            public void onDecreaseClick(View v, int position) {
                Log.e(TAG,"onDecreaseClick, pos:  "+position);
                decreaseCart(position);
            }
        });

        adapter_redimir.setRVOnIncreaseClickListener(new MH_Adapter_Redimir2.IncreaseClickListener() {
            @Override
            public void onIncreaseClick(View v, int position) {
                Log.e(TAG,"onIncreaseClick, pos:  "+position);
                if(isAvailablePts(position)) {
                    increaseCart(position);
                }
            }
        });

        adapter_redimir.setRVOnAddCartClickListener(new MH_Adapter_Redimir2.AddCartClickListener() {
            @Override
            public void onAddCartClick(View v, int position) {
                //verifica si quiere agregar
                if(isAvailablePts(position)) {
                    testAddCart();
                }
                //listFilter.get(position).setInTheCart(true);// se habilita controles mas incremento y decremento
                //listFilter.get(position).setCantidad(1);// se predetermina cantidad en 1
                //adapter_catalogo.notifyItemChanged(position);
            }
        });

        tvTotalPuntos = v.findViewById(R.id.tvTotalPuntos);
        tvTotalPuntos.setText("");
        btnRedimir = v.findViewById(R.id.btnRedimir);
        btnRedimir.setOnClickListener(mListenerClick);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        enableButton(false);
        checkPuntos();

        return v;
    }

    private void checkPuntos(){
        new Http(getActivity()).getRedimirPremios(TAG, BROACAST_REDIMIR_OBTAIN);
    }
    private void gotoZoomImage(String urlImage){
        Intent intent = new Intent(getActivity(), ImageZoomActivity.class);
        intent.putExtra(ImageZoomActivity.URL_IMAGE, urlImage);
        startActivity(intent);
    }

    View.OnClickListener mListenerClick =
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.btnRedimir:
                            sendPremios();
                            break;
                    }
                }
            };


    public void testAddCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.agregar), getString(R.string.desea_agregar_premio));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_ADD_PREMIO);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }


    private void sendPremios(){
        new Http(getActivity()).redimirPremios(new RequiredRedimirPremios(sendPremiosList), TAG, BROACAST_REDIMIR_SENDED);
    }

    public void testRemoveCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.remover), getString(R.string.desea_remover_premio));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_REMOVER_PREMIO);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
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
                    case BROACAST_REDIMIR_OBTAIN:
                        String jsonRedimir = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonRedimir!=null){
                            resultPremios = new Gson().fromJson(jsonRedimir, ListPremios.class);
                            updateView();
                        }
                        break;
                    case BROACAST_S_DIALOG_REMOVER_PREMIO:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVER_PREMIO).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            removeCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVER_PREMIO).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;


                    //Datos personales
                    case BROACAST_S_DIALOG_ADD_PREMIO:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_PREMIO).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            addCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_PREMIO).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;

                    case BROACAST_REDIMIR_SENDED:
                        String result = intent.getStringExtra(Http.BROACAST_DATA);
                        if(result!=null) {
                            msgToast(result);
                            checkPuntos();//actualiza la data para verificar cambios

                        }
                        break;
                }
            }
        }
    }

    boolean quitar=false;
    private void updateView(){
        listPremios.clear();
        listFilter.clear();
        listPremios.addAll(resultPremios.getPremios());
        listFilter.addAll(listPremios);

        tvTotalPuntos.setText(String.valueOf(resultPremios.getPuntos_premio()).concat(" pts."));

        if(!quitar){
            quitar=true;
            //resultPremios.setPuntos_premio(1000);
            tvTotalPuntos.setText(String.valueOf(resultPremios.getPuntos_premio()).concat(" pts."));
        }

        oldPuntos = resultPremios.getPuntos_premio();
        //tvTotalPuntos.setText("hhh");
        enableButton(false);
        adapter_redimir.notifyDataSetChanged();
    }

    private void enableButton(boolean isEnable){
        btnRedimir.setEnabled(isEnable);
        btnRedimir.setBackground(isEnable ?
                getResources().getDrawable(R.drawable.rounded_background_blue) :
                getResources().getDrawable(R.drawable.rounded_background_gray)
        );
    }


    private void decreaseCart(int position){
        if(position>-1) {
            int cantidad = Integer.parseInt(listFilter.get(position).getCantidad());
            if (cantidad > 0) {
                if (cantidad == 1) {
                    testRemoveCart();//pregunta si quiere eliminar
                } else {
                    listFilter.get(position).setCantidad(String.valueOf(cantidad - 1));
                    updatePuntos(position, DECREASE);
                    adapter_redimir.notifyItemChanged(position);
                }
                //if(cantidad==0 ){
                //    listFilter.get(position).setInTheCart(false);// se DEShabilita controles mas incremento y decremento
                //}
            }
        }
    }

    private void increaseCart(int position) {
        if(position>-1) {
            listFilter.get(position).setCantidad(String.valueOf(Integer.parseInt(listFilter.get(position).getCantidad()) + 1));
            updatePuntos(position, INCREASE);
            adapter_redimir.notifyItemChanged(position);
        }
    }

    public void addCart(){
        int position=adapter_redimir.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1) {

            listFilter.get(position).setInTheCart(true);// se habilita controles mas incremento y decremento
            listFilter.get(position).setCantidad("1");// se predetermina cantidad en 1
            updatePuntos(position, ADD);
            adapter_redimir.notifyItemChanged(position);
        }
    }

    private void updatePuntos(int position, int action){
        if(position>-1) {
            int restante = resultPremios.getPuntos_premio();
            int puntosPremio = Integer.parseInt(listFilter.get(position).getPuntos());

            if (action == ADD || action == INCREASE) {
                restante = restante - puntosPremio;
            } else if (action == REMOVE || action == DECREASE) {
                restante = restante + puntosPremio;
            }


            resultPremios.setPuntos_premio(restante);
            tvTotalPuntos.setText("Dispone de: ".concat(String.valueOf(restante)).concat(" pts."));

            enableButton(oldPuntos!=restante);

            updateListToSend(action, new SendPremios(listFilter.get(position).getCodigo(),listFilter.get(position).getCantidad()));
        }
    }

    private void updateListToSend(int action, SendPremios sendPremios){
        int index = indexByCode(sendPremios.getCodigo());

        int cantidad=-1;
        if(index >- 1) {
            cantidad = Integer.parseInt(sendPremiosList.get(index).getCantidad());
        }

        switch (action) {
            case ADD:
                sendPremiosList.add(sendPremios);
                break;
            case REMOVE:
                if(index > -1 && index < sendPremiosList.size())
                    sendPremiosList.remove(index);
                break;
            case INCREASE:
                if( cantidad != -1 ) {
                    cantidad++;
                    sendPremiosList.get(index).setCantidad(String.valueOf(cantidad));
                }
                break;
            case DECREASE:
                if( cantidad != -1 ) {
                    cantidad--;
                    sendPremiosList.get(index).setCantidad(String.valueOf(cantidad));
                }
                break;
        }
    }

    private int indexByCode(String code){
        for(int i=0; i< sendPremiosList.size(); i++){
            if(code.equals(sendPremiosList.get(i).getCodigo())){
                return i;
            }
        }

        return -1;
    }

    public void removeCart(){
        int position=adapter_redimir.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1) {
            listFilter.get(position).setInTheCart(false);// se habilita controles mas incremento y decremento
            listFilter.get(position).setCantidad("0");// se predetermina cantidad en 1
            updatePuntos(position, REMOVE);
            adapter_redimir.notifyItemChanged(position);
        }
    }

    private boolean isAvailablePts(int position){
        //.p    `1int position=adapter_redimir.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1) {
            int puntos = Integer.parseInt(listFilter.get(position).getPuntos());
            int restante = resultPremios.getPuntos_premio();
            if(puntos>restante){
                msgToast("No tiene puntos disponibles");
                return false;
            }
            return true;
        }
        return false;
    }

    private void msgToast(String msg){
        Log.e(TAG + "Toast", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
    
}
