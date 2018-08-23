package com.dupreincaperu.dupree;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter_Incorporacion;
import com.dupreincaperu.dupree.mh_fragments_main.CatalogosAdvanceFragment;
import com.dupreincaperu.dupree.mh_fragments_main.CatalogosFragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Bandeja_Entrada_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Catalogo_Premios_Fragment;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_response_api.Campana;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_fragments_main.ContactFragment;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Config_ModPerfil_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Incentivos_ConsultaPtos_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Incentivos_Redimir_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Incentivos_Referido_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Incorp_Todos_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Panel_Asesora_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Panel_Gerente_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Pedidos_EdoPedido_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Pedidos_Faltantes_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Pedidos_Hacer_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_CDR_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_CupoSaldoConf_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_Factura_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_Pagos_Realizados_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_Retenidos_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Reporte_SegPetQueRec_Fragment;
import com.dupreincaperu.dupree.mh_fragments_menu.Servicios_PagosOnLine_Fragment;
import com.dupreincaperu.dupree.mh_hardware.Camera;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.dupreincaperu.dupree.mh_response_api.Catalogo;
import com.dupreincaperu.dupree.mh_response_api.Oferta;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.ResponseGeneric;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MenuActivity";
    private final String BROACAST_S_DIALOG_LOGOUT = "broadcast_simple_dialog_logout";
    private final String BROACAST_S_DIALOG_UPDATE = "broadcast_simple_dialog_update";
    private final String BROACAST_S_DIALOG_SHARE = "broadcast_simple_dialog_share";

    //public static final String BROACAST_S_DIALOG_VOLANTE = "broadcast_simple_dialog_volante";

    Toolbar toolbar;
    //DrawerLayout drawer;
    Camera camera;
    boolean firtOpen=false;
    private Perfil perfil;

    private Realm realm;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Realm.init(this);//la base de datos
        realm = Realm.getDefaultInstance();

        setContentView(R.layout.activity_menu);
        perfil = getTypePerfil();
        //enable and disable

        navigationView = (NavigationView) findViewById(R.id.nav_view);//
        navigationView.setNavigationItemSelectedListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Panel");
        setSupportActionBar(toolbar);

        //selecciona el primer elemento del meni
        /*if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.menu_lat_home_asesoras, 0);
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //navigationView.setCheckedItem(R.id.menu_lat_hacer_pedidos);

        //selecciona item inicial

        localBroadcastReceiver = new LocalBroadcastReceiver();

        //Para el uso de imagenes
        camera = new Camera(MenuActivity.this);

        showMenuByPerfil(navigationView);


        //readCatalogos();

    }

    public void gotoMessages(){
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.menu_lat_bandeja_entrada));
    }
    /*
    private void readCatalogos(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

                Log.v(TAG,"readCatalogos... ---------------readCatalogos--------------");
                RealmQuery<Catalogo> query = bgRealm.where(Catalogo.class);
                RealmResults<Catalogo> result1 = query.findAll();
                String result;
                for (int i=0; i<result1.size();i++) {
                    Log.v(TAG,"data: "+String.valueOf(i)+") "+result1.get(i).getName());
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v(TAG,"readCatalogos... ---------------ok--------------");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG,"readCatalogos... ---------------error--------------");
                Log.e(TAG,"readCatalogos... "+error.getMessage());
                //realm.close();
            }
        });
    }
    */

    private void showMenuByPerfil(NavigationView navigationView){
        switch (perfil.getPerfil()){
            case Perfil.ADESORA:
                navigationView.getMenu().findItem(R.id.menu_lat_home_asesoras).setVisible(true);
                navigationView.getMenu().findItem(R.id.menu_lat_home_gerentes).setVisible(false);
                navigationView.getMenu().findItem(R.id.incorporaciones).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_redimir_incentivos).setVisible(true);
                navigationView.getMenu().findItem(R.id.menu_lat_cupo_saldo_conf).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_pedidos_ret).setVisible(false);

                navigationView.getMenu().findItem(R.id.menu_lat_edo_pedido).setVisible(false);
                navigationView.getMenu().findItem(R.id.servicios).setVisible(true);
                navigationView.getMenu().findItem(R.id.menu_lat_pagos_linea).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_detalle_factura).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_cat_premios).setVisible(false);

                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.menu_lat_home_asesoras));
                break;
            case Perfil.LIDER:
            case Perfil.GERENTE_ZONA:
            case Perfil.GERENTE_REGION:
                navigationView.getMenu().findItem(R.id.menu_lat_home_asesoras).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_home_gerentes).setVisible(true);

                //deshabilitar las inscripciones
                navigationView.getMenu().findItem(R.id.incorporaciones).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_redimir_incentivos).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_cupo_saldo_conf).setVisible(true);
                navigationView.getMenu().findItem(R.id.menu_lat_pedidos_ret).setVisible(true);

                navigationView.getMenu().findItem(R.id.menu_lat_edo_pedido).setVisible(false);
                navigationView.getMenu().findItem(R.id.servicios).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_detalle_factura).setVisible(false);
                navigationView.getMenu().findItem(R.id.menu_lat_cat_premios).setVisible(false);
                if(perfil.getPerfil()==Perfil.GERENTE_ZONA || perfil.getPerfil()==Perfil.GERENTE_REGION) {
                    navigationView.getMenu().findItem(R.id.menu_lat_faltantes).setVisible(false);
                }
                onNavigationItemSelected(navigationView.getMenu().findItem(R.id.menu_lat_home_gerentes));
                break;
        }
    }

    private Perfil getTypePerfil(){
        String jsonPerfil = mPreferences.getJSON_TypePerfil(MenuActivity.this);
        if(jsonPerfil != null){
            return new Gson().fromJson(jsonPerfil, Perfil.class);
        }
        msgToast("Hubo un problema desconocido, se recomienda cerrar sesión e iniciar nuevamente");
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void enableSearch(boolean isSearching, boolean isAsesora, boolean isCatalogo, String hintSearch){
        if(mMenu!=null){
            Log.e(TAG,"onCreateOptionsMenu (menu!=null)");
            Log.e(TAG,"onCreateOptionsMenu (isCatalogo): "+ isCatalogo);
            toolbar.getMenu().findItem(R.id.menu_toolbar_search).setVisible(isSearching);
            toolbar.getMenu().findItem(R.id.menu_asesora).setVisible(isAsesora);
            toolbar.getMenu().findItem(R.id.menu_share).setVisible(isCatalogo);
            toolbar.getMenu().findItem(R.id.menu_download).setVisible(isCatalogo);
            searchView.setQueryHint(hintSearch.concat("..."));
        }
    }

    private SearchView searchView;
    //private Menu menu;
    private EditText txtSearchSUgerencias;

    Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.mMenu=menu;
        //old menu
        Log.e(TAG,"onCreateOptionsMenu BBB");

        getMenuInflater().inflate(R.menu.menu, menu);
        //return true;

        //old menu
        MenuItem searchItem = menu.findItem(R.id.menu_toolbar_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(searchView.getQueryHint().toString().concat("..."));



        //searchView.setQueryHint(getResources().getString(R.string.codigo).concat("..."));


        //color para el place holder y texto
        /*EditText txtSearch = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        txtSearch.setHintTextColor(getResources().getColor(R.color.gray_3));
        txtSearch.setTextColor(Color.BLACK);*/

        // Configura el boton cancelar
        /*ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(getResources().getColor(R.color.gray_1), PorterDuff.Mode.MULTIPLY);*/

        // Configura el boton aceptar
        /*searchView.setSubmitButtonEnabled(true);
        ImageView searchSubmit = (ImageView) searchView.findViewById (android.support.v7.appcompat.R.id.search_go_btn);
        searchSubmit.setImageResource(R.drawable.ic_filter_list_white_24dp);
        searchSubmit.setColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY);
        searchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        // Configura el boton de busqueda
        /*ImageView searchButton = (ImageView) searchView.findViewById (android.support.v7.appcompat.R.id.search_button);
        searchButton.setImageResource(R.drawable.ic_search_white_24dp);
        //searchButton.setColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick to: ", "onClick");
                searchView.setIconified(true);
            }
        });*/



        // Configura el editext de busqueda
        txtSearchSUgerencias = ((EditText) searchView.findViewById (android.support.v7.appcompat.R.id.search_src_text));
        txtSearchSUgerencias.setInputType(InputType.TYPE_CLASS_NUMBER);
        //txtSearchSUgerencias.setTextSize(16);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Evento, se solicita realizar la búsqueda pulsando el botón “Enter” del teclado (o lupa)
            @Override
            public boolean onQueryTextSubmit(String query) {
                //se oculta el EditText
                obClickSearch(query);
                Log.e("onQueryTextSubmit to: ", query);
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;//habilita el serach del teclado
            }
            //Evento, cambio texto de búsqueda.
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onTextChanged to: ", newText);
                SearchView_onQueryTextChange(newText);
                return true;
            }
        });

        // editext de busqueda inicialmente oculto
        searchView.setIconified(true);


        return true;
    }

    public void SearchView_onQueryTextChange(String newText) {
        Log.e("newText to: ", newText);

        try {
            Pedidos_Hacer_Fragment myFragmentPed = (Pedidos_Hacer_Fragment) fragmentoGenerico;
            if (myFragmentPed != null && myFragmentPed.isVisible()) {
                // add your code here
                Log.e("myFragment to: ", newText);
                myFragmentPed.filterCatalogo(newText);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        /*


        try {
            Pedidos_Faltantes_Fragment myFragmentFalt = (Pedidos_Faltantes_Fragment) fragmentoGenerico;
            if (myFragmentFalt != null && myFragmentFalt.isVisible()) {
                // add your code here
                Log.e("myFragmentFalt to: ", newText);
                myFragmentFalt.filterFaltantes(newText);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
        */

        try {
            Reporte_Retenidos_Fragment myFragmentRet = (Reporte_Retenidos_Fragment) fragmentoGenerico;
            if (myFragmentRet != null && myFragmentRet.isVisible()) {
                // add your code here
                Log.e("myFragmentRet to: ", newText);
                myFragmentRet.filterRetenidos(newText);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }


    }

    private void obClickSearch(String data){
        try {
            Reporte_CupoSaldoConf_Fragment myFragmentCSC = (Reporte_CupoSaldoConf_Fragment) fragmentoGenerico;
            if (myFragmentCSC != null && myFragmentCSC.isVisible()) {
                // add your code here
                Log.e("myFragmentCSC to: ", data);
                myFragmentCSC.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Incentivos_ConsultaPtos_Fragment myFragmentConsP = (Incentivos_ConsultaPtos_Fragment) fragmentoGenerico;
            if (myFragmentConsP != null && myFragmentConsP.isVisible()) {
                // add your code here
                Log.e("myFragmentConsP to: ", data);
                myFragmentConsP.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Incentivos_Referido_Fragment myFragmentIncentR = (Incentivos_Referido_Fragment) fragmentoGenerico;
            if (myFragmentIncentR != null && myFragmentIncentR.isVisible()) {
                // add your code here
                Log.e("myFragmentIncentR to: ", data);
                myFragmentIncentR.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Reporte_CDR_Fragment myFragmentCDR = (Reporte_CDR_Fragment) fragmentoGenerico;
            if (myFragmentCDR != null && myFragmentCDR.isVisible()) {
                // add your code here
                Log.e("myFragmentCDR to: ", data);
                myFragmentCDR.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Reporte_SegPetQueRec_Fragment myFragmentPQR = (Reporte_SegPetQueRec_Fragment) fragmentoGenerico;
            if (myFragmentPQR != null && myFragmentPQR.isVisible()) {
                // add your code here
                Log.e("myFragmentPQR to: ", data);
                myFragmentPQR.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Reporte_Factura_Fragment myFragmentFact = (Reporte_Factura_Fragment) fragmentoGenerico;
            if (myFragmentFact != null && myFragmentFact.isVisible()) {
                // add your code here
                Log.e("myFragmentFact to: ", data);
                myFragmentFact.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Reporte_Pagos_Realizados_Fragment myFragmentPagos = (Reporte_Pagos_Realizados_Fragment) fragmentoGenerico;
            if (myFragmentPagos != null && myFragmentPagos.isVisible()) {
                // add your code here
                Log.e("myFragmentPagos to: ", data);
                myFragmentPagos.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Reporte_CupoSaldoConf_Fragment myFragmentCupSaldConf = (Reporte_CupoSaldoConf_Fragment) fragmentoGenerico;
            if (myFragmentCupSaldConf != null && myFragmentCupSaldConf.isVisible()) {
                // add your code here
                Log.e("myFragCupSaldConf to: ", data);
                myFragmentCupSaldConf.searchIdenty(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Pedidos_Hacer_Fragment myFragmentHacerPedidos = (Pedidos_Hacer_Fragment) fragmentoGenerico;
            if (myFragmentHacerPedidos != null && myFragmentHacerPedidos.isVisible()) {
                // add your code here
                Log.e("myFragCupSaldConf to: ", data);
                /// OJOOOOOO LO COMENTE myFragmentHacerPedidos.addToCartSearchView(data);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.e(TAG, "menu onOptionsItemSelected");
        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_asesora) {
            Log.e(TAG, "menu asesora");
            searchAsesora();
            return true;
        } else if (id == R.id.menu_share) {
            Log.e(TAG, "onOptionsItemSelected menu_share");
            if(perfil.isCatalogo())
                sharedCatalog_advance();
            else
                sharedCatalog();
        } else if (id == R.id.menu_download) {
            Log.e(TAG, "onOptionsItemSelected menu_download");
            if(perfil.isCatalogo())
                downloadCatalog_advance();
            else
                downloadCatalog();
        }
        Log.e(TAG, item.getTitle().toString());

        return super.onOptionsItemSelected(item);
    }

    public void searchAsesora(){
        try {
            Pedidos_Hacer_Fragment myFragmentHacerPedidos = (Pedidos_Hacer_Fragment) fragmentoGenerico;
            if (myFragmentHacerPedidos != null && myFragmentHacerPedidos.isVisible()) {
                // add your code here
                //Log.e("searchAsesora to: ", idAsesora);
                myFragmentHacerPedidos.obtainIdentyAsesora();
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
    }

    public void sharedCatalog(){
        try {
            Log.e(TAG,"sharedCatalog A");
            CatalogosFragment myFragmentHShared = (CatalogosFragment) fragmentoGenerico;

            if (myFragmentHShared != null && myFragmentHShared.isVisible()) {
                myFragmentHShared.sharedCatalog();
                Log.e(TAG,"sharedCatalog B");
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
            Log.e(TAG,"sharedCatalog C");
        }

        try {
            Log.e(TAG,"sharedCatalog A!");
            Catalogo_Premios_Fragment myFragmentHShared = (Catalogo_Premios_Fragment) fragmentoGenerico;

            if (myFragmentHShared != null && myFragmentHShared.isVisible()) {
                myFragmentHShared.sharedCatalog();
                Log.e(TAG,"sharedCatalog B!");
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
            Log.e(TAG,"sharedCatalog C!");
        }
    }

    public void downloadCatalog(){
        try {
            CatalogosFragment myFragmentDownload = (CatalogosFragment) fragmentoGenerico;
            if (myFragmentDownload != null && myFragmentDownload.isVisible()) {
                myFragmentDownload.downloadCatalog();
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }

        try {
            Catalogo_Premios_Fragment myFragmentDownload = (Catalogo_Premios_Fragment) fragmentoGenerico;
            if (myFragmentDownload != null && myFragmentDownload.isVisible()) {
                myFragmentDownload.downloadCatalog();
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
    }

    public void sharedCatalog_advance(){
        try {
            CatalogosAdvanceFragment myFragmentHShared = (CatalogosAdvanceFragment) fragmentoGenerico;
            if (myFragmentHShared != null && myFragmentHShared.isVisible()) {
                myFragmentHShared.sharedCatalog();
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
    }

    public void downloadCatalog_advance(){
        try {
            CatalogosAdvanceFragment myFragmentDownload = (CatalogosAdvanceFragment) fragmentoGenerico;
            if (myFragmentDownload != null && myFragmentDownload.isVisible()) {
                myFragmentDownload.downloadCatalog();
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
    }

    Fragment fragmentoGenerico;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(false);
        fragmentoGenerico = null;
        // Handle navigation view item clicks here.
        setFragment(item);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(MenuItem menuItem){
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (menuItem.getItemId()) {
            case R.id.menu_lat_home_asesoras:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Panel_Asesora_Fragment();
                ((Panel_Asesora_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_home_gerentes:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Panel_Gerente_Fragment();
                break;
            case R.id.menu_lat_bandeja_entrada:
                enableSearch(false,false,false,"");

                //navigationView.getMenu().findItem(R.id.menu_lat_bandeja_entrada).getIcon().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);

                fragmentoGenerico = new Bandeja_Entrada_Fragment();

                break;
            case R.id.menu_lat_register:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Incorp_Todos_Fragment();//hay que validar para que caiga en la pagina correcta
                ((Incorp_Todos_Fragment)fragmentoGenerico).loadData(MH_PagerAdapter_Incorporacion.PAGE_PREINSCRIPCION, perfil);
                break;
            case R.id.menu_lat_list_pre:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Incorp_Todos_Fragment();//hay que validar para que caiga en la pagina correcta
                ((Incorp_Todos_Fragment)fragmentoGenerico).loadData(MH_PagerAdapter_Incorporacion.PAGE_LIST_PRE, perfil);
                break;
            case R.id.menu_lat_hacer_pedidos:
                enableSearch(true,!perfil.getPerfil().equals(Perfil.ADESORA),false,getString(R.string.codigo_catalogo));
                fragmentoGenerico = new Pedidos_Hacer_Fragment();
                ((Pedidos_Hacer_Fragment)fragmentoGenerico).loadData(perfil);
                //((Pedidos_Hacer_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_catalogo:
                enableSearch(false,false,true,getString(R.string.codigo_catalogo));
                if(perfil.isCatalogo()) {
                    fragmentoGenerico = new CatalogosAdvanceFragment();
                    ((CatalogosAdvanceFragment) fragmentoGenerico).setToolbarVisible(false);//oculta la arra por defecto de este fragmento, ya esta activity la tiene
                } else {
                    fragmentoGenerico = new CatalogosFragment();
                    ((CatalogosFragment) fragmentoGenerico).setToolbarVisible(false);//oculta la arra por defecto de este fragmento, ya esta activity la tiene
                }
                //((Pedidos_Hacer_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_cat_premios:
                enableSearch(false,false,true,getString(R.string.codigo_catalogo));
                fragmentoGenerico = new Catalogo_Premios_Fragment();
                ((Catalogo_Premios_Fragment) fragmentoGenerico).setToolbarVisible(false);
                break;
            //LISTO PARA PRUEBAS ( TODOS - INDEPENDIENTE )
            case R.id.menu_lat_faltantes:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Pedidos_Faltantes_Fragment();
                break;
            case R.id.menu_lat_edo_pedido:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Pedidos_EdoPedido_Fragment();
                break;
            //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
            case R.id.menu_lat_consulta_puntos:
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Incentivos_ConsultaPtos_Fragment();
                ((Incentivos_ConsultaPtos_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            //LISTO PARA PRUEBAS ( SOLO ASESORAS )
            case R.id.menu_lat_redimir_incentivos:
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Incentivos_Redimir_Fragment();
                break;
            //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
            case R.id.menu_lat_incent_por_referido:
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Incentivos_Referido_Fragment();
                ((Incentivos_Referido_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
            case R.id.menu_lat_canjes_dev:
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_CDR_Fragment();
                ((Reporte_CDR_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
            case R.id.menu_lat_seg_pet_quej_rec_pqr:
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_SegPetQueRec_Fragment();
                ((Reporte_SegPetQueRec_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
            case R.id.menu_lat_detalle_factura:
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_Factura_Fragment();
                ((Reporte_Factura_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_pagos_realizados:
                //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_Pagos_Realizados_Fragment();
                ((Reporte_Pagos_Realizados_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_cupo_saldo_conf:
                //LISTO PARA PRUEBAS ( TODOS - GERENTE BUSCAR ASESORA )
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_CupoSaldoConf_Fragment();
                ((Reporte_CupoSaldoConf_Fragment)fragmentoGenerico).loadData(perfil);
                break;
            case R.id.menu_lat_pedidos_ret:
                //LISTO PARA PRUEBAS ( SOLO GERENTE )
                enableSearch(!perfil.getPerfil().equals(Perfil.ADESORA),false,false,getString(R.string.cedula_asesora));
                fragmentoGenerico = new Reporte_Retenidos_Fragment();
                break;
            case R.id.menu_lat_pet_quej_rec_pqr:
                //LISTO PARA PRUEBAS ( PAGINA )
                enableSearch(false,false,false,"");
                //fragmentoGenerico = new Servicios_PetQueRec_Fragment();// es el mismo del menu ppal
                fragmentoGenerico = new ContactFragment();
                break;
            case R.id.menu_lat_pagos_linea:
                //LISTO PARA PRUEBAS ( NO ESTA DEFINIDO )
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Servicios_PagosOnLine_Fragment();
                break;
            case R.id.menu_lat_share:
                //LISTO PARA PRUEBAS ( COMPARTIR APP )
                testShare();
                //break;
                return;
            case R.id.menu_lat_modif_perfil:
                //LISTO PARA PRUEBAS ( EDITAR DATOS DE USUARIO )
                enableSearch(false,false,false,"");
                fragmentoGenerico = new Config_ModPerfil_Fragment();
                break;
            case R.id.menu_lat_update:
                checkUpdate();
                //break;
                return;
            case R.id.menu_lat_logout:
                testLogOut();
                //break;
                return;
        }

        if (fragmentoGenerico != null) {
            String title = menuItem.getTitle().toString();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment, fragmentoGenerico, menuItem.getTitle().toString())// se reconoce al fragment por el String
                    .commit();

            toolbar.setTitle(menuItem.getItemId()==R.id.menu_lat_hacer_pedidos ? "Ingrese código ->" : menuItem.getTitle());
        }
    }

    private void actualizar(){
        mPreferences.setUpdateBannerAndPDF(MenuActivity.this, true);
        startActivity(new Intent(MenuActivity.this, FullscreenActivity.class));
        finish();
    }

    private void checkUpdate(){
        Http http = new Http(this);
        http.setGenericListener(new Http.GenericListener() {
            @Override
            public void onProcess(String version) {
                if(version.equals(mPreferences.getVersionCatalogo(MenuActivity.this))){
                    Toast.makeText(MenuActivity.this, getResources().getString(R.string.no_hay_actualizaciones), Toast.LENGTH_LONG).show();
                } else {
                    testUpdate();
                }
            }

            @Override
            public void onFailed() {
                Toast.makeText(MenuActivity.this, getResources().getString(R.string.http_error_desconocido), Toast.LENGTH_LONG).show();
            }
        });
        http.getVersion();
    }

    public void testLogOut(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.salir), getString(R.string.desea_salir));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_LOGOUT);
        simpleDialog.show(getSupportFragmentManager(),"mDialog");
    }

    public void testShare(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.compartir), getString(R.string.desea_compartir));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_SHARE);
        simpleDialog.show(getSupportFragmentManager(),"mDialog");
    }

    public void testUpdate(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.salir), getString(R.string.desea_actualizar));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_UPDATE);
        simpleDialog.show(getSupportFragmentManager(),"mDialog");
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
        LocalBroadcastManager.getInstance(MenuActivity.this).registerReceiver(
                localBroadcastReceiver,
                new IntentFilter(TAG));
    }

    public void unregisterBroadcat(){
        LocalBroadcastManager.getInstance(MenuActivity.this).unregisterReceiver(
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
                    case BROACAST_S_DIALOG_LOGOUT:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_LOGOUT).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            logout();

                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_LOGOUT).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_S_DIALOG_SHARE:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_SHARE).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            shareApp();

                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_SHARE).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_S_DIALOG_UPDATE:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_UPDATE).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            actualizar();

                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_UPDATE).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    /*case BROACAST_S_DIALOG_VOLANTE:
                        Log.i(TAG, "BROACAST_S_DIALOG_VOLANTE");
                        break;*/
                }
            }
        }
    }

    private void logout(){
        mPreferences.cerrarSesion(MenuActivity.this);
        clearCartDB();
        //clearOffersDB();
        deleteOferta();
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
        finish();
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

    //SE DEBERIA BORRAR TODAS LAS OFERTAS
    public void clearOffersDB(){
        RealmResults<Oferta> querycat;
        List<Oferta> ofertaList = new ArrayList<>();
        Log.v(TAG,"clearOffersDB... ---------------clearOffersDB--------------");
        realm.beginTransaction();
        try {
            ofertaList.clear();
            querycat = realm.where(Oferta.class)
                    .beginGroup()
                    .greaterThan("cantidad",0)
                    .or()
                    .greaterThan("cantidadServer",0)
                    .endGroup()
                    .findAllSorted("time").sort("time", Sort.DESCENDING);
            //.findAll();
            ofertaList.addAll(querycat);

            for (int i = 0; i < ofertaList.size(); i++) {
                ofertaList.get(i).setCantidad(0);
                ofertaList.get(i).setCantidadServer(0);
            }

            realm.commitTransaction();
        } catch(Throwable e) {
            Log.v(TAG,"clearOffersDB... ---------------error--------------");
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw e;
        }
    }

    public void deleteOferta(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                bgRealm.delete(Oferta.class);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v(TAG,"deleteOferta... ---------------ok--------------");
                //realm.close();
                //writeOfertas(listServer);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e(TAG,"deleteOferta... ---------------error--------------");
                Log.e(TAG,"deleteOferta... "+error.getMessage());
                //realm.close();

            }
        });
        //realm.beginTransaction();

        //realm.commitTransaction();
    }

    private void shareApp(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, " Hola! Encontré un app en la que puedes hacer pedidos en linea con dupreé, entra a https://play.google.com/store/apps/details?id=com.dupree2.Dupre&hl=es");
        startActivity(Intent.createChooser(intent, "Compartir con"));
        Log.e(TAG,"Compartir");
    }

    /**
     * METODOS DE RESPUESTAS HTTP
     */
    public void responseHttpBarrio(Boolean isBarrioResidencia, List<Barrio> barrioList){
        //((RegisterAsesoraFragment) fragmentoGenerico).responseHttpBarrio(isBarrioResidencia, barrioList);
        //(pagerAdapter.getAsesoraFragment())
    }

    public void successfulRegister(ResponseGeneric responseGeneric){
        msgToast(responseGeneric.getResult());
        ((RegisterAsesoraFragment) fragmentoGenerico).dissmissDialogTermins();
    }

    public void successfulTermins(ResponseGeneric responseGeneric){
        //msgToast(responseGeneric.getResult());
        ((RegisterAsesoraFragment) fragmentoGenerico).showTerminsDialog(responseGeneric.getResult());
        //showLogin.gotoPage(MH_PagerAdapter_Login.PAGE_LOGIN);
    }

    public void successUploadImage(ResponseGeneric responseGeneric){
        msgToast(responseGeneric.getStatus());
        //((RegisterAsesoraFragment) fragmentoGenerico).successUploadImage(responseGeneric.getResult());
    }

    public void responseHttpCampana(List<Campana> campanaList){
        //msgToast(responseGeneric.getStatus());
        //((RegisterAsesoraFragment) fragmentoGenerico).successUploadImage(responseGeneric.getResult());
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_LONG).show();
    }
    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + "FileDownloaded.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
    /**
     * CAMARA
     *
     *
     */
    //public AlertDialog takeImage(ImageView imageView) {
    public AlertDialog takeImage(String tagFragment, String objectFragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        camera.setBroadcast(tagFragment, objectFragment);

        //camera.setImageViewPic(imageView);//foto resultante
        builder.setTitle("Cargar imagen")
                .setMessage("Seleccione el origen de su imagen")
                .setPositiveButton("Cámara",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("Image - Base64","11111111");
                                boolean result=camera.checkPermissionCamera(MenuActivity.this);
                                camera.setUserChoosenTask(Camera.TAKE_PHOTO);
                                if(result) {
                                    Log.e("Image - Base64","333333333");
                                    camera.cameraTake();
                                }
                            }
                        })
                .setNegativeButton("Galería",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean result = camera.checkPermissionGalery(MenuActivity.this);
                                camera.setUserChoosenTask(Camera.CHOOSE_PHOTO);
                                if (result)
                                    camera.galleryTake();
                            }
                        })
                .setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        camera.onRequestPermissionsResultHand(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //SE ATRAPA SOLO EL ULTIMO CASO DE IMAGEN LISTA
        Uri uri=null;
        switch (requestCode){
            case Camera.PIC_CROP:
                if(data!=null) {
                    Bundle extras = data.getExtras();
                    if(extras!=null) {
                        Bitmap thePic = (Bitmap) extras.get("data");
                        //camera.getImageViewPic().setImageBitmap(thePic);//para cuando se envia el imgeview, en este caso se usa broadcast para pasar la ruta


                        publishResult(camera.getTagFragment(), camera.getObjectFragment(), data.getData().getPath());

                        //sendImageMultiPart(data.getData().getPath());
                    }
                }
                break;
            case Camera.CAMERA_CAPTURE:
                uri = Camera.picUri;


                Log.i(TAG,"Camera.CAMERA_CAPTURE: uri=: "+uri.toString());
                Log.i(TAG,"Camera.CAMERA_CAPTURE: uri=: "+camera.getRealPathFromURI(MenuActivity.this, uri));
                //Log.i(TAG,"onActivityResult: data.getData().getPath()=: "+data.getData().getPath());
                //publishResult(camera.getTagFragment(), camera.getObjectFragment(), uri.getPath());
                //camera.onActivityResultHand(requestCode, resultCode, data);

                Log.i(TAG,"Camera.CAMERA_CAPTURE: getPath=: "+Camera.imageFile.getPath());
                Log.i(TAG,"Camera.CAMERA_CAPTURE: getAbsolutePath=: "+Camera.imageFile.getAbsolutePath());
                //Log.i(TAG,"Camera.CAMERA_CAPTURE: uri=: "+camera.getRealPathFromURI(MenuActivity.this, uri));

                publishResult(camera.getTagFragment(), camera.getObjectFragment(), Camera.imageFile.getPath());
                break;
            case Camera.PICK_IMAGE_REQUEST:
                uri = data.getData();
                Log.i(TAG,"Camera.PICK_IMAGE_REQUEST: uri=: "+uri.toString());
                Log.i(TAG,"Camera.PICK_IMAGE_REQUEST: uri=: "+camera.getRealPathFromURI(MenuActivity.this, uri));
                //publishResult(camera.getTagFragment(), camera.getObjectFragment(), uri.getPath());
                Log.i(TAG,"onActivityResult: data.getData().getPath()=: "+data.getData().getPath());
                //camera.onActivityResultHand(requestCode, resultCode, data);

                publishResult(camera.getTagFragment(), camera.getObjectFragment(), camera.getRealPathFromURI(MenuActivity.this, uri));
                break;
        }
    }

    /*
    private void sendImageMultiPart(String filePath){
        new Http(MenuActivity.this).uploadImage(
                filePath, TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE
        );
    }
    */

    private void publishResult(String tagFrangment, String objectFragment, String data){
        Log.i(TAG,"publishResult: "+tagFrangment+" - "+objectFragment+" - "+data);
        LocalBroadcastManager.getInstance(MenuActivity.this).sendBroadcast(
                new Intent(tagFrangment)
                        .putExtra(tagFrangment, objectFragment)
                        .putExtra(Camera.BROACAST_DATA, data));
    }

    private void sendImage(String encodedImage){
        /*new HttpGaver(MH_Activity_Chat_Detail.this).addFileAvatar(
                new RequiredAddFile(
                        Preferences.getIdSesion(MH_Activity_Chat_Detail.this),
                        encodedImage,
                        0
                )
        );*/
        //Toast.makeText(MH_Activity_Chat_Detail.this,"En construccion...",Toast.LENGTH_LONG).show();
    }

    public void gotoFragmInscription(String nombre, String cedula, boolean modeEdit){
        ((Incorp_Todos_Fragment) fragmentoGenerico).gotoPageInscription(nombre, cedula, modeEdit);
    }

    public void updateListPre(){
        ((Incorp_Todos_Fragment) fragmentoGenerico).gotoPagelistPre();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void hideSearchView(){
        searchView.setQuery("", false);
        searchView.setIconified(true);
    }

    public void snackBar(String msg){
        try {
            Pedidos_Hacer_Fragment myFragmentHacerPedidos = (Pedidos_Hacer_Fragment) fragmentoGenerico;
            if (myFragmentHacerPedidos != null && myFragmentHacerPedidos.isVisible()) {
                // add your code here
                Log.e("snackBar to: ", msg);
                myFragmentHacerPedidos.snackBar(msg);
                return;
            }
        } catch (ClassCastException e){
            //en caso que la clase no sea
        }
        /*Snackbar.make(navigationView, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).setDuration(5000).show();*/
    }
}
