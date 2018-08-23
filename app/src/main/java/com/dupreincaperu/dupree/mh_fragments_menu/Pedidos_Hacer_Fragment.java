package com.dupreincaperu.dupree.mh_fragments_menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.FullscreenActivity;
import com.dupreincaperu.dupree.MenuActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Catalogo;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter_Pedidos;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Input;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.ProductoSend;
import com.dupreincaperu.dupree.mh_response_api.RequiredLiquidar;
import com.dupreincaperu.dupree.mh_response_api.ResponseEstadoPedido;
import com.dupreincaperu.dupree.mh_response_api.ResponseLiquida;
import com.dupreincaperu.dupree.mh_response_api.ResultEdoPedido;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pedidos_Hacer_Fragment extends Fragment {
    public final static String PEDIDO_FACTURADO = "2";
    public final static String PEDIDO_NUEVO = "0";
    public final static String PEDIDO_MODIFICAR = "1";
    public String statudPedido;

    public static  final String BROACAST_DATA="broacast_data";

    public static final String TAG = "Pedidos_Hacer_Fragment";
    private final String BROACAST_EDO_PEDIDO_OBTAIN = "broadcast_edo_pedido_obtaint";

    private final String BROACAST_S_DIALOG_ADD_FILTER = "broadcast_simple_dialog_add_products";
    private final String BROACAST_S_DIALOG_REMOVE_FILTER = "broadcast_simple_dialog_remove_products";

    private final String BROACAST_EDO_PEDIDO_INPUT_DIALOG = "broadcast_edo_pedido_input_dialog";

    private final String BROACAST_S_DIALOG_SEND_PEDIDO = "broadcast_simple_dialog_send_pedido";
    private final String BROACAST_LIQUIDATE_PRODUCTS = "broadcast_liquidate_products";

    public final static String BROACAST_CHANGE_PRODUCTS_EDIT = "broadcast_change_products_edit";
    public final static String BROACAST_CHANGE_OFFERS_EDIT = "broadcast_change_offerss_edit";
    public final static String BROACAST_CHANGE_EDIT_YES = "broadcast_change__edit_yes";
    public final static String BROACAST_CHANGE_EDIT_NOT = "broadcast_change__edit_not";

    public final static String BROACAST_UPDATE_TOTAL = "broadcast_update_total";

    TabLayout tabs;
    SwipeRefreshLayout swipePedidos;
    ViewPager pagerPedidos;
    LinearLayout layoutDotPedidos;
    private boolean enable = true;

    MH_PagerAdapter_Pedidos fragmentoPedidos;

    //FILTRO CONTROL
    RealmResults<Catalogo> querycat;
    private List<Catalogo> listFilter;//, listSelected;
    private MH_Adapter_Catalogo adapter_catalogo;
    private Realm realm;
    private LinearLayout ctcRcvFilter;
    private RecyclerView rcvCatalogo;
    //FILTRO CONTROL

    public Pedidos_Hacer_Fragment() {
        // Required empty public constructor
    }

    TextView tvNombreAsesora;
    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    private FloatingTextButton fabSendPedido;

    private boolean productsEditable=false;
    private boolean offersEditable=false;
    @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pedidos_hacer, container, false);

        fragmentoPedidos = new MH_PagerAdapter_Pedidos(getChildFragmentManager());

        fabSendPedido = v.findViewById(R.id.fabSendPedido);

        fabSendPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEnable()) {
                    msgToast(getString(R.string.pedido_no_puede_modificarse));
                    return;
                }

                if(fragmentoPedidos.getCartFragment().validate() || (fragmentoPedidos.getOffersFragment().validate())) {
                    testRefreshServer();
                } else {
                    msgToast("No se detectaron cambios");
                }
            }
        });

        //CONTROL DE FILTROS Y CATALOGO
        realm = Realm.getDefaultInstance();

        ctcRcvFilter = v.findViewById(R.id.ctcRcvFilter);
        ctcRcvFilter.setVisibility(View.GONE);

        rcvCatalogo = v.findViewById(R.id.rcvFilterPedido);
        rcvCatalogo.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rcvCatalogo.setHasFixedSize(true);

        //listPremios = new ArrayList<>();
        listFilter = new ArrayList<>();
        adapter_catalogo = new MH_Adapter_Catalogo(listFilter, getActivity());
        rcvCatalogo.setAdapter(adapter_catalogo);



        adapter_catalogo.setRVOnItemClickListener(new MH_Adapter_Catalogo.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
                //gotoZoomImage(listPremios.get(position).getImagen());
            }
        });

        adapter_catalogo.setRVOnAddCartClickListener(new MH_Adapter_Catalogo.AddCartClickListener() {
            @Override
            public void onAddCartClick(View v, int position) {
                //verifica si quiere agregar
                Log.e(TAG,"onAddCartClick: "+String.valueOf(position));
                addCart();//agregar sin validar
                //testAddCart();//solicitaron quitar esta validacion
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

        filterCatalogoDB("");//mostrar toodo el catalogo
        //CONTROL DE FILTROS Y CATALOGO

        //cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        //cardViewBackGround.setVisibility(View.INVISIBLE);
        //tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        //tvNombreAsesora.setText("");

        //CONTROL DE VISTA Y FRAGMENTOS
        localBroadcastReceiver = new LocalBroadcastReceiver();

        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        tabs = v.findViewById(R.id.tabs);

        swipePedidos = v.findViewById(R.id.swipePedidos);
        swipePedidos.setOnRefreshListener(mOnRefreshListener);
        swipePedidos.setEnabled(false);

        pagerPedidos = swipePedidos.findViewById(R.id.pagerPedidos);



        pagerPedidos.setAdapter(fragmentoPedidos);
        pagerPedidos.addOnPageChangeListener(mOnPageChangeListener);
        layoutDotPedidos = swipePedidos.findViewById(R.id.layoutDotPedidos);

        tabs.setupWithViewPager(pagerPedidos);
        createTabIcons();

        checkEdoPedido();

        return v;
    }

    private void controlEditable(){
        //fabSendPedido.setColorFilter((productsEditable | offersEditable) ? getResources().getColor(R.color.red_1) : getResources().getColor(R.color.green_check), PorterDuff.Mode.MULTIPLY);
        //fabSendPedido.setBackgroundTintList(ColorStateList.valueOf((productsEditable | offersEditable) ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.green_check)));


        Log.e(TAG,"controlEditable(obtainProductsLiquidate()), total_pedido: "+total_pedido);
        fabSendPedido.setBackgroundColor((productsEditable | offersEditable) ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.green_check));
    }



    public void testRefreshServer(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.guardar_cambios_server), getString(R.string.desea_guardar_cambios_server));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_SEND_PEDIDO);//para que sea diferente el broadcats de cada items, y pasar el filtro de repetidos
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    private void sendToServer(){
        new Http(getActivity()).liquidarPedido(obtainProductsLiquidate(), TAG, BROACAST_LIQUIDATE_PRODUCTS);
    }

    private double total_pedido = 0;
    public RequiredLiquidar obtainProductsLiquidate(){
        hiseSearchView();
        String id_pedido = resultEdoPedido!=null ? resultEdoPedido.getId_pedido() : "";

        //PAQUETONES SE QUITA
        List<String> paquetones= new ArrayList<>();
        //OFERTAS SE DEJA, PEOR NO ESTA LISTO
        List<ProductoSend> ofertas = new ArrayList<>();
        List<ProductoSend> productos = new ArrayList<>();
        //fragmentoPedidos.getOffersFra
        total_pedido = 0;
        for(int i=0;i<fragmentoPedidos.getOffersFragment().getListFilterOffers().size();i++) {
            if (fragmentoPedidos.getOffersFragment().getListFilterOffers().get(i).getCantidad() > 0) {
                ofertas.add(new ProductoSend(String.valueOf(fragmentoPedidos.getOffersFragment().getListFilterOffers().get(i).getId()), fragmentoPedidos.getOffersFragment().getListFilterOffers().get(i).getCantidad()));
                total_pedido = total_pedido + fragmentoPedidos.getOffersFragment().getListFilterOffers().get(i).getCantidad() * Double.parseDouble(fragmentoPedidos.getOffersFragment().getListFilterOffers().get(i).getValor_descuento());
            }
        }

        for(int i=0;i<fragmentoPedidos.getCartFragment().getListFilterCart().size();i++){
            productos.add(new ProductoSend(String.valueOf(fragmentoPedidos.getCartFragment().getListFilterCart().get(i).getId()), fragmentoPedidos.getCartFragment().getListFilterCart().get(i).getCantidad()));
            total_pedido = total_pedido + fragmentoPedidos.getCartFragment().getListFilterCart().get(i).getCantidad() * Double.parseDouble(fragmentoPedidos.getCartFragment().getListFilterCart().get(i).getValor());
        }

        return new RequiredLiquidar(id_pedido, paquetones, productos, ofertas);
    }



    private void checkEdoPedido(){
        Log.e(TAG,"checkEdoPedido(A)");
        if(perfil != null){
            Log.e(TAG,"checkEdoPedido(B)");
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                Log.e(TAG,"checkEdoPedido(C)");

                searchIdenty("");
            } else {
                enableEdit(false);
                //debe desabilitar todod en mod
                //fragmentoPedidos.getCartFragment().clearCartDB();
            }
        }
    }

    private void updateView(){
        Log.e(TAG, "updateView()");
        //cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(resultEdoPedido.getAsesora());

        //VERIFICA QUE LA ASESORA SE HAYA CARGADO BIEN
        if(!tvNombreAsesora.getText().toString().isEmpty()){
            //muestra o no ofertas
            fragmentoPedidos.getOffersFragment().setShowOffers(resultEdoPedido.getOfertas().getActivo().equals(ResponseEstadoPedido.SHOW_PRDUCTS));

            //VERIFICA SI CAMBIO LA CAMPAÑA
            String campanaActualServer = resultEdoPedido.getCampana();
            String campanaActualLocal = mPreferences.getCampanaActual(getActivity());

            //campanaActualLocal="3000";

            if(campanaActualLocal==null){
                mPreferences.setCampanaActual(campanaActualServer, getActivity());// se respalda la campañ actual
            } else if(!campanaActualLocal.equals(campanaActualServer)){//si cambio la campaña
                // se borrar e pedido actual (prductos y ofertas) de la DB
                mPreferences.setCampanaActual(campanaActualServer, getActivity());// se respalda la campañ actual
                mPreferences.setChangeCampana(getActivity(), true);

                startActivity(new Intent(getActivity(), FullscreenActivity.class));
                getActivity().finish();
                return;
            }

            selectAction();
        }else{
            Log.e(TAG,"No se actualiza el nombre de la asesora");
            Toast.makeText(getActivity().getApplicationContext(),"Ingrese nuevamente la cedula de asesora",Toast.LENGTH_SHORT).show();
        }

    }

    private void selectAction(){
        Log.e(TAG, "selectAction()");
        switch (resultEdoPedido.getEstado_pedido()) {
            case PEDIDO_NUEVO:
                // Todoel proceso desde cero, puede existir algo loca
                //borra los productos de la DB que esten pedidos con anterioridad
                Log.e(TAG, "PEDIDO NUEVO");//no llega nada de productos nuevos, se mantienen los de la db local
                enableEdit(true);
                fragmentoPedidos.getCartFragment().sincCatalogoDB(resultEdoPedido.getProductos().getProductos(), false);//Se da prioridad al pedido local
                fragmentoPedidos.getCartFragment().clearEditable();
                fragmentoPedidos.getCartFragment().setEnable(true);
                fragmentoPedidos.getCartFragment().filterCartDB("");//actualiza el carrito

                fragmentoPedidos.getOffersFragment().clearEditable();
                fragmentoPedidos.getOffersFragment().setEnable(true);
                fragmentoPedidos.getOffersFragment().sincOfertasDB(resultEdoPedido.getOfertas().getProductos(), false);//prevalece la oferta local;

                break;
            case PEDIDO_MODIFICAR:
                //Si, es editar se da prioridad a lo que esta en el server
                Log.e(TAG, "PEDIDO A MODIFICAR");
                enableEdit(true);

                fragmentoPedidos.getCartFragment().sincCatalogoDB(resultEdoPedido.getProductos().getProductos(), true);//Se borra el pedido local
                fragmentoPedidos.getCartFragment().clearEditable();
                fragmentoPedidos.getCartFragment().setEnable(true);
                fragmentoPedidos.getCartFragment().filterCartDB("");//actualiza el carrito

                fragmentoPedidos.getOffersFragment().clearEditable();
                fragmentoPedidos.getOffersFragment().setEnable(true);
                fragmentoPedidos.getOffersFragment().sincOfertasDB(resultEdoPedido.getOfertas().getProductos(), true);//prevalece la oferta del server
                //enableEdit(true);

                break;
            case PEDIDO_FACTURADO:
                //ya el pedido fue facturado, no se puede hacer otro o editar, no debe haber nada local
                //se muestra los que llegan, y no se pueden modificar
                Log.e(TAG, "PEDIDO YA FACTURADO");
                enableEdit(false);
                break;
        }
        updateTotal();
    }

    private void enableEdit(boolean isEnable){
        setEnable(isEnable);
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tabOne.setText(getResources().getString(R.string.carrito));

        Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_shopping_cart_white_24dp);
        mDrawable2.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tabOne.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable2, null, null);
        tabs.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tabTwo.setText(getResources().getString(R.string.ofertas));

        Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_list_white_24dp);
        mDrawable1.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tabTwo.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable1, null, null);
        tabs.getTabAt(1).setCustomView(tabTwo);
    }

    /**
     * Eventos SwipeRefreshLayout
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {

        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG,"PAGE_MAIN Page: "+position);
            switch (position){
                case MH_PagerAdapter_Pedidos.PAGE_CART:
                    //setSelectedItem(R.id.navigation_asesora);
                    fragmentoPedidos.getCartFragment().setEnable(isEnable());
                    fragmentoPedidos.getOffersFragment().setEnable(isEnable());

                    fragmentoPedidos.getCartFragment().filterCart(textFilter);
                    break;
                case MH_PagerAdapter_Pedidos.PAGE_OFFERS:
                    //setSelectedItem(R.id.navigation_home);
                    fragmentoPedidos.getCartFragment().setEnable(isEnable());
                    fragmentoPedidos.getOffersFragment().setEnable(isEnable());

                    fragmentoPedidos.getOffersFragment().filterOffersDB(textFilter);
                    break;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public static String textFilter="";
    public void filterCatalogo(String textFilter){
        this.textFilter=textFilter;

        if(isEnable()) {
            //adapter_catalogo.getmFilter().filter(textFilter);
            Log.e("newText to: ", textFilter);
            if (pagerPedidos.getCurrentItem() == MH_PagerAdapter_Pedidos.PAGE_CART) {
                if (textFilter.length() > 2) {
                    ctcRcvFilter.setVisibility(View.VISIBLE);
                    filterCatalogoDB(textFilter);
                } else {
                    ctcRcvFilter.setVisibility(View.GONE);
                }
                //fragmentoPedidos.getProductsFragment().filterCatalogo(textFilter);
            } else if (pagerPedidos.getCurrentItem() == MH_PagerAdapter_Pedidos.PAGE_OFFERS) {
                fragmentoPedidos.getOffersFragment().filterOffersDB(textFilter);
            }
        }

    }

    /*
    public void filterCatalogo(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        filterCatalogoDB(textFilter);
    }*/

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
    ResultEdoPedido resultEdoPedido;
    private long lastTime=0;
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            Log.i(TAG, "BroadcastReceiver");
            if (intent == null || intent.getAction() == null) {
                return;
            }

            if(Calendar.getInstance().getTimeInMillis()-lastTime<500) {// si no ha pasado medio segundo puede ser un rebote
                Log.e(TAG,"intentRepeat()");
                return;
            }

            lastTime=Calendar.getInstance().getTimeInMillis();

            /*if(intentRepeat.equals(intent.getExtras().toString())) {
                Log.e(TAG,"repeat: "+intent.getExtras().toString());
                return;
            }*/
            intentRepeat=intent.getExtras().toString();

            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    case BROACAST_EDO_PEDIDO_OBTAIN:
                        Log.i(TAG, "BROACAST_EDO_PEDIDO_OBTAIN");
                        String jsonEstadoPedido = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonEstadoPedido!=null){
                            resultEdoPedido = new Gson().fromJson(jsonEstadoPedido, ResultEdoPedido.class);
                            updateView();
                        }
                        break;
                    case BROACAST_S_DIALOG_ADD_FILTER:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_FILTER).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            addCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_ADD_FILTER).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_S_DIALOG_REMOVE_FILTER:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_FILTER).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            removeCart();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_REMOVE_FILTER).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_EDO_PEDIDO_INPUT_DIALOG:
                        String identyAsesora = intent.getStringExtra(MH_Dialogs_Input.BROACAST_DATA);
                        if(identyAsesora!=null){
                            clearAllData();
                            searchIdenty(identyAsesora);
                            Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 66666666");
                        }
                        break;
                    case BROACAST_S_DIALOG_SEND_PEDIDO:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_SEND_PEDIDO).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            sendToServer();
                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_SEND_PEDIDO).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_LIQUIDATE_PRODUCTS:
                        //actualizar la base de datos con lo procesado
                        // se debe hacer que la cantidad actual, sea igual a la cantidad enviada
                        Log.i(TAG, "BROACAST_LIQUIDATE_PRODUCTS");
                        String jsonLiquidate = intent.getStringExtra(MH_Dialogs_Input.BROACAST_DATA);
                        if(jsonLiquidate!=null){
                            //searchIdenty(identyAsesora);
                            ResponseLiquida responseLiquida = new Gson().fromJson(jsonLiquidate, ResponseLiquida.class);
                            if(responseLiquida.getCodigo()!=null && responseLiquida.getCodigo().equals(Http.CODE_OK)){
                                NumberFormat formatter = NumberFormat.getInstance(Locale.US);
                                ((MenuActivity) getActivity()).snackBar
                                        (
                                                "Total: "
                                                        .concat("$".concat(formatter.format(Float.parseFloat(responseLiquida.getTotal_pedido()))))
                                                        .concat(". ")
                                                        .concat(responseLiquida.getMensaje())
                                        );
                                fragmentoPedidos.getCartFragment().pedidoEndviadoexitosamente();
                                fragmentoPedidos.getOffersFragment().ofertaEndviadoexitosamente();

                                initFAB();

                                //if(!perfil.getPerfil().equals(Perfil.ADESORA))//solicitaron borrar toda la data luego de hacer el pedido
                                    clearAllData();//a ultima hora solicitaron limpiar en caso gerente
                            }
                        }




                        break;

                    case BROACAST_CHANGE_PRODUCTS_EDIT:
                        Log.e(TAG, "BROACAST_CHANGE_PRODUCTS_EDIT_YES");
                        if(intent.getStringExtra(BROACAST_DATA).equals(BROACAST_CHANGE_EDIT_YES)){
                            Log.i(TAG, "BROACAST_CHANGE_EDIT_YES");
                            productsEditable=true;
                        } else if(intent.getStringExtra(BROACAST_DATA).equals(BROACAST_CHANGE_EDIT_NOT)){
                            Log.i(TAG, "BROACAST_CHANGE_EDIT_NOT");
                            productsEditable=false;
                        }
                        controlEditable();
                        break;

                    case BROACAST_CHANGE_OFFERS_EDIT:
                        Log.e(TAG, "BROACAST_CHANGE_PRODUCTS_EDIT_NOT");
                        if(intent.getStringExtra(BROACAST_DATA).equals(BROACAST_CHANGE_EDIT_YES)){
                            Log.i(TAG, "BROACAST_CHANGE_EDIT_YES");
                            offersEditable=true;
                        } else if(intent.getStringExtra(BROACAST_DATA).equals(BROACAST_CHANGE_EDIT_NOT)){
                            Log.i(TAG, "BROACAST_CHANGE_EDIT_NOT");
                            offersEditable=false;
                        }
                        controlEditable();
                        break;
                    case BROACAST_UPDATE_TOTAL:
                        //Log.e(TAG, "BROACAST_UPDATE_TOTAL");

                        updateTotal();
                        break;
                }
            }
        }
    }

    private void initFAB(){
        //fabSendPedido.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_check)));
        fabSendPedido.setBackgroundColor(getResources().getColor(R.color.green_check));
        fabSendPedido.setTitle("");
    }

    private void clearAllData(){
        setEnable(false);

        tvNombreAsesora.setText("");
        //EN ESTE CASO SE ESTA MODO GERENTE, SE LIMPIA TODITO EL PEDIDO AL CAMBIAR DE ASESORA
        Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 11111111");
        fragmentoPedidos.getOffersFragment().deleteOferta();
        Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 22222222");
        //fragmentoPedidos.getOffersFragment().filterOffersDB("");
        Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 33333333");
        fragmentoPedidos.getCartFragment().clearCartDB();
        Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 44444444");
        fragmentoPedidos.getCartFragment().filterCart("");
        Log.i(TAG, "BROACAST_EDO_PEDIDO_INPUT_DIALOG 55555555");
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    String cedula;
    public void searchIdenty(String cedula){
        tvNombreAsesora.setText("");
        initFAB();
        this.cedula=cedula;
        if(!perfil.equals(Perfil.ADESORA))
            setEnable(false);

        new Http(getActivity()).getEstadoPedidos(new RequiredIdenty(cedula), TAG, BROACAST_EDO_PEDIDO_OBTAIN);
    }

    ///////////CONTROL DEL FILTRO DE PEDIDOS////////////

    public void testRemoveCart(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.remover), getString(R.string.desea_remover_pedido));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_REMOVE_FILTER);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void obtainIdentyAsesora(){
        MH_Dialogs_Input mhDialogsInput = new MH_Dialogs_Input();
        mhDialogsInput.loadData(TAG, BROACAST_EDO_PEDIDO_INPUT_DIALOG, getString(R.string.cedula_asesora), getString(R.string.cedula_asesora));
        //mhDialogsInput.activateBroadcast(TAG, BROACAST_S_DIALOG_ADD_FILTER);
        mhDialogsInput.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

















    private void increaseCart(){
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "increaseCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(listFilter.get(position).getCantidad()+1);
                adapter_catalogo.notifyItemChanged(position);
                //fragmentoPedidos.getCartFragment().filterCartDB("");
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
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(cantidad - 1);
                adapter_catalogo.notifyItemChanged(position);
                //fragmentoPedidos.getCartFragment().filterCartDB("");
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
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(1);// se predetermina cantidad en 1
                listFilter.get(position).setTime(Calendar.getInstance().getTimeInMillis());//controla el ultimo item agregado a la lista (cambiar a un int y controlar incremento)
                adapter_catalogo.notifyItemChanged(position);
                realm.commitTransaction();


                // se refersca  la lista del carrito
                //((MenuActivity) getActivity()).hideSearchView();
                hiseSearchView();
                fragmentoPedidos.getCartFragment().filterCartDB("");

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
        int position=adapter_catalogo.getPosSelected();
        Log.i(TAG, "removeCart(), pos: "+position);
        if(position>-1 && position<listFilter.size()) {
            realm.beginTransaction();
            try {
                listFilter.get(position).setCantidad(0);// se predetermina cantidad en 1
                adapter_catalogo.notifyItemChanged(position);
                //fragmentoPedidos.getCartFragment().filterCartDB("");
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
            querycat = realm.where(Catalogo.class)
                    .beginGroup()
                    .equalTo("cantidad",0)
                    .equalTo("cantidadServer",0)
                    .endGroup()
                    .contains("id",textFilter).findAll();
            listFilter.addAll(querycat);
            adapter_catalogo.notifyDataSetChanged();
            realm.commitTransaction();

            Log.e(TAG,"filterCatalogoDB... listFilter.SIZE(): "+listFilter.size());
        } catch(Throwable e) {
            Log.v(TAG,"filterCatalogoDB... ---------------error--------------");
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }


    public void snackBar(String msg){
        Snackbar.make(fabSendPedido, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).setDuration(15000).show();
    }
    ///////////CONTROL DEL FILTRO DE PEDIDOS////////////

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    private void hiseSearchView(){
        ((MenuActivity) getActivity()).hideSearchView();
    }

    private void updateTotal(){
        obtainProductsLiquidate();
        Log.e(TAG, "Update Total: "+total_pedido);

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        fabSendPedido.setTitle("$ ".concat(formatter.format(total_pedido)));
    }
}
