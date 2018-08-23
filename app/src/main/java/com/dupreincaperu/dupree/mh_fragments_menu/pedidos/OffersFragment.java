package com.dupreincaperu.dupree.mh_fragments_menu.pedidos;


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
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Offers;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_fragments_menu.Pedidos_Hacer_Fragment;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
import com.dupreincaperu.dupree.mh_response_api.Oferta;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment {

    private final String TAG = "OffersFragment";
    public static  final String BROACAST_DATA="broacast_data";
    private final String BROACAST_S_DIALOG_ADD_PRODUCTS = "broadcast_simple_dialog_add_products";
    private final String BROACAST_S_DIALOG_REMOVE_PRODUCTS = "broadcast_simple_dialog_remove_products";

    RealmResults<Oferta> querycat;
    private List<Oferta> listFilterOffers;//, listSelected;
    private MH_Adapter_Offers adapter_catalogo;
    private Realm realm;

    private boolean enable=false;


    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        Bundle args = new Bundle();
        
        OffersFragment fragment = new OffersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products, container, false);

        realm = Realm.getDefaultInstance();

        RecyclerView rcvCatalogo = v.findViewById(R.id.rcvProductos);
        rcvCatalogo.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rcvCatalogo.setHasFixedSize(true);


        //listPremios = new ArrayList<>();
        listFilterOffers = new ArrayList<>();
        adapter_catalogo = new MH_Adapter_Offers(listFilterOffers, getActivity());

        rcvCatalogo.setAdapter(adapter_catalogo);

        adapter_catalogo.setRVOnItemClickListener(new MH_Adapter_Offers.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
                //gotoZoomImage(listPremios.get(position).getImagen());
            }
        });

        adapter_catalogo.setRVOnAddCartClickListener(new MH_Adapter_Offers.AddCartClickListener() {
            @Override
            public void onAddCartClick(View v, int position) {
                //verifica si quiere agregar
                Log.e(TAG,"onAddCartClick: "+String.valueOf(position));
                testAddCart();
                //listFilterOffers.get(position).setInTheCart(true);// se habilita controles mas incremento y decremento
                //listFilterOffers.get(position).setCantidad(1);// se predetermina cantidad en 1
                //adapter_catalogo.notifyItemChanged(position);
            }
        });

        adapter_catalogo.setRVOnDecreaseClickListener(new MH_Adapter_Offers.DecreaseClickListener() {
            @Override
            public void onDecreaseClick(View v, int position) {
                Log.e(TAG,"onDecreaseClick, pos:  "+position);
                int cantidad =listFilterOffers.get(position).getCantidad();
                if(cantidad>0) {
                    if(cantidad==1){
                        testRemoveCart();//pregunta si quiere eliminar
                    } else {
                        decreaseCart(cantidad);
                    }
                    //if(cantidad==0 ){
                    //    listFilterOffers.get(position).setInTheCart(false);// se DEShabilita controles mas incremento y decremento
                    //}
                }
            }
        });

        adapter_catalogo.setRVOnIncreaseClickListener(new MH_Adapter_Offers.IncreaseClickListener() {
            @Override
            public void onIncreaseClick(View v, int position) {
                Log.e(TAG,"onIncreaseClick, pos:  "+position);
                increaseCart();

            }
        });

        adapter_catalogo.setRVOnAddEditableClickListener(new MH_Adapter_Offers.AddEditableClickListener() {
            @Override
            public void onAddEditableClick(boolean isEditable) {
                Log.e(TAG,"onAddEditableClick, status:  "+isEditable);
                publishResultEditable(
                        Pedidos_Hacer_Fragment.TAG,
                        Pedidos_Hacer_Fragment.BROACAST_CHANGE_OFFERS_EDIT,
                        isEditable ? Pedidos_Hacer_Fragment.BROACAST_CHANGE_EDIT_YES : Pedidos_Hacer_Fragment.BROACAST_CHANGE_EDIT_NOT
                );
            }
        });

        localBroadcastReceiver = new LocalBroadcastReceiver();

        //QUIZAS INICESARIO, PARA NO DEJAR LA PANTALLA VACIA
        filterOffersDB("");//mostrar todo el catalogo

        return v;
    }

    //antes de enviar
    public boolean validate(){
        if(listFilterOffers.size()>0){
            for(int i=0;i<listFilterOffers.size(); i++){
                if (listFilterOffers.get(i).getCantidad() != listFilterOffers.get(i).getCantidadServer())
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void testAddCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.agregar), getString(R.string.desea_agregar_pedido));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_ADD_PRODUCTS);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void testRemoveCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.remover), getString(R.string.desea_remover_pedido));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_REMOVE_PRODUCTS);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    /*
    public void filterCatalogo(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.i(TAG,"filterCatalogo()");
        filterOffersDB(textFilter);
    }*/


    public void refresshCatalogo(RealmResults<Catalogo> querycat){
        Log.i(TAG,"refresshCatalogo()");
        //listFilterOffers.addAll(querycat);
        adapter_catalogo.notifyDataSetChanged();
        updateTotal();
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
                    case BROACAST_S_DIALOG_ADD_PRODUCTS:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_PRODUCTS).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            addCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_PRODUCTS).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_S_DIALOG_REMOVE_PRODUCTS:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_PRODUCTS).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            removeCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_PRODUCTS).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                }
            }
        }
    }

    private void increaseCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "increaseCart(), pos: "+position);
        if(position>-1 && position<listFilterOffers.size()) {
            realm.beginTransaction();
            try {
                listFilterOffers.get(position).setCantidad(listFilterOffers.get(position).getCantidad()+1);
                adapter_catalogo.notifyItemChanged(position);
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "increaseCart... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    private void decreaseCart(int cantidad){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "decreaseCart(), pos: "+position);
        if(position>-1 && position<listFilterOffers.size()) {
            realm.beginTransaction();
            try {
                listFilterOffers.get(position).setCantidad(cantidad - 1);
                adapter_catalogo.notifyItemChanged(position);
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "decreaseCart... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    private void addCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilterOffers.size()) {
            realm.beginTransaction();
            try {
                listFilterOffers.get(position).setCantidad(1);// se predetermina cantidad en 1
                adapter_catalogo.notifyItemChanged(position);
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "addCart... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    private void updateTotal(){
        publishResultEditable(Pedidos_Hacer_Fragment.TAG, Pedidos_Hacer_Fragment.BROACAST_UPDATE_TOTAL,"data");
    }

    private void removeCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1 && position<listFilterOffers.size()) {
            realm.beginTransaction();
            try {
                listFilterOffers.get(position).setCantidad(0);// se predetermina cantidad en 1
                adapter_catalogo.notifyItemChanged(position);
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "removeCart... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    public void sincOfertasDB(List<Oferta> listServer, boolean edit){
        Log.v(TAG,"sincOfertasDB... ---------------sincOfertasDB--------------");
        // si no hay ofertas, se agrega a la base de datos.....
        if(listServer.size()>0) {

            querycat = realm.where(Oferta.class).findAll();

            if (querycat.size() < 1) {
                //si no hay nada local, se sincroniza cantidad server y local iguales y se escribe toodo tal cual localmente
                for(int i=0; i<listServer.size();i++){
                    listServer.get(i).setCantidadServer(listServer.get(i).getCantidad());//se igualan las cantidades local y del server
                }

                writeOfertas(listServer);//si no hay ofertas, se agregan
            } else {
                updateOffers(listServer, edit);
                filterOffersDB("");
            }
        }
    }

    private void updateOffers(List<Oferta> listServer, boolean edit){
        // si hay tanto ofertas locales, como remotas...
        /// se da prioridad a las locales, eliminando las que ya no existan en la nueva lista consultada
        realm.beginTransaction();

        try {
            //listFilter.clear();
            for (int i = 0; i < listServer.size(); i++) {
                Oferta oferta = realm.where(Oferta.class).equalTo("id", listServer.get(i).getId()).findFirst();// se busca el id
                if(oferta==null){//si NO existe, se agrega
                    listServer.get(i).setCantidadServer(listServer.get(i).getCantidad());//se agrega pero sincronizada
                    realm.insertOrUpdate(listServer.get(i));
                } else {/// en caso de que exista
                    if(edit){//si esta en modo edicion, se prevalece la del server, de lo contrario queda la local
                        oferta.setCantidad(listServer.get(i).getCantidad());
                        oferta.setCantidadServer(listServer.get(i).getCantidad());
                    }
                }
            }

            for (int i = 0; i < listFilterOffers.size(); i++) {
                //se busca si hay algun elemento que dejo de estar disponible
                if(!listServer.contains(listFilterOffers.get(i))){///si no existe el elemento en la lista actual del server
                    listFilterOffers.get(i).deleteFromRealm();
                }
            }

            //listFilter.addAll(querycat);
            //adapter_cart.notifyDataSetChanged();
            realm.commitTransaction();

        } catch (Throwable e) {
            Log.v(TAG, "sincCatalogoDB... ---------------error--------------");
            if (realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }

    public void writeOfertas(final List<Oferta> listServer){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.insert(listServer);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v(TAG,"writeOfertas... ---------------ok--------------");
                Log.v(TAG, ": " + new Gson().toJson(listServer));
                filterOffersDB("");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG,"writeOfertas... ---------------error--------------");
                Log.e(TAG,"writeOfertas... "+error.getMessage());
                //realm.close();
            }
        });
    }

    public void filterOffersDB(final String textFilter){
        Log.e(TAG, "filterOffersDB()");
        if(showOffers) {
            Log.v(TAG, "filterOffersDB... ---------------filterOffersDB--------------");
            realm.beginTransaction();
            try {
                listFilterOffers.clear();

                String []fieldNames={"cantidad","cantidadServer"};
                Sort sort[]={Sort.DESCENDING,Sort.DESCENDING};

                querycat = realm.where(Oferta.class).contains("id", textFilter)
                        .findAllSorted(fieldNames, sort);
                if (querycat.size() > 0)
                    listFilterOffers.addAll(querycat);

                adapter_catalogo.notifyDataSetChanged();
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "filterOffersDB... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    public void ofertaEndviadoexitosamente(){
        if(showOffers) {
            Log.v(TAG, "ofertaEndviadoexitosamente... ---------------ofertaEndviadoexitosamente--------------");
            realm.beginTransaction();
            try {
                listFilterOffers.clear();


                String []fieldNames={"cantidad","cantidadServer"};
                Sort sort[]={Sort.DESCENDING,Sort.DESCENDING};

                querycat = realm.where(Oferta.class).contains("id", "")
                        .findAllSorted(fieldNames, sort);

                //querycat = realm.where(Oferta.class).findAllSorted("cantidad",Sort.DESCENDING);
                if (querycat.size() > 0) {
                    listFilterOffers.addAll(querycat);
                    for(int i=0; i<listFilterOffers.size();i++){
                        listFilterOffers.get(i).setCantidadServer(listFilterOffers.get(i).getCantidad());
                    }
                }

                adapter_catalogo.notifyDataSetChanged();
                updateTotal();
                realm.commitTransaction();
            } catch (Throwable e) {
                Log.v(TAG, "ofertaEndviadoexitosamente... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    public void deleteOferta(){
        Log.e(TAG, "deleteOferta2()");
        if(showOffers) {
            Log.v(TAG, "deleteOferta2... ---------------filterOdeleteOferta2ffersDB--------------");
            realm.beginTransaction();
            try {
                realm.delete(Oferta.class);
                realm.commitTransaction();

                filterOffersDB("");
            } catch (Throwable e) {
                Log.v(TAG, "deleteOferta2... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private boolean showOffers=false;
    public void setShowOffers(boolean showOffers) {
        this.showOffers = showOffers;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        adapter_catalogo.setEnable(enable);
        adapter_catalogo.notifyDataSetChanged();
        updateTotal();
    }

    public List<Oferta> getListFilterOffers() {
        return listFilterOffers;
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void publishResultEditable(String tagFragment, String objectFragment, String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));
    }

    public void clearEditable(){
        adapter_catalogo.clearEditable();
    }
}
