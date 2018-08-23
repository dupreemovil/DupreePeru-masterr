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
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_cdr;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_response_api.ListaProductos;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.ProductosPorTipo;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pedidos_EdoPedido_Fragment extends Fragment {

    private final String TAG = "Pedidos_EdoPedido_Frag";
    private final String BROACAST_EDO_PEDIDO_ASESORA = "broadcast_edo_pedido_asesora";
    private TextView tvCamp;

    private MH_Adapter_cdr adapter_cdr;
    private ListaProductos listProducts, listPaquetones, listOfertas, listFilter;

    public Pedidos_EdoPedido_Fragment() {
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
        View v = inflater.inflate(R.layout.fragment_pedidos_edo_pedido, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        RecyclerView rcvEdoPedido = v.findViewById(R.id.rcvEdoPedido);
        rcvEdoPedido.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvEdoPedido.setHasFixedSize(true);


        listProducts = new ListaProductos();
        listPaquetones = new ListaProductos();
        listOfertas = new ListaProductos();
        listFilter = new ListaProductos();

        //listCDR = getCDR();
        //listFilter.addAll(listCDR);
        //adapter_cdr = new MH_Adapter_cdr(listCDR, listFilter, getActivity());
        rcvEdoPedido.setAdapter(adapter_cdr);

        //tvCamp = v.findViewById(R.id.tvCamp);
        //tvCamp.setText("CAMPAÑA: ".concat(faltantesHttp.getCampana()));

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkEdoPedido();


        return v;
    }

    private void checkEdoPedido(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        cardViewBackGround.setVisibility(View.INVISIBLE);
        //listProducts.clear();
        //listPaquetones.clear();
        //listOfertas.clear();
        //listFilter.clear();
        /*listProducts.addAll(productosPorTipo.getProductos());
        listPaquetones.addAll(listaCDR.getTable());
        listOfertas.addAll(listaCDR.getTable());
        listFilter.addAll(listaCDR.getTable());

        tvNombreAsesora.setText(listaCDR.getAsesora());

        adapter_cdr.notifyDataSetChanged();*/
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

    ProductosPorTipo productosPorTipo;
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
                    case BROACAST_EDO_PEDIDO_ASESORA:
                        String jsonProductByType = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonProductByType!=null){
                            productosPorTipo = new Gson().fromJson(jsonProductByType, ProductosPorTipo.class);
                            updateView();
                        }
                        break;

                }
            }
        }
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getEstadoPedidos(new RequiredIdenty(cedula), TAG, BROACAST_EDO_PEDIDO_ASESORA);
    }

}
