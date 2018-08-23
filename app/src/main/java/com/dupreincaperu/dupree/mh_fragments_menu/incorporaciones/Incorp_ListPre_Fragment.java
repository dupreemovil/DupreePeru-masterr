package com.dupreincaperu.dupree.mh_fragments_menu.incorporaciones;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog3Button;
import com.dupreincaperu.dupree.mh_required_api.RequiredApprovePreIns;
import com.dupreincaperu.dupree.mh_response_api.Preinscripcion;
import com.dupreincaperu.dupree.mh_response_api.ResponseListPreinscripcion;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_ListPre;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredListPre;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incorp_ListPre_Fragment extends Fragment {

    private final String TAG = "Incorp_ListPre_Fragment";
    private final String BROACAST_LIST_PREE_CONSULTA = "broadcast_list_pre_consulta";
    private final String BROACAST_LIST_PREE_VALIDATE_INCRIPCION = "broadcast_list_pre_validate_inscripcion";
    private final String BROACAST_LIST_PREE_VALIDATE_EDIT_INCRIPCION = "broadcast_list_pre_validate_edit_inscripcion";
    private final String BROACAST_LIST_PREE_VALIDATE_PREINCRIPCION = "broadcast_list_pre_validate_preinscripcion";
    private final String BROACAST_LIST_APPROVE_PREINCRIPCION = "broadcast_list_approve_preinscripcion";

    private final String BROACAST_LIST_ERROR = "broadcast_list_error";

    private List<Preinscripcion> listPre;
    private ResponseListPreinscripcion responseListPreinscripcion;
    private MH_Adapter_ListPre adapter_listPre;

    private LinearLayout dotsLayout;
    private TextView[] dots;

    private TextView tvButtonLeft, tvButtonRight;
    public Incorp_ListPre_Fragment() {
        // Required empty public constructor
    }

    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;

    }

    public static Incorp_ListPre_Fragment newInstance() {
        Bundle args = new Bundle();

        Incorp_ListPre_Fragment fragment = new Incorp_ListPre_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    SwipeRefreshLayout refreshListPre;

    RequiredListPre requiredListPre;
    public static String nameSelected="", identySelected="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_incorp_list_pre, container, false);

        localBroadcastReceiver = new LocalBroadcastReceiver();
        dotsLayout = (LinearLayout) v.findViewById(R.id.layoutDots);
        tvButtonLeft = (TextView) v.findViewById(R.id.tvButtonLeft);
        tvButtonLeft.setOnClickListener(mClickPaginator);
        tvButtonRight = (TextView) v.findViewById(R.id.tvButtonRight);
        tvButtonRight.setOnClickListener(mClickPaginator);
        enableButton(tvButtonLeft, false);
        enableButton(tvButtonRight, false);

        //dots = new TextView[10];
        //addBottomDots(5);

        refreshListPre = v.findViewById(R.id.refreshListPre);

        RecyclerView rcvListPre = refreshListPre.findViewById(R.id.rcvListPre);
        rcvListPre.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvListPre.setHasFixedSize(true);



        listPre = new ArrayList<>();
        //listPre = getListPreHttp();
        adapter_listPre = new MH_Adapter_ListPre(listPre, getActivity());
        rcvListPre.setAdapter(adapter_listPre);

        adapter_listPre.setRVOnItemClickListener(new MH_Adapter_ListPre.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                if(position>-1 && position<listPre.size()) {
                    if (listPre.get(position).getEstado().equals(MH_Adapter_ListPre.STATUS_AUTORIZADO)) {
                        nameSelected = listPre.get(position).getNombre() + " " + listPre.get(position).getApellido();
                        identySelected = listPre.get(position).getCedula();

                        testInscription(nameSelected);
                        //si es gerente de zona, el etado es pendiente y fue realizada por una LIDER, la Z debe aprobar.
                    } else if(listPre.get(position).getEstado().equals(MH_Adapter_ListPre.STATUS_PENDIENTE)
                            && perfil.getPerfil().equals(Perfil.GERENTE_ZONA) && listPre.get(position).getUsuario().equals(Perfil.LIDER)){
                        identySelected = listPre.get(position).getCedula();
                        testPreInscription(listPre.get(position).getNombre() + " " + listPre.get(position).getApellido());
                    }else if(listPre.get(position).getEstado().equals(MH_Adapter_ListPre.STATUS_RECHAZADO)) {
                        nameSelected = listPre.get(position).getNombre() + " " + listPre.get(position).getApellido();
                        identySelected = listPre.get(position).getCedula();
                        testEditInscription(nameSelected);
                        //msgToast("Esta preinscripción se encuentra " + listPre.get(position).getEstado());
                    }
                }
            }
        });

        refresh();

        refreshListPre.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                refreshListPre.setRefreshing(false);
            }
        });

        createProgress();

        return v;
    }

    private  void refresh(){
        Perfil typePerfil = new Gson().fromJson(mPreferences.getJSON_TypePerfil(getActivity()), Perfil.class);

        if(typePerfil!=null) {
            requiredListPre = new RequiredListPre(mPreferences.getTokenSesion(getActivity()), typePerfil.getPerfil(), typePerfil.getValor(), 0);
            checkListaPre();
        } else {
            msgToast("No hay datos de perfil... Vuelva a iniciar sesion");
        }
    }
    public void checkListaPre(){
        //if(listPre.size()<1){//OJO REFREZCAR CON PULL REFRESH
            searchListPre(0);
        //}
    }

    private void searchListPre(int page){
        requiredListPre.setIndex_pages(page);
        new Http(getActivity()).getListaPre(requiredListPre, TAG, BROACAST_LIST_PREE_CONSULTA);
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
        //unregisterBroadcat();//CREO QUE SE SALE AL IR A INSCRIPCION
        Log.i(TAG,"onPause()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
        unregisterBroadcat();
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

    /*
    public List<ResponseListPreinscripcion> extractListPre(String data){
        Type listRetenido = new TypeToken<ArrayList<ResponseListPreinscripcion>>(){}.getType();
        return new Gson().fromJson(data, listRetenido);
    }
    */

    public void testInscription(String to){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.inscripcion), getString(R.string.desea_inscribir)+" "+to+"?");
        simpleDialog.activateBroadcast(TAG, BROACAST_LIST_PREE_VALIDATE_INCRIPCION);
        simpleDialog.show(getChildFragmentManager(),"mDialog");
    }

    public void testEditInscription(String to){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.inscripcion), getString(R.string.desea_editar_inscripcion)+" "+to+"?");
        simpleDialog.activateBroadcast(TAG, BROACAST_LIST_PREE_VALIDATE_EDIT_INCRIPCION);
        simpleDialog.show(getChildFragmentManager(),"mDialog");
    }


    public void testPreInscription(String to){
        SimpleDialog3Button simpleDialog = new SimpleDialog3Button();
        simpleDialog.loadData(getString(R.string.approve_preinscription), getString(R.string.desea_aprobar_preinscription)+" "+to+"?",getString(R.string.aprobar), getString(R.string.rechazar));
        simpleDialog.activateBroadcast(TAG, BROACAST_LIST_PREE_VALIDATE_PREINCRIPCION);
        simpleDialog.show(getChildFragmentManager(),"mDialog");
    }

    private void addRetenidos(String jsonListPre){
        listPre.clear();
        if(jsonListPre!=null) {
            responseListPreinscripcion = new Gson().fromJson(jsonListPre, ResponseListPreinscripcion.class);
            if(responseListPreinscripcion.getResult()!=null)
                listPre.addAll(responseListPreinscripcion.getResult());
        }
        adapter_listPre.notifyDataSetChanged();
    }


    private long lastTime=0;
    private static String intentRepeat="";
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            Log.i(TAG, "BroadcastReceiver");
            if (intent == null || intent.getAction() == null) {
                return;
            }

            Log.e(TAG,"Calendar.getInstance().getTimeInMillis()= "+String.valueOf(Calendar.getInstance().getTimeInMillis()));
            Log.e(TAG,"lastTime= "+String.valueOf(lastTime));
            //si en un tiempo menor a un segundo, llega el mismo resultado, se desecha
            //esto garantiza que se pueda ejecutar la misma accion manualmente, mas evita repeticiones del broadcast que hacen algunos telefonos
            if( (Calendar.getInstance().getTimeInMillis()-lastTime<1000) && intentRepeat.equals(intent.getExtras().toString())) {// si no ha pasado medio segundo puede ser un rebote
                Log.e(TAG,"intentRepeat()");
                return;
            }

            lastTime=Calendar.getInstance().getTimeInMillis();
            intentRepeat=intent.getExtras().toString();


            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    //Datos personales
                    case BROACAST_LIST_PREE_CONSULTA:
                        String jsonRetenidos = intent.getStringExtra(Http.BROACAST_DATA);
                        addRetenidos(jsonRetenidos);
                        controllerPagination();

                        break;
                    case BROACAST_LIST_PREE_VALIDATE_INCRIPCION:
                        if(intent.getStringExtra(BROACAST_LIST_PREE_VALIDATE_INCRIPCION).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            new Http(getActivity()).enlaceInscription(nameSelected, identySelected, false);
                        }
                        break;
                    case BROACAST_LIST_PREE_VALIDATE_EDIT_INCRIPCION:
                        if(intent.getStringExtra(BROACAST_LIST_PREE_VALIDATE_EDIT_INCRIPCION).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            new Http(getActivity()).enlaceInscription(nameSelected, identySelected, true);
                        }
                        break;
                    case BROACAST_LIST_PREE_VALIDATE_PREINCRIPCION:
                        showProgressDialog();
                        if(intent.getStringExtra(BROACAST_LIST_PREE_VALIDATE_PREINCRIPCION).equals(SimpleDialog3Button.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            new Http(getActivity()).aprobarPreinscriccion(new RequiredApprovePreIns(identySelected,RequiredApprovePreIns.APROBAR),TAG, BROACAST_LIST_APPROVE_PREINCRIPCION, BROACAST_LIST_ERROR);
                        } else if(intent.getStringExtra(BROACAST_LIST_PREE_VALIDATE_PREINCRIPCION).equals(SimpleDialog3Button.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                            new Http(getActivity()).aprobarPreinscriccion(new RequiredApprovePreIns(identySelected,RequiredApprovePreIns.RECHAZAR),TAG, BROACAST_LIST_APPROVE_PREINCRIPCION, BROACAST_LIST_ERROR);
                        }
                        break;
                    case BROACAST_LIST_APPROVE_PREINCRIPCION:
                        dismissProgressDialog();
                        String result = intent.getStringExtra(Http.BROACAST_DATA);
                        if(result!=null){
                            msgToast(result);
                            refreshList(result);
                        }
                        break;
                    case BROACAST_LIST_ERROR:
                        dismissProgressDialog();
                        break;

                }

            }
        }
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

    private void refreshList(String result){
        int posSelected = adapter_listPre.getPosSelected();
        if(posSelected!=-1) {
            switch (result) {
                case "Rechazada":
                    listPre.get(posSelected).setEstado(MH_Adapter_ListPre.STATUS_RECHAZADO);
                    break;
                case "Aprobada":
                    listPre.get(posSelected).setEstado(MH_Adapter_ListPre.STATUS_AUTORIZADO);
                    break;
            }
            adapter_listPre.notifyItemChanged(posSelected);
        }
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void addBottomDots(int currentPage, int total_pages) {
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dots = new TextView[total_pages];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {

            //addBottomDots(5);
            dots[i] = new TextView(getActivity());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            dots[i].setTextSize(40);
            dots[i].setTextColor(colorsInactive[0]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[0]);
    }

    View.OnClickListener mClickPaginator = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.tvButtonLeft:
                    searchListPre(responseListPreinscripcion.getPage_index()-1);
                    break;
                case R.id.tvButtonRight:
                    searchListPre(responseListPreinscripcion.getPage_index()+1);
                    break;
            }
        }
    };

    private void controllerPagination(){
        if(responseListPreinscripcion!=null){
            int page_index = responseListPreinscripcion.getPage_index();//pagina actial
            int page_results = responseListPreinscripcion.getPage_results();//total de items 3
            int total_pages = responseListPreinscripcion.getTotal_pages();//total de paginas 3
            int total_results = responseListPreinscripcion.getPage_results();//total de items


            if((page_index==1 && total_pages>1) || (page_index==total_pages-1)){
                enableButton(tvButtonRight, true);
            } else if(page_index==total_pages){
                enableButton(tvButtonRight, false);
            }


            if(page_index==1){
                enableButton(tvButtonLeft, false);
            } else if(page_index==2){
                enableButton(tvButtonLeft, true);
            }

            addBottomDots(page_index-1, total_pages);//inicia en 0
        }
    }

    private void enableButton(TextView tvButtton, Boolean isEnable){
        tvButtton.setEnabled(isEnable);
    }

    public void updateList(){
        searchListPre(0);
    }
}
