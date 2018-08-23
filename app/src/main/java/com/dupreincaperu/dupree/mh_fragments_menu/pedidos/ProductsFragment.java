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

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Catalogo;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    private final String TAG = "products_fragment";
    private final String BROACAST_S_DIALOG_ADD_PRODUCTS = "broadcast_simple_dialog_add_products";
    private final String BROACAST_S_DIALOG_REMOVE_PRODUCTS = "broadcast_simple_dialog_remove_products";

    RealmResults<Catalogo> querycat;
    private List<Catalogo> listFilter;//, listSelected;
    private MH_Adapter_Catalogo adapter_catalogo;
    private Realm realm;

    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance() {
        Bundle args = new Bundle();
        
        ProductsFragment fragment = new ProductsFragment();
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
        rcvCatalogo.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvCatalogo.setHasFixedSize(true);

        //listPremios = new ArrayList<>();
        listFilter = new ArrayList<>();
        adapter_catalogo = new MH_Adapter_Catalogo(listFilter, getActivity());
        rcvCatalogo.setAdapter(adapter_catalogo);

        adapter_catalogo.setRVOnItemClickListener(new MH_Adapter_Catalogo.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
            }
        });

        adapter_catalogo.setRVOnAddCartClickListener(new MH_Adapter_Catalogo.AddCartClickListener() {
            @Override
            public void onAddCartClick(View v, int position) {
                //verifica si quiere agregar
                Log.e(TAG,"onAddCartClick: "+String.valueOf(position));
                testAddCart();
                //listFilter.get(position).setInTheCart(true);// se habilita controles mas incremento y decremento
                //listFilter.get(position).setCantidad(1);// se predetermina cantidad en 1
                //adapter_catalogo.notifyItemChanged(position);
            }
        });

        adapter_catalogo.setRVOnDecreaseClickListener(new MH_Adapter_Catalogo.DecreaseClickListener() {
            @Override
            public void onDecreaseClick(View v, int position) {
                Log.e(TAG,"onDecreaseClick, pos:  "+position);
                int cantidad =listFilter.get(position).getCantidad();
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

        adapter_catalogo.setRVOnIncreaseClickListener(new MH_Adapter_Catalogo.IncreaseClickListener() {
            @Override
            public void onIncreaseClick(View v, int position) {
                Log.e(TAG,"onIncreaseClick, pos:  "+position);
                increaseCart();

            }
        });




        localBroadcastReceiver = new LocalBroadcastReceiver();

        //QUIZAS INICESARIO, PARA NO DEJAR LA PANTALLA VACIA
        filterCatalogoDB("");//mostrar todo el catalogo

        return v;
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


    public void filterCatalogo(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        filterCatalogoDB(textFilter);
    }


    public void refresshCatalogo(RealmResults<Catalogo> querycat){
        Log.i(TAG,"refresshCatalogo()");
        //listFilter.addAll(querycat);
        adapter_catalogo.notifyDataSetChanged();
    }

    /*
    private  List<Productos> getCatalogo(){
        List<Productos> productos = new ArrayList<>();

        productos.add(new Productos("92820","AGUA DE PERFUME COLOMBIA ELLA 50 ML","34999","","600",0));
        productos.add(new Productos("66770","AMAZING SCRUB 500ML","9999","","184",0));
        productos.add(new Productos("663007","ANILLO ANDALUZ","12999","","108",0));
        productos.add(new Productos("800553","ANILLO ARMONIOSA","9999","","114",0));
        productos.add(new Productos("93438","ANILLO AUSTRIA T-7","14999","","106",0));
        productos.add(new Productos("36927","ANILLO AUSTRIA T-8","14999","","106",0));
        productos.add(new Productos("85313","ANILLO AUSTRIA T-9","14999","","106",0));
        productos.add(new Productos("314139","ANILLO BARU","9999","","114",0));
        productos.add(new Productos("38083","ANILLO BASIC","9999","","114",0));
        productos.add(new Productos("178879","ANILLO BRILLO IMPERIAL","11999","","105",0));
        productos.add(new Productos("84305","ANILLO DIAMANTI SAFIRO","9999","","114",0));
        productos.add(new Productos("87726","ANILLO ESMERALDA","9999","","114",0));
        productos.add(new Productos("64453","ANILLO MIDNIGHT","9999","","114",0));
        productos.add(new Productos("260788","ANILLO MILEY","9999","","114",0));
        productos.add(new Productos("186923","ANILLO PRIMAVERA","8999","","120",0));
        productos.add(new Productos("44965","ANILLO PULSERA ADELE","19999","","110",0));
        productos.add(new Productos("84193","ANILLO PULSERA FASHION PLATA","16999","","110",0));
        productos.add(new Productos("199180","ANILLO PULSERA SAFARI","20999","","117",0));
        productos.add(new Productos("61131","ANILLO PULSERA SEDUCTORA","19999","","110",0));
        productos.add(new Productos("922909","ANILLO ROMANIA","12999","","109",0));
        productos.add(new Productos("423426","ANILLO TI AMO","11999","","107",0));
        productos.add(new Productos("505118","ARETES ABBA","14999","","103",0));
        productos.add(new Productos("595143","ARETES ABBA NIVELES","10999","","850",0));
        productos.add(new Productos("791857","ARETES AFRIKA","12999","","111",0));
        productos.add(new Productos("962510","ARETES ANDALUZ","16999","","112",0));
        productos.add(new Productos("716960","ARETES BRILLO IMPERIAL","14999","","105",0));
        productos.add(new Productos("862859","ARETES CORTOS BLESSINGS","12999","","108",0));
        productos.add(new Productos("442288","ARETES CORTOS CORAZON PINK","12999","","118",0));
        productos.add(new Productos("587152","ARETES CORTOS ROMANIA","8999","","109",0));
        productos.add(new Productos("177877","ARETES CORTOS SAFARI","8999","","117",0));
        productos.add(new Productos("257560","ARETES DELIRIO","16999","","112",0));
        productos.add(new Productos("94807","ARETES DURCAL GOLD -IMP-B.","16999","","112",0));
        productos.add(new Productos("680921","ARETES ETNIA","12999","","113",0));
        productos.add(new Productos("979077","ARETES LARGOS BLESSINGS","14999","","108",0));
        productos.add(new Productos("117916","ARETES LARGOS CORAZON PINK","16999","","118",0));

        return  productos;
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

    public void increaseCart2(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            listFilter.get(position).setCantidad(listFilter.get(position).getCantidad()+1);
            realm.commitTransaction();
            adapter_catalogo.notifyItemChanged(position);
        }
    }

    private void increaseCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "increaseCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(listFilter.get(position).getCantidad()+1);
                adapter_catalogo.notifyItemChanged(position);
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

    public void decreaseCart2(int cantidad){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            listFilter.get(position).setCantidad(cantidad - 1);
            realm.commitTransaction();
            adapter_catalogo.notifyItemChanged(position);
        }
    }

    private void decreaseCart(int cantidad){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "decreaseCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(cantidad - 1);
                adapter_catalogo.notifyItemChanged(position);
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

    public void addCart2(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            //listFilter.get(position).setInTheCart(true);// se habilita controles mas incremento y decremento
            realm.beginTransaction();
            listFilter.get(position).setCantidad(1);// se predetermina cantidad en 1
            realm.commitTransaction();
            adapter_catalogo.notifyItemChanged(position);
        }
    }

    private void addCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "addCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(1);// se predetermina cantidad en 1
                adapter_catalogo.notifyItemChanged(position);
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

    public void removeCart2(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            //listFilter.get(position).setInTheCart(false);// se habilita controles mas incremento y decremento
            realm.beginTransaction();
            listFilter.get(position).setCantidad(0);// se predetermina cantidad en 0
            realm.commitTransaction();
            adapter_catalogo.notifyItemChanged(position);
        }
    }

    private void removeCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(0);// se predetermina cantidad en 1
                adapter_catalogo.notifyItemChanged(position);
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

    private void filterCatalogoDB(final String textFilter){
        Log.v(TAG,"filterCatalogoDB... ---------------filterCatalogoDB--------------");
        realm.beginTransaction();
        try {
            listFilter.clear();
            querycat = realm.where(Catalogo.class).contains("id",textFilter).findAll();
            listFilter.addAll(querycat);
            adapter_catalogo.notifyDataSetChanged();
            realm.commitTransaction();
        } catch(Throwable e) {
            Log.v(TAG,"filterCatalogoDB... ---------------error--------------");
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
