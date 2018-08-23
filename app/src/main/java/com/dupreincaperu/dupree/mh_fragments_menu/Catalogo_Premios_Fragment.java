package com.dupreincaperu.dupree.mh_fragments_menu;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dupreincaperu.dupree.CatalogoViewerActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Catalogo_Premios;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Offers;
import com.dupreincaperu.dupree.mh_dialogs.ListString_Check;
import com.dupreincaperu.dupree.mh_dialogs.ListString_Radio;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog3Button;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog_01Button;
import com.dupreincaperu.dupree.mh_fragments_main.CatalogosAdvanceFragment;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.ListBoolean;
import com.dupreincaperu.dupree.mh_response_api.CatalogoPremiosList;
import com.dupreincaperu.dupree.mh_response_api.Oferta;
import com.dupreincaperu.dupree.mh_response_api.UrlCatalogo;
import com.dupreincaperu.dupree.mh_utilities.DownloadFileAsyncTask;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class Catalogo_Premios_Fragment extends Fragment {
    private final String BROACAST_CATALOGOS_SHARE = "broadcast_catalogos_share_advance";
    private final String BROACAST_CATALOGOS_DOWNLOAD = "broadcast_catalogos_download_advance";
    private final String BROACAST_CATALOGOS_DOWNLOADING_STATUS = "broadcast_catalogos_downloading_status_advance";

    private final String BROACAST_CATALOGOS_OVERRIDE_FILE = "broadcast_catalogos_override_file_advance";

    public static final String BROACAST_CATALOGOS_DOWNLOADING_ERROR="broadcast_catalogos_downloading_error_advance";

    private final String BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE = "broadcast_catalogos_decide_download_online_advance";


    public Catalogo_Premios_Fragment() {
        // Required empty public constructor
    }


    private ProgressBar progress;
    private RecyclerView rcvPremios;

    private List<CatalogoPremiosList.Folleto> listPremios;
    private MH_Adapter_Catalogo_Premios adapter_catalogo;

    private String TAG = "Catalogo_Premios_Frag";

    Toolbar toolbar;

    private CatalogoPremiosList.Folleto folleto;

    private StaggeredGridLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_catalogo_premios, container, false);

        AppBarLayout AppBarL_FragCatg = (AppBarLayout) v.findViewById(R.id.AppBarL_FragCatgP);
        toolbar = (Toolbar) AppBarL_FragCatg.findViewById(R.id.Toolbar_Act);
        toolbar.setNavigationIcon(null);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        setHasOptionsMenu(true);//hace falta cuando es fragmento para agregar opciones a toolbar

        toolbar.setVisibility(isToolbarVisible() ? View.VISIBLE : View.GONE);//en main, tiene tootlbar propia

        progress = v.findViewById(R.id.progress);
        rcvPremios = v.findViewById(R.id.rcvPremios);


        rcvPremios.setHasFixedSize(true);

        listPremios = new ArrayList<>();
        adapter_catalogo = new MH_Adapter_Catalogo_Premios(listPremios, getActivity());
        adapter_catalogo.setRVOnItemClickListener(new MH_Adapter_Catalogo_Premios.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemClickListener");
                folleto = listPremios.get(position);
                testOnlineDownload(folleto.getName());
            }
        });
        adapter_catalogo.setRVOnItemLongClickListener(new MH_Adapter_Catalogo_Premios.ItemsLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                Log.e(TAG,"adapter_catalogo.setRVOnItemLongClickListener");
            }
        });

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rcvPremios.setLayoutManager(mLayoutManager);
        rcvPremios.setAdapter(adapter_catalogo);


        createProgress();
        obtainCatalogoPremios();

        localBroadcastReceiver = new LocalBroadcastReceiver();

        return v;
    }

    private void obtainCatalogoPremios(){
        showProgressDialog();
        Http http = new Http(getActivity());
        http.getCatalogoPremios();
        http.setGenericListener(new Http.GenericListener() {
            @Override
            public void onProcess(String message) {
                dismissProgressDialog();
                CatalogoPremiosList catalogoPremiosList = new Gson().fromJson(message, CatalogoPremiosList.class);
                listPremios.clear();

                listPremios.addAll(catalogoPremiosList.getFolleto());

                adapter_catalogo.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                dismissProgressDialog();
            }
        });
    }

    public void sharedCatalog(){
        List<String> data = new ArrayList<>();
        for(int i=0; i<listPremios.size(); i++){
            data.add(listPremios.get(i).getName());
        }

        Log.e(TAG, new Gson().toJson(data));
        showList(BROACAST_CATALOGOS_SHARE, getString(R.string.share), data, null);
    }

    public void downloadCatalog(){
        List<String> data = new ArrayList<>();
        for(int i=0; i<listPremios.size(); i++){
            data.add(listPremios.get(i).getName());
        }

        showListRadio(BROACAST_CATALOGOS_DOWNLOAD, getString(R.string.download), data, null);
    }

    public void showList(String object, String title, List<String> data, String[] itemsSelected){
        ListString_Check dialogList = new ListString_Check();
        dialogList.loadData(TAG, object, title, data, itemsSelected);
        dialogList.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void showListRadio(String object, String title, List<String> data, String itemsSelected){
        ListString_Radio dialogList = new ListString_Radio();
        dialogList.loadData(TAG, object, title, data, itemsSelected);
        dialogList.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.e(TAG, "onOptionsItemSelected");
        switch (item.getItemId()){
            case R.id.menuDownload:
                downloadCatalog();
                break;
            case R.id.menuShare:
                sharedCatalog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
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

            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    //Datos personales
                    case BROACAST_CATALOGOS_SHARE:
                        String jsonShared = intent.getStringExtra(ListString_Check.BROACAST_DATA);
                        if(jsonShared!=null){
                            shareApp(jsonShared);
                        }
                        break;
                    case BROACAST_CATALOGOS_DOWNLOAD:
                        String jsonDownload = intent.getStringExtra(ListString_Radio.BROACAST_DATA);
                        if(jsonDownload!=null){
                            downloadPDF(jsonDownload);
                        }
                        break;
                    case BROACAST_CATALOGOS_DOWNLOADING_STATUS:
                        String jsonDownloading = intent.getStringExtra(ListString_Check.BROACAST_DATA);
                        if(jsonDownloading!=null){
                            progress.setProgress(Integer.parseInt(jsonDownloading));
                        }
                        break;
                    case BROACAST_CATALOGOS_OVERRIDE_FILE:
                        if(intent.getStringExtra(BROACAST_CATALOGOS_OVERRIDE_FILE).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            downloadFile();
                        } else if(intent.getStringExtra(BROACAST_CATALOGOS_OVERRIDE_FILE).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        break;
                    case BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE:
                        if(intent.getStringExtra(BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            openCatalogoPDF(folleto.getPdf());
                        } else if(intent.getStringExtra(BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                            //VER ONLINE
                            viewCatalogOnline(folleto.getUrl());
                        }
                        break;
                    case BROACAST_CATALOGOS_DOWNLOADING_ERROR:
                        progress.setProgress(0);
                        String error = intent.getStringExtra(ListString_Check.BROACAST_DATA);
                        if(error!=null){
                            showMessageAlert(R.drawable.ic_notifications_black_24dp, "Error!", "Ocurrió un error descargando el catálogo. Debe verificar su conexión a internet y reanudar la descarga.");
                            //msgToast("Ocurrio un error descargando el archivo: "+error);
                        }
                        break;
                }
            }
        }
    }

    private void shareApp(String jsonShared){
        boolean share_on = false;
        String share="Me gustaría compartir:\n\n";

        ListBoolean listBoolean = new Gson().fromJson(jsonShared, ListBoolean.class);

        for(int i = 0; i < listBoolean.getBooleanList().size(); i++) {
            if (listBoolean.getBooleanList().get(i)){
                share += listPremios.get(i).getName()
                        .concat("\n").concat(listPremios.get(i).getUrl().concat("\n\n"));
                share_on = true;
            }
        }

        if(share_on) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, share);
            startActivity(Intent.createChooser(intent, "Compartir con"));
            Log.e(TAG, "Compartir");
        }
    }

    /*
    private void shareApp(String jsonShared){
        String share="Me gustaría compartir ";
        switch (jsonShared){
            case "1":
                share+="el siguiente catálogo:\n\n"
                        .concat(responseUrlCatalogos.getCatalogos().get(0).getCatalogo3().getName())
                        .concat(", ")
                        .concat(responseUrlCatalogos.getCatalogos().get(0).getCatalogo3().getUrl());
                break;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, share);
        startActivity(Intent.createChooser(intent, "Compartir con"));
        Log.e(TAG,"Compartir");
    }
    */

    private void downloadPDF(String jsonDownload){
        int  positionSelected = Integer.valueOf(jsonDownload);
        if(positionSelected>-1 && positionSelected<listPremios.size()){
            url = listPremios.get(positionSelected).getPdf();
            permissionPDF();
        }
    }

    String url;
    public static final int CONST_PERMISSION_WRITE=123;
    @AfterPermissionGranted(CONST_PERMISSION_WRITE)
    private void permissionPDF() {
        //private void connect(int mode, String roomId, String nameCall, String numberCall, String idDevice) {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            //hacer
            Log.e(TAG,"----------------------------------");
            trydownloadFile(url);
        } else {
            EasyPermissions.requestPermissions(this, "Necesita habilitar permisos", CONST_PERMISSION_WRITE, perms);
        }
    }

    /**
     * Respuestas a permisos
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private String fileName;
    public void trydownloadFile(String url){
        Log.e(TAG, "trydownloadFile, try: "+url);
        //EXAMPLE URL "https://alcor.dupree.co/image/banner/catalogos400x300.jpg"
        String[] separate = url.split("/");//divide trama en un arreglo separado por "/"
        fileName = separate[separate.length-1];

        String pathFiles = mPreferences.getPathFiles(getActivity());
        if(pathFiles!=null) {
            String pathFileCurrent = pathFiles.concat("/").concat(fileName);
            File file = new File(pathFileCurrent);
            if(file.exists()){//ya la ruta existe
                Log.e(TAG,pathFileCurrent+", ya existe");
                testDownloadFile();
                return;
            }
        }

        downloadFile();

    }

    private void downloadFile(){
        new Http(getActivity()).downloadFile(TAG, BROACAST_CATALOGOS_DOWNLOADING_STATUS, BROACAST_CATALOGOS_DOWNLOADING_ERROR, url, fileName, DownloadFileAsyncTask.DIRECTORY_DOCUMENTS/*"https://alcor.dupree.co/image/banner/catalogos400x300.jpg"*/);
    }

    private void openCatalogoPDF(String url){
        Log.e(TAG, "openCatalogoPDF, try: "+url);
        //url = responseUrlCatalogos.getCatalogos().get(0).getCatalogo3().getUrl();
        String[] separate = url.split("/");//divide trama en un arreglo separado por "/"
        String fileName = separate[separate.length-1];

        String pathFiles = mPreferences.getPathFiles(getActivity());
        Log.e(TAG, "pathFiles, "+pathFiles);
        if(pathFiles!=null) {
            String pathFileCurrent = pathFiles.concat("/").concat(fileName);
            File file = new File(pathFileCurrent);
            if(file.exists()){//ya la ruta existe
                Log.e(TAG,pathFileCurrent+", ya existe");
                //testDownloadFile();
                //return;

                //String filename = "blabla.pdf";
                //File file = new File(filename);
                Uri internal = Uri.fromFile(file);
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(internal, "application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                target = Intent.createChooser(target, "Open File");
                try {
                    startActivity(target);
                } catch (ActivityNotFoundException e) {
                    msgToast("Error! PDF viewer");
                }
                startActivity(target);
            } else {
                msgToast("Debe descargar el catálogo");
            }
        } else {
            msgToast("Debe descargar el catálogo");
        }
    }

    private void testDownloadFile(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(fileName, getString(R.string.desea_sobreescribir_archivo));
        simpleDialog.activateBroadcast(TAG, BROACAST_CATALOGOS_OVERRIDE_FILE);//para que sea diferente el broadcats de cada items, y pasar el filtro de repetidos
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void testOnlineDownload(String titleCatalg){
        SimpleDialog3Button simpleDialog = new SimpleDialog3Button();
        simpleDialog.loadData(titleCatalg, getString(R.string.desea_ver_catalogo), getString(R.string.ver_previa_descarga), getString(R.string.dialog_view_online));
        simpleDialog.activateBroadcast(TAG, BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE);
        simpleDialog.show(getChildFragmentManager(),"mDialog");
    }

    public void showMessageAlert(int iconId, String title, String msg){
        SimpleDialog_01Button simpleDialog = new SimpleDialog_01Button();
        simpleDialog.loadData(iconId, title, msg);
        //simpleDialog.activateBroadcast(TAG, BROACAST_CATALOGOS_DECIDE_DOWNLOAD_ONLINE);
        simpleDialog.show(getChildFragmentManager(),"mDialog");
    }

    private void viewCatalogOnline(String urlFile){
        Intent intenCatalog = new Intent(getActivity(), CatalogoViewerActivity.class);
        intenCatalog.putExtra(CatalogoViewerActivity.URL_FILE, urlFile);
        startActivity(intenCatalog);
    }

    private boolean toolbarVisible=true;

    public void setToolbarVisible(boolean toolbarVisible) {
        this.toolbarVisible = toolbarVisible;
    }

    public boolean isToolbarVisible() {
        return toolbarVisible;
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private ProgressDialog pDialog;
    private void createProgress(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//K0LM6F
    }

    public void showProgressDialog(){
        pDialog.setMessage(getResources().getString(R.string.msg_espere));
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
    }
}
