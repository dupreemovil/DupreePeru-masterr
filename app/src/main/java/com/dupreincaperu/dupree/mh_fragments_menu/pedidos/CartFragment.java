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

import com.dupreincaperu.dupree.mh_fragments_menu.Pedidos_Hacer_Fragment;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Catalogo;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
import com.dupreincaperu.dupree.mh_response_api.DetalleProductos;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private final String TAG = "ProductsFragment";
    public static  final String BROACAST_DATA="broacast_data";

    private final String BROACAST_S_DIALOG_ADD_CART = "broadcast_simple_dialog_add_cart";
    private final String BROACAST_S_DIALOG_REMOVE_CART = "broadcast_simple_dialog_remove_cart";

    RealmResults<Catalogo> querycat;

    private List<Catalogo> listFilterCart;//, listSelected;
    private MH_Adapter_Catalogo adapter_cart;

    private Realm realm;

    private boolean enable=true;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        Bundle args = new Bundle();

        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rcvCartProducts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        realm = Realm.getDefaultInstance();



        rcvCartProducts = v.findViewById(R.id.rcvCartProducts);
        rcvCartProducts.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvCartProducts.setHasFixedSize(true);



        //listPremios = new ArrayList<>();
        listFilterCart = new ArrayList<>();
        adapter_cart = new MH_Adapter_Catalogo(listFilterCart, getActivity());

        rcvCartProducts.setAdapter(adapter_cart);

        adapter_cart.setRVOnItemClickListener(new MH_Adapter_Catalogo.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
                //gotoZoomImage(listPremios.get(position).getImagen());
            }
        });

        adapter_cart.setRVOnAddCartClickListener(new MH_Adapter_Catalogo.AddCartClickListener() {
            @Override
            public void onAddCartClick(View v, int position) {
                //verifica si quiere agregar
                Log.e(TAG,"onAddCartClick: "+String.valueOf(position));
                testAddCart();
            }
        });

        adapter_cart.setRVOnDecreaseClickListener(new MH_Adapter_Catalogo.DecreaseClickListener() {
            @Override
            public void onDecreaseClick(View v, int position) {
                Log.e(TAG,"onDecreaseClick, pos:  "+position);
                int cantidad =listFilterCart.get(position).getCantidad();
                if(cantidad>0) {
                    if(cantidad==1){
                        testRemoveCart();//pregunta si quiere eliminar
                    } else {
                        decreaseCart(cantidad);
                    }
                    //if(cantidad==0 ){
                    //    listFilter.get(position).setInTheCart(false);// se DEShabilita controles mas incremento y decremento
                    //}
                }
            }
        });

        adapter_cart.setRVOnIncreaseClickListener(new MH_Adapter_Catalogo.IncreaseClickListener() {
            @Override
            public void onIncreaseClick(View v, int position) {
                Log.e(TAG,"onIncreaseClick, pos:  "+position);
                increaseCart();

            }
        });

        adapter_cart.setRVOnAddEditableClickListener(new MH_Adapter_Catalogo.AddEditableClickListener() {
            @Override
            public void onAddEditableClick(boolean isEditable) {
                Log.e(TAG,"onAddEditableClick, status:  "+isEditable);
                publishResultEditable(
                        Pedidos_Hacer_Fragment.TAG,
                        Pedidos_Hacer_Fragment.BROACAST_CHANGE_PRODUCTS_EDIT,
                        isEditable ? Pedidos_Hacer_Fragment.BROACAST_CHANGE_EDIT_YES : Pedidos_Hacer_Fragment.BROACAST_CHANGE_EDIT_NOT
                );
            }
        });



        localBroadcastReceiver = new LocalBroadcastReceiver();


        //si es gerente limpia lo seleccionado, luego filtra
        if(!getTypePerfil().getPerfil().equals(Perfil.ADESORA)){
            clearCartDB();
        } else {
            filterCartDB("");//mostrar todo el catalogo
        }


        //QUIZAS INICESARIO, PARA NO DEJAR LA PANTALLA VACIA






        return v;
    }

    private Perfil getTypePerfil(){
        String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());
        if(jsonPerfil != null){
            return new Gson().fromJson(jsonPerfil, Perfil.class);
        }
        msgToast("Hubo un problema desconocido, se recomienda cerrar sesión e iniciar nuevamente");
        return null;
    }

    //antes de enviar
    public boolean validate(){
        if(listFilterCart.size()>0){
            for(int i=0;i<listFilterCart.size(); i++){
                if (listFilterCart.get(i).getCantidad() != listFilterCart.get(i).getCantidadServer())
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
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_ADD_CART);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }


    public void testRemoveCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.remover), getString(R.string.desea_remover_pedido));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_REMOVE_CART);//para que sea diferente el broadcats de cada items, y pasar el filtro de repetidos
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }




    String textFilter="";
    public void filterCart(String textFilter){
        this.textFilter = textFilter;
        //adapter_catalogo.getmFilter().filter(textFilter);
        filterCartDB(textFilter);
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

    private static String intentRepeat="";
    private long lastTime=0;
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }

            if(Calendar.getInstance().getTimeInMillis()-lastTime<500) {// si no ha pasado medio segundo puede ser un rebote
                Log.e(TAG,"intentRepeat()");
                return;
            }

            lastTime=Calendar.getInstance().getTimeInMillis();

            /*
            if(intentRepeat.equals(intent.getExtras().toString())) {
                Log.e(TAG,"repeat: "+intent.getExtras().toString());
                return;
            }*/

            Log.e(TAG,"LocalBroadcastReceiver: " + intent.toString());

            intentRepeat=intent.getExtras().toString();
            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    //Datos personales
                    case BROACAST_S_DIALOG_ADD_CART:// se muestran unicamente los que tienen cntidades positiva, por ende no se puede agregar, solo eliminar
                        if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_CART).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            addCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_CART).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_S_DIALOG_REMOVE_CART:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_CART).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            removeCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_CART).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;

                }
            }
        }
    }











    public void pedidoEndviadoexitosamente(){
        Log.v(TAG, "pedidoEndviadoexitosamente... -----------------------------------");
        if(listFilterCart.size()>0) {
            try {
                realm.beginTransaction();
                for(int position=0; position<listFilterCart.size(); position++) {
                        //se edita para que la cantidad actual sea la enviada

                        listFilterCart.get(position).setCantidadServer(listFilterCart.get(position).getCantidad());
                }
                realm.commitTransaction();
                Log.v(TAG, "pedidoEndviadoexitosamente... --------------  OK  --------------");


                //adapter_cart.notifyDataSetChanged();// esto no actualiza a los que se eliminaron, se procedio a filtar nuevamente
                filterCart(textFilter);
            } catch (Throwable e) {
                Log.v(TAG, "pedidoEndviadoexitosamente... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                //break;
                throw e;
            }
        }
    }

    private void increaseCart(){
        int position=adapter_cart.getPosSelected();
        Log.i(TAG, "increaseCart(), pos: "+position);
        if(position>-1 && position<listFilterCart.size()) {
            realm.beginTransaction();
            try {
                listFilterCart.get(position).setCantidad(listFilterCart.get(position).getCantidad()+1);
                adapter_cart.notifyItemChanged(position);
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
        int position=adapter_cart.getPosSelected();
        Log.i(TAG, "decreaseCart(), pos: "+position);
        if(position>-1 && position<listFilterCart.size()) {
            realm.beginTransaction();
            try {
                listFilterCart.get(position).setCantidad(cantidad - 1);
                adapter_cart.notifyItemChanged(position);
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
        int position=adapter_cart.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilterCart.size()) {
            realm.beginTransaction();
            try {
                listFilterCart.get(position).setCantidad(1);// se predetermina cantidad en 1
                adapter_cart.notifyItemChanged(position);
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

    private void removeCart(){
        int position=adapter_cart.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1 && position<listFilterCart.size()) {
            realm.beginTransaction();
            try {

                listFilterCart.get(position).setCantidad(0);// se predetermina cantidad en 1
                realm.commitTransaction();

                adapter_cart.notifyItemChanged(position);
                updateTotal();

                filterCartDB(textFilter);//esto se hace para que actualize la lista y quite el elemento
            } catch (Throwable e) {
                Log.v(TAG, "removeCart... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }


    public void filterCartDB(final String textFilter){
        Log.v(TAG,"filterCatalogoDB... ---------------filterCatalogoDB--------------");
        realm.beginTransaction();
        try {
            listFilterCart.clear();
            querycat = realm.where(Catalogo.class)
                    .beginGroup()
                        .greaterThan("cantidad",0)
                        .or()
                        .greaterThan("cantidadServer",0)
                    .endGroup()
                    .contains("id",textFilter)

                    .findAllSorted("time").sort("time", Sort.DESCENDING);
                    //.findAll();

            listFilterCart.addAll(querycat);

            adapter_cart.notifyDataSetChanged();
            updateTotal();

            realm.commitTransaction();
        } catch(Throwable e) {
            Log.v(TAG,"filterCatalogoDB... ---------------error--------------");
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }


    public void sincCatalogoDB(List<DetalleProductos> listServer, boolean edit){//en modeo edicion, no se toma en cuenta si hay o no pedidos locales
        //en modo nuevo pedido, si hay algo local se deja tal cual
        Log.v(TAG,"sincCatalogoDB... ---------------sincCatalogoDB--------------");
        if( (listFilterCart.size()<1 || edit) && listServer.size()>0) {
            realm.beginTransaction();
            try {
                //listFilter.clear();
                for (int i = 0; i < listServer.size(); i++) {
                    Catalogo catalogo = realm.where(Catalogo.class).equalTo("id", listServer.get(i).getId()).findFirst();// se busca el id
                    if (catalogo != null && catalogo.getId().equals(listServer.get(i).getId())) {//se actualiza localmente lo que trae el API
                        catalogo.setCantidad(listServer.get(i).getCantidad());
                        catalogo.setCantidadServer(listServer.get(i).getCantidad());
                    } else if(catalogo==null){//si no existe, es xq hay que eliminarlo
                        //este caso supone que se modifico el catalogo
                    }
                }

                //debe verificar cuales no llegaron xq fieron eliminados
                for (int i = 0; i < listFilterCart.size(); i++) {
                    //se busca si hay algun elemento que dejo de estar disponible
                    for(int j=0; j<listServer.size();j++){
                        if(listServer.get(j).getId().equals(listFilterCart.get(i).getId())){///si no existe el elemento en la lista actual del server
                            break;//si encuentra el elemento, busca el siguiente
                        }
                        if(j>=listServer.size()-1)//llego al final de for y no encontro el elemento
                            listFilterCart.get(i).deleteFromRealm();
                    }

                }

                //listFilter.addAll(querycat);
                //adapter_cart.notifyDataSetChanged();
                realm.commitTransaction();
                Log.v(TAG, "sincCatalogoDB... ---------------  OK  --------------");
            } catch (Throwable e) {
                Log.v(TAG, "sincCatalogoDB... ---------------error--------------");
                if (realm.isInTransaction()) {
                    realm.cancelTransaction();
                }
                throw e;
            }
        }
    }


    public void clearCartDB(){
        RealmResults<Catalogo> querycat;
        List<Catalogo> catalogoList = new ArrayList<>();
        Log.v(TAG,"clearCartDB... ---------------clearCartDB--------------");
        realm.beginTransaction();
        try {
            catalogoList.clear();
            querycat = realm.where(Catalogo.class)
                    .beginGroup()
                    .greaterThan("cantidad",0)
                    .or()
                    .greaterThan("cantidadServer",0)
                    .endGroup()
                    .findAllSorted("time").sort("time", Sort.DESCENDING);
            //.findAll();
            catalogoList.addAll(querycat);

            for (int i = 0; i < catalogoList.size(); i++) {
                catalogoList.get(i).setCantidad(0);
                catalogoList.get(i).setCantidadServer(0);
            }

            realm.commitTransaction();
        } catch(Throwable e) {
            Log.v(TAG,"clearCartDB... ---------------error--------------");
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }

    private void updateTotal(){
        publishResultEditable(Pedidos_Hacer_Fragment.TAG, Pedidos_Hacer_Fragment.BROACAST_UPDATE_TOTAL,"data");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        adapter_cart.setEnable(enable);
        adapter_cart.notifyDataSetChanged();
        updateTotal();
    }

    public List<Catalogo> getListFilterCart() {
        return listFilterCart;
    }

    private void publishResultEditable(String tagFragment, String objectFragment, String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));
    }

    public void clearEditable(){
        adapter_cart.clearEditable();
    }
}
