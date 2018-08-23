package com.dupreincaperu.dupree.mh_fragments_main;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_dialogs.DateDialog;
import com.dupreincaperu.dupree.mh_dialogs.ListCity;
import com.dupreincaperu.dupree.mh_dialogs.ListDpto;
import com.dupreincaperu.dupree.mh_dialogs.ListString;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Barrio;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Termins;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Volante;
import com.dupreincaperu.dupree.mh_hardware.Camera;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister;
import com.dupreincaperu.dupree.mh_required_api.RequiredTermins;
import com.dupreincaperu.dupree.mh_required_api.RequiredTerminsGerente;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.dupreincaperu.dupree.mh_response_api.Ciudad;
import com.dupreincaperu.dupree.mh_response_api.Departamento;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.Validate;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterAsesoraFragment extends Fragment{

    public static final String TAG = "RegisterAsesoraFragment";
    public static final String BROACAST_REG_TYPE_VALIDATE_REF="reg_type_validate_ref";

    public static final String BROACAST_REG_TYPE_TERMINS_VOLANTE ="reg_type_termins_volante";
    public static final String BROACAST_REG_TYPE_TERMINS_GERENTE="reg_type_termins_gerente";

    public static final String BROACAST_REG_TYPE_REGISTER_TERMINS="reg_type_register_termins";
    public static final String BROACAST_REG_TYPE_REGISTER_VOLANTE="reg_type_register_volante";
    public static final String BROACAST_REG_ERROR="reg_type_error";

    public static final String BROACAST_REG_TYPE_REGISTER_UPLOAD_FILE="reg_type_register_upload_file";

    public static final String BROACAST_REG_TYPE_TERMINS="reg_type_termins";
    public static final String BROACAST_REG_TYPE_BARRIOS="reg_type_barrios";
    public static final String BROACAST_REG_TYPE_BARRIOS_2="reg_type_barrios_2";



    EditText txtSpnTypeId, txtIdenty, txtName, txtLastname, txtDateBird, txtZone, txtSpnTypeVia,
            txtNum1, txtSpnChar1, txtSpnBis, txtNum2, txtSpnChar2, txtNum3, txtSpnSur,
            txtInfo, txtSpnDpto, txtSpnCity, txtSpnBarrio,txtSpnDirSend, txtPhone, txtCellphone,
            txtEmail;
    TextView txtConcatenateDir, txtConcatenateDir_2;

    LinearLayout ctxDirSend;
    EditText txtSpnTypeVia_2,
            txtNum1_2, txtSpnChar1_2, txtSpnBis_2, txtNum2_2, txtSpnChar2_2, txtNum3_2, txtSpnSur_2,
            txtInfo_2, txtSpnDpto_2, txtSpnCity_2, txtSpnBarrio_2;

    Button btnRegister;
    TextInputLayout txtInputRiesgo;

    ImageButton imgB_Searchref, imgB_CleanRef;
    public RegisterAsesoraFragment() {
        // Required empty public constructor
    }

    private ProgressDialog pDialog;
    FrameLayout ctnRegister;
    ScrollView scrollRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_asesora, container, false);
        //Datos personales
        ctnRegister = (FrameLayout) v.findViewById(R.id.ctnRegister);
        scrollRegister = (ScrollView) v.findViewById(R.id.scrollRegister);

        txtSpnTypeId = (EditText) v.findViewById(R.id.txtSpnTypeId);

        txtIdenty = v.findViewById(R.id.txtIdenty);
        txtInputRiesgo = v.findViewById(R.id.txtInputRiesgo);

        imgB_Searchref = v.findViewById(R.id.imgB_Searchref);
        imgB_CleanRef = v.findViewById(R.id.imgB_CleanRef);
        imgB_Searchref.setOnClickListener(mListenerClick);
        imgB_CleanRef.setOnClickListener(mListenerClick);

        txtName = (EditText) v.findViewById(R.id.txtName);
        txtLastname = (EditText) v.findViewById(R.id.txtLastname);


        txtDateBird = (EditText) v.findViewById(R.id.txtDateBird);
        txtZone = (EditText) v.findViewById(R.id.txtZone);

        //Direccion residencia
        txtConcatenateDir = (TextView) v.findViewById(R.id.txtConcatenateDir);
        txtConcatenateDir_2 = (TextView) v.findViewById(R.id.txtConcatenateDir_2);
        txtSpnTypeVia = (EditText) v.findViewById(R.id.txtSpnTypeVia);
        txtSpnTypeVia.addTextChangedListener(mChangeText);
        txtNum1 = (EditText) v.findViewById(R.id.txtNum1);
        txtNum1.addTextChangedListener(mChangeText);
        txtSpnChar1 = (EditText) v.findViewById(R.id.txtSpnChar1);
        txtSpnChar1.addTextChangedListener(mChangeText);
        txtSpnBis = (EditText) v.findViewById(R.id.txtSpnBis);
        txtSpnBis.addTextChangedListener(mChangeText);
        txtNum2 = (EditText) v.findViewById(R.id.txtNum2);
        txtNum2.addTextChangedListener(mChangeText);
        txtSpnChar2 = (EditText) v.findViewById(R.id.txtSpnChar2);
        txtSpnChar2.addTextChangedListener(mChangeText);
        txtNum3 = (EditText) v.findViewById(R.id.txtNum3);
        txtNum3.addTextChangedListener(mChangeText);
        txtSpnSur = (EditText) v.findViewById(R.id.txtSpnSur);
        txtSpnSur.addTextChangedListener(mChangeText);
        txtInfo = (EditText) v.findViewById(R.id.txtInfo);
        txtInfo.addTextChangedListener(mChangeText);


        txtSpnDpto = (EditText) v.findViewById(R.id.txtSpnDpto);
        txtSpnCity = (EditText) v.findViewById(R.id.txtSpnCity);
        txtSpnBarrio = (EditText) v.findViewById(R.id.txtSpnBarrio);

        //Direccion de envio
        txtSpnDirSend = (EditText) v.findViewById(R.id.txtSpnDirSend);
        txtSpnDirSend.setText(Arrays.asList(getResources().
                getStringArray(R.array.dirSend)).get(0));
        ctxDirSend = (LinearLayout) v.findViewById(R.id.ctxDirSend);

        txtSpnTypeVia_2 = (EditText) v.findViewById(R.id.txtSpnTypeVia_2);
        txtSpnTypeVia_2.addTextChangedListener(mChangeText_2);
        txtNum1_2 = (EditText) v.findViewById(R.id.txtNum1_2);
        txtNum1_2.addTextChangedListener(mChangeText_2);
        txtSpnChar1_2 = (EditText) v.findViewById(R.id.txtSpnChar1_2);
        txtSpnChar1_2.addTextChangedListener(mChangeText_2);
        txtSpnBis_2 = (EditText) v.findViewById(R.id.txtSpnBis_2);
        txtSpnBis_2.addTextChangedListener(mChangeText_2);
        txtNum2_2 = (EditText) v.findViewById(R.id.txtNum2_2);
        txtNum2_2.addTextChangedListener(mChangeText_2);
        txtSpnChar2_2 = (EditText) v.findViewById(R.id.txtSpnChar2_2);
        txtSpnChar2_2.addTextChangedListener(mChangeText_2);
        txtNum3_2 = (EditText) v.findViewById(R.id.txtNum3_2);
        txtNum3_2.addTextChangedListener(mChangeText_2);
        txtSpnSur_2 = (EditText) v.findViewById(R.id.txtSpnSur_2);
        txtSpnSur_2.addTextChangedListener(mChangeText_2);
        txtInfo_2 = (EditText) v.findViewById(R.id.txtInfo_2);
        txtInfo_2.addTextChangedListener(mChangeText_2);
        txtSpnDpto_2 = (EditText) v.findViewById(R.id.txtSpnDpto_2);
        txtSpnCity_2 = (EditText) v.findViewById(R.id.txtSpnCity_2);
        txtSpnBarrio_2 = (EditText) v.findViewById(R.id.txtSpnBarrio_2);

        //Contacto
        txtPhone = (EditText) v.findViewById(R.id.txtPhone);
        txtCellphone = (EditText) v.findViewById(R.id.txtCellphone);
        txtEmail = (EditText) v.findViewById(R.id.txtEmail);

        btnRegister = (Button) v.findViewById(R.id.btnRegister);

        //events
        txtSpnTypeId.setOnClickListener(mListenerClick);

        txtDateBird.setOnClickListener(mListenerClick);
        txtSpnTypeVia.setOnClickListener(mListenerClick);
        txtSpnChar1.setOnClickListener(mListenerClick);
        txtSpnBis.setOnClickListener(mListenerClick);
        txtSpnChar2.setOnClickListener(mListenerClick);
        txtSpnSur.setOnClickListener(mListenerClick);
        txtSpnDpto.setOnClickListener(mListenerClick);
        txtSpnCity.setOnClickListener(mListenerClick);
        txtSpnBarrio.setOnClickListener(mListenerClick);

        //events direccion envio
        txtSpnDirSend.setOnClickListener(mListenerClick);
        txtSpnTypeVia_2.setOnClickListener(mListenerClick);
        txtNum1_2.setOnClickListener(mListenerClick);
        txtSpnChar1_2.setOnClickListener(mListenerClick);
        txtSpnBis_2.setOnClickListener(mListenerClick);
        txtNum2_2.setOnClickListener(mListenerClick);
        txtSpnChar2_2.setOnClickListener(mListenerClick);
        txtNum3_2.setOnClickListener(mListenerClick);
        txtSpnSur_2.setOnClickListener(mListenerClick);
        txtInfo_2.setOnClickListener(mListenerClick);
        txtSpnDpto_2.setOnClickListener(mListenerClick);
        txtSpnCity_2.setOnClickListener(mListenerClick);
        txtSpnBarrio_2.setOnClickListener(mListenerClick);


        btnRegister.setOnClickListener(mListenerClick);

        localBroadcastReceiver = new LocalBroadcastReceiver();
        Log.e(TAG,"new LocalBroadcastReceiver()");

        getListDpto(mPreferences.getDpto(getActivity()));

        createProgress();

        setRefValidated(false);
        return v;
    }

    public Perfil getPerfil(){
        String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());
        if(jsonPerfil!=null)
            return new Gson().fromJson(jsonPerfil, Perfil.class);

        return null;
    }

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

    public void error(){
        dismissProgressDialog();
    }

    List<Departamento> listDpto=null;
    Departamento dptoSelected;
    List<Ciudad> listCiudad=null, listCiudad_2=null;
    Ciudad ciudad=null, ciudad_2=null;
    List<Barrio> listBarrio=null, listBarrio_2=null;
    Barrio barrio=null, barrio_2=null;
    public void getListDpto(String data){
        Type listType = new TypeToken<ArrayList<Departamento>>(){}.getType();
        listDpto = new Gson().fromJson(data, listType);
    }

    public String getJSONCiudad(List<Ciudad> listCiudad){
        return new Gson().toJson(listCiudad);
    }

    public void getDpto(String data){
        Type listType = new TypeToken<ArrayList<Departamento>>(){}.getType();
        listDpto = new Gson().fromJson(data, listType);
    }

    public List<Barrio> extractListBarrio(String data){
        Type listType = new TypeToken<ArrayList<Barrio>>(){}.getType();
        return new Gson().fromJson(data, listType);
    }


    View.OnClickListener mListenerClick =
            new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        case R.id.imgB_Searchref:
                            if(!txtIdenty.getText().toString().isEmpty()) {
                                new Http(getActivity()).validateCentralRiesgo(
                                        new RequiredIdenty(
                                                txtIdenty.getText().toString()
                                        ),
                                        TAG,
                                        BROACAST_REG_TYPE_VALIDATE_REF
                                );
                            }
                            break;
                        case R.id.imgB_CleanRef:
                            setRefValidated(false);
                            break;
                        case R.id.txtSpnTypeId:
                            showList(ListString.BROACAST_REG_TYPE_DOC, getString(R.string.tipo_de_documento), Arrays.asList(getResources().getStringArray(R.array.typeIdenty)), txtSpnTypeId.getText().toString());
                            break;


                        case R.id.txtDateBird:
                            showDate(DateDialog.BROACAST_REG_TYPE_BIRD);
                            break;
                        case R.id.txtSpnTypeVia:
                            showList(ListString.BROACAST_REG_TYPE_VIA, getString(R.string.tipo_via), Arrays.asList(getResources().getStringArray(R.array.typeVia)), txtSpnTypeVia.getText().toString());
                            break;
                        case R.id.txtSpnChar1:
                            showList(ListString.BROACAST_REG_TYPE_LETRA1, getString(R.string.letra), Arrays.asList(getResources().getStringArray(R.array.charOption)), txtSpnChar1.getText().toString());
                            break;
                        case R.id.txtSpnBis:
                            showList(ListString.BROACAST_REG_TYPE_BIS, getString(R.string.bis), Arrays.asList(getResources().getStringArray(R.array.bis)), txtSpnBis.getText().toString());
                            break;
                        case R.id.txtSpnChar2:
                            showList(ListString.BROACAST_REG_TYPE_LETRA2, getString(R.string.letra), Arrays.asList(getResources().getStringArray(R.array.charOption)), txtSpnChar2.getText().toString());
                            break;
                        case R.id.txtSpnSur:
                            showList(ListString.BROACAST_REG_TYPE_SUR, getString(R.string.sur), Arrays.asList(getResources().getStringArray(R.array.coord)), txtSpnSur.getText().toString());
                            break;
                        case R.id.txtSpnDpto:
                            if(listDpto!=null)
                                showDpto(ListDpto.BROACAST_REG_TYPE_DPTO, getString(R.string.departamento), listDpto, txtSpnDpto.getText().toString());
                            break;
                        case R.id.txtSpnCity:
                            if(listCiudad!=null)
                                showCity(ListCity.BROACAST_REG_TYPE_CITY, getString(R.string.ciudad), listCiudad, txtSpnCity.getText().toString());
                            break;
                        case R.id.txtSpnBarrio:
                            if(listBarrio!=null) {
                                barrioList = new ArrayList<>();
                                filterBarrio = new ArrayList<>();
                                barrioList.addAll(listBarrio);
                                filterBarrio.addAll(listBarrio);
                                showBarrio(MH_Dialogs_Barrio.BROACAST_REG_TYPE_BARRIO, getString(R.string.barrio), filterBarrio, barrioList, txtSpnBarrio.getText().toString());
                            }
                            break;
                        case R.id.txtSpnDirSend:
                            showList(ListString.BROACAST_REG_TYPE_DIR_SEND, getString(R.string.direccion_de_envio_pedido), Arrays.asList(getResources().getStringArray(R.array.dirSend)), txtSpnDirSend.getText().toString());
                            break;

                        case R.id.txtSpnTypeVia_2:
                            showList(ListString.BROACAST_REG_TYPE_VIA_2, getString(R.string.tipo_via), Arrays.asList(getResources().getStringArray(R.array.typeVia)), txtSpnTypeVia_2.getText().toString());
                            break;
                        case R.id.txtSpnChar1_2:
                            showList(ListString.BROACAST_REG_TYPE_LETRA1_2, getString(R.string.letra), Arrays.asList(getResources().getStringArray(R.array.charOption)), txtSpnChar1_2.getText().toString());
                            break;
                        case R.id.txtSpnBis_2:
                            showList(ListString.BROACAST_REG_TYPE_BIS_2, getString(R.string.bis), Arrays.asList(getResources().getStringArray(R.array.bis)), txtSpnBis_2.getText().toString());
                            break;
                        case R.id.txtSpnChar2_2:
                            showList(ListString.BROACAST_REG_TYPE_LETRA2_2, getString(R.string.letra), Arrays.asList(getResources().getStringArray(R.array.charOption)), txtSpnChar2_2.getText().toString());
                            break;
                        case R.id.txtSpnSur_2:
                            showList(ListString.BROACAST_REG_TYPE_SUR_2, getString(R.string.sur), Arrays.asList(getResources().getStringArray(R.array.coord)), txtSpnSur_2.getText().toString());
                            break;
                        case R.id.txtSpnDpto_2:
                            if(listDpto!=null)
                                showDpto(ListDpto.BROACAST_REG_TYPE_DPTO_2, getString(R.string.departamento), listDpto, txtSpnDpto_2.getText().toString());
                            break;
                        case R.id.txtSpnCity_2:
                            if(listCiudad_2!=null)
                                showCity(ListCity.BROACAST_REG_TYPE_CITY_2, getString(R.string.ciudad), listCiudad_2, txtSpnCity_2.getText().toString());
                            break;
                        case R.id.txtSpnBarrio_2:
                            if(listBarrio_2!=null) {
                                barrioList = new ArrayList<>();
                                filterBarrio = new ArrayList<>();
                                barrioList.addAll(listBarrio_2);
                                filterBarrio.addAll(listBarrio_2);
                                showBarrio(MH_Dialogs_Barrio.BROACAST_REG_TYPE_BARRIO_2, getString(R.string.barrio), filterBarrio, barrioList, txtSpnBarrio_2.getText().toString());
                            }
                            break;


                        case R.id.btnRegister:
                            //connect();
                            register();
                            break;
                    }

                }
            };

    public static RegisterAsesoraFragment newInstance() {
        Bundle args = new Bundle();
        RegisterAsesoraFragment fragment = new RegisterAsesoraFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //List<String> data = new ArrayList<String>();
    public void showList(String objectFragment, String title, List<String> data, String itemSelected){
        ListString dialogList = new ListString();
        dialogList.loadData(TAG, objectFragment, title, data, itemSelected);
        dialogList.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void showDpto(String objectFragment, String title, List<Departamento> data, String itemSelected){
        ListDpto dialogDpto = new ListDpto();
        dialogDpto.loadData(TAG, objectFragment, title, data, itemSelected);
        dialogDpto.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void showCity(String objectFragment, String title, List<Ciudad> data, String itemSelected){
        ListCity dialogCity = new ListCity();
        dialogCity.loadData(TAG, objectFragment, title, data, itemSelected);
        dialogCity.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void showBarrio(String objectFragment, String title, List<Barrio> datafilter, List<Barrio> data, String itemSelected){
        MH_Dialogs_Barrio dialogBarrio = new MH_Dialogs_Barrio();
        dialogBarrio.loadData(TAG, objectFragment, title, datafilter, data, itemSelected);
        dialogBarrio.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void showDate(String objectFragment){
        DateDialog newFragment = new DateDialog();
        newFragment.setData(TAG, objectFragment);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void successUploadImage(String urlImage){
        dialogsVolante.dismiss();
        new Http(getActivity()).terminsGerente(new RequiredTerminsGerente(txtIdenty.getText().toString(), urlImage), TAG, BROACAST_REG_TYPE_TERMINS_GERENTE, BROACAST_REG_ERROR);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"onDestroyView()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,"onDetach()");
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
        //unregisterBroadcat();//GENERALMENTE VA AQUI, PERO SE CAMBIO, PARA QUE SIGA RECIBIENDO CUANDO ABRE LA GALERIA DE IMAGENES
        Log.i(TAG,"onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()");
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

    private List<Barrio> barrioList;
    private List<Barrio> filterBarrio;

    private static long lastTime=0;
    private static String intentRepeat="";
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }

            /*
            if(intentRepeat.equals(intent.getExtras().toString())) {
                Log.e(TAG,"repeat: "+intent.getExtras().toString());
                return;
            }
            intentRepeat=intent.getExtras().toString();

*/
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
                switch (intent.getStringExtra(TAG)){
                    //Datos personales
                    case ListString.BROACAST_REG_TYPE_DOC:
                        String tipo = intent.getStringExtra(ListString.BROACAST_DATA);
                        Log.i(TAG,"BROACAST_REG_TYPE_DOC= "+tipo);
                        txtSpnTypeId.setError(null);
                        txtSpnTypeId.setText(tipo);
                        break;
                    //Direccion de residencia
                    case BROACAST_REG_TYPE_VALIDATE_REF:
                        Log.i(TAG, "BROACAST_REG_TYPE_VALIDATE_REF");
                        String nombreReferido = intent.getStringExtra(Http.BROACAST_DATA);
                        if(nombreReferido!=null) {
                            txtInputRiesgo.setHint(nombreReferido);
                            /*if(nombreReferido.equals("RECHAZADO"))
                                msgToast(txtIdenty.getText().toString().concat(" -> ").concat(nombreReferido));*/

                            setRefValidated(!nombreReferido.equals("RECHAZADO"));

                            if(nombreReferido.equals("RECHAZADO"))
                                snackBar(nombreReferido+" por centrales de riesgo");
                        }
                        break;


                    case ListString.BROACAST_REG_TYPE_VIA:
                        Log.i(TAG,"BROACAST_REG_TYPE_VIA");
                        txtSpnTypeVia.setError(null);
                        txtSpnTypeVia.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_LETRA1:
                        Log.i(TAG,"BROACAST_REG_TYPE_LETRA1");
                        txtSpnChar1.setError(null);
                        txtSpnChar1.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_BIS:
                        Log.i(TAG,"BROACAST_REG_TYPE_BIS");
                        txtSpnBis.setError(null);
                        txtSpnBis.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.bis)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_LETRA2:
                        Log.i(TAG,"BROACAST_REG_TYPE_LETRA2");
                        txtSpnChar2.setError(null);
                        txtSpnChar2.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_SUR:
                        Log.i(TAG,"BROACAST_REG_TYPE_SUR");
                        txtSpnSur.setError(null);
                        txtSpnSur.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListDpto.BROACAST_REG_TYPE_DPTO:
                        //limpiar
                        clearCity();

                        Log.i(TAG,"BROACAST_REG_TYPE_DPTO");
                        dptoSelected = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Departamento.class);
                        txtSpnDpto.setError(null);
                        txtSpnDpto.setText(dptoSelected.getName_dpto());
                        txtSpnDpto.setTag(dptoSelected.getId_dpto());
                        listCiudad=dptoSelected.getCiudades();
                        //cityJSON=getJSONCiudad(dptoSelected.getCiudades());
                        break;
                    case ListString.BROACAST_REG_TYPE_CITY:
                        //limp[iar
                        clearBarrio();

                        Log.i(TAG,"BROACAST_REG_TYPE_CITY");
                        ciudad = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Ciudad.class);
                        txtSpnCity.setError(null);
                        txtSpnCity.setText(ciudad.getName_ciudad());
                        txtSpnCity.setTag(ciudad.getId_ciudad());

                        //Buscar barrios
                        new Http(getActivity()).getBarrios(ciudad.getId_ciudad(), TAG, BROACAST_REG_TYPE_BARRIOS);
                        break;
                    case MH_Dialogs_Barrio.BROACAST_REG_TYPE_BARRIO:
                        Log.i(TAG,"BROACAST_REG_TYPE_BARRIO");
                        txtSpnBarrio.setError(null);
                        barrio = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Barrio.class);
                        txtSpnBarrio.setText(barrio.getName_barrio());
                        txtSpnBarrio.setTag(barrio.getId_barrio());
                        break;
                    //Direccion de envio
                    case ListString.BROACAST_REG_TYPE_DIR_SEND:
                        Log.i(TAG,"BROACAST_REG_TYPE_DIR_SEND");
                        txtSpnDirSend.setError(null);
                        txtSpnDirSend.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        ctxDirSend.setVisibility(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.dirSend)).get(0)) ? View.GONE : View.VISIBLE);
                        break;
                    case ListString.BROACAST_REG_TYPE_VIA_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_VIA_2");
                        txtSpnTypeVia_2.setError(null);
                        txtSpnTypeVia_2.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_LETRA1_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_LETRA1_2");
                        txtSpnChar1_2.setError(null);
                        txtSpnChar1_2.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_BIS_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_BIS_2");
                        txtSpnBis_2.setError(null);
                        txtSpnBis_2.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.bis)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_LETRA2_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_LETRA2_2");
                        txtSpnChar2_2.setError(null);
                        txtSpnChar2_2.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_REG_TYPE_SUR_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_SUR_2");
                        txtSpnSur_2.setError(null);
                        txtSpnSur_2.setText(
                                intent.getStringExtra(ListString.BROACAST_DATA).
                                        equals(Arrays.asList(getResources().
                                                getStringArray(R.array.charOption)).get(0)) ? "" :intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListDpto.BROACAST_REG_TYPE_DPTO_2:
                        //limpiar
                        clearCity_2();

                        Log.i(TAG,"BROACAST_REG_TYPE_DPTO");
                        dptoSelected = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Departamento.class);
                        txtSpnDpto_2.setError(null);
                        txtSpnDpto_2.setText(dptoSelected.getName_dpto());
                        txtSpnDpto_2.setTag(dptoSelected.getId_dpto());
                        listCiudad_2=dptoSelected.getCiudades();
                        //cityJSON_2=getJSONCiudad(dptoSelected.getCiudades());

                        txtSpnCity_2.setText("");
                        break;
                    case ListString.BROACAST_REG_TYPE_CITY_2:
                        //limpiar
                        clearBarrio_2();

                        Log.i(TAG,"BROACAST_REG_TYPE_CITY_2");
                        ciudad_2 = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Ciudad.class);
                        txtSpnCity_2.setError(null);
                        txtSpnCity_2.setText(ciudad_2.getName_ciudad());
                        txtSpnCity_2.setTag(ciudad_2.getId_ciudad());

                        new Http(getActivity()).getBarrios(ciudad_2.getId_ciudad(), TAG, BROACAST_REG_TYPE_BARRIOS_2);
                        break;
                    case MH_Dialogs_Barrio.BROACAST_REG_TYPE_BARRIO_2:
                        Log.i(TAG,"BROACAST_REG_TYPE_BARRIO_2");
                        barrio_2 = new Gson().fromJson(intent.getStringExtra(ListDpto.BROACAST_DATA), Barrio.class);
                        txtSpnBarrio_2.setError(null);
                        txtSpnBarrio_2.setText(barrio_2.getName_barrio());
                        txtSpnBarrio_2.setTag(barrio_2.getId_barrio());
                        break;
                    case DateDialog.BROACAST_REG_TYPE_BIRD:
                        Log.i(TAG,"BROACAST_REG_TYPE_BIRD");
                        txtDateBird.setError(null);
                        txtDateBird.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case BROACAST_REG_TYPE_BARRIOS:
                        responseHttpBarrio(extractListBarrio(intent.getStringExtra(Http.BROACAST_DATA)));
                        break;
                    case BROACAST_REG_TYPE_BARRIOS_2:
                        Log.i(TAG,intent.getStringExtra(Http.BROACAST_DATA));
                        responseHttpBarrio_2(extractListBarrio(intent.getStringExtra(Http.BROACAST_DATA)));
                        break;
                    case BROACAST_REG_TYPE_TERMINS:
                        dismissProgressDialog();
                        String termins = intent.getStringExtra(ListDpto.BROACAST_DATA);
                        showTerminsDialog(termins);
                        break;
                    case BROACAST_REG_TYPE_TERMINS_GERENTE:
                        //String termins = intent.getStringExtra(ListDpto.BROACAST_REG_DATA);
                        //showTerminsDialog(termins);
                        sendDataRegister();
                        break;

                    case BROACAST_REG_TYPE_TERMINS_VOLANTE:
                        String urlVolante = intent.getStringExtra(ListDpto.BROACAST_DATA);
                        dialogsVolante.dismiss();
                        new Http(getActivity()).terminsGerente(new RequiredTerminsGerente(txtIdenty.getText().toString(), urlVolante), TAG, BROACAST_REG_TYPE_TERMINS_GERENTE, BROACAST_REG_ERROR);
                        break;
                    case BROACAST_REG_TYPE_REGISTER_TERMINS:
                        String result = intent.getStringExtra(ListDpto.BROACAST_DATA);
                        msgToast(result);
                        dissmissDialogTermins();
                        dismissProgressDialog();
                        clearAllData();
                        break;
                    case BROACAST_REG_TYPE_REGISTER_VOLANTE:
                        String result2 = intent.getStringExtra(ListDpto.BROACAST_DATA);
                        msgToast(result2);
                        dissmissDialogVolante();
                        dismissProgressDialog();
                        clearAllData();
                        new Http(getActivity()).enlaceUpdateList();
                        break;
                    case BROACAST_REG_TYPE_REGISTER_UPLOAD_FILE:
                        String pathImage = intent.getStringExtra(Camera.BROACAST_DATA);
                        Log.e(TAG, BROACAST_REG_TYPE_REGISTER_UPLOAD_FILE+", pathImage+ "+pathImage);

                        dialogsVolante.setPathImage(pathImage);
                        break;
                    case BROACAST_REG_ERROR:
                        dismissProgressDialog();
                        break;
                }
            }

        }
    }

    String urlVolante;//para evitar repetir el broacast


    private void clearCity(){
        listCiudad=null;
        ciudad=null;
        txtSpnCity.setText("");
        listBarrio=null;
        barrio=null;
        txtSpnBarrio.setText("");
    }

    private void clearBarrio(){
        listBarrio=null;
        barrio=null;
        txtSpnBarrio.setText("");
    }

    private void clearCity_2(){
        listCiudad_2=null;
        ciudad_2=null;
        txtSpnCity_2.setText("");
        listBarrio_2=null;
        barrio_2=null;
        txtSpnBarrio_2.setText("");
    }

    private void clearBarrio_2(){
        listBarrio_2=null;
        barrio_2=null;
        txtSpnBarrio_2.setText("");
    }


    /**
     * RESPUESTAS HTTP
     */
    public void responseHttpBarrio(List<Barrio> barrioList){
        listBarrio = new ArrayList<>();
        listBarrio.addAll(barrioList);
    }

    public void responseHttpBarrio_2(List<Barrio> barrioList){
        listBarrio_2 = new ArrayList<>();
        listBarrio_2.addAll(barrioList);
    }

    public void register(){
        if(validateRegister()){
            intentRepeat="";//para que procese el ultimo caso
            //if(mPreferences.isLoggedIn(getActivity())) {
            Log.e("onError", "1111111111");
            String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());
            if(jsonPerfil!=null){
                Perfil perfil = new Gson().fromJson(jsonPerfil, Perfil.class);
                if ((perfil != null) && (perfil.getPerfil().equals(Perfil.LIDER) || perfil.getPerfil().equals(Perfil.GERENTE_ZONA))) {
                    Log.e("onError", "2222222222");
                    showVolante(txtName.getText().toString()+" "+txtLastname.getText().toString());
                } else {
                    Log.e("onError", "333333333");
                    searchTermins();
                }
            } else{
                searchTermins();
            }
        }
    }

    private void sendDataRegister(){
        new Http(getActivity()).register(obtainDataUser(), TAG, BROACAST_REG_TYPE_REGISTER_VOLANTE, BROACAST_REG_ERROR);
    }

    MH_Dialogs_Termins showTermins;
    public void showTerminsDialog(String termins) {
        showTermins = MH_Dialogs_Termins.newInstance();
        showTermins.loadData(obtainDataUser(), termins, this);
        showTermins.show(getActivity().getSupportFragmentManager(), "fragment_edit_name");
    }

    MH_Dialogs_Volante dialogsVolante;
    public void showVolante(String asesora){
        dialogsVolante = new MH_Dialogs_Volante();
        dialogsVolante.loadData(asesora, this);
        dialogsVolante.show(getActivity().getSupportFragmentManager(),"mDialog");
    }

    public void dissmissDialogTermins(){
        showTermins.dismiss();
    }

    public void dissmissDialogVolante(){
        dialogsVolante.dismiss();
    }




    private void searchTermins(){
        showProgressDialog();
        RequiredTermins requiredTermins = new RequiredTermins(txtIdenty.getText().toString(), txtCellphone.getText().toString(), txtEmail.getText().toString());
        new Http(getActivity()).termins(requiredTermins, TAG, BROACAST_REG_TYPE_TERMINS, BROACAST_REG_ERROR);
    }

    public Boolean validateRegister()
    {
        Validate valid=new Validate();
        //datos personales
        if (txtSpnTypeId.getText().toString().isEmpty())
        {
            msgToast("Seleccione tipo de documento...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnTypeId);
            return false;
        }
        else if (valid.isValidInteger(txtIdenty.getText().toString()))
        {
            msgToast("Cédula invalida...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtIdenty);
            return false;
        }else if(!isRefValidated()){
            msgToast("Debe validar la cédula... Verifique");
            valid.setLoginError(getString(R.string.deba_validar),txtIdenty);
            return false;
        }
        else if (txtName.getText().toString().isEmpty())
        {
            msgToast("Nombre invalido...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtName);
            return false;
        }
        else if (txtLastname.getText().toString().isEmpty())
        {
            msgToast("Apellido invalido...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtLastname);
            return false;
        }
        else if (txtDateBird.getText().toString().isEmpty())
        {
            msgToast("Seleccione fecha de nacimiento...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtDateBird);
            return false;
        }
        else if (txtZone.getText().toString().isEmpty())
        {
            msgToast("Zona seccion... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtZone);
            return false;
        }

        //REDIDENCIA
        else if (txtSpnTypeVia.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Tipo de vía... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnTypeVia);
            return false;
        }
        else if (!txtSpnTypeVia.getText().toString().equals("Otro") && txtNum1.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 1... Verifiqu");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum1);
            return false;
        }
        else if (!txtSpnTypeVia.getText().toString().equals("Otro") && txtNum2.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 2... Verifiqu");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum2);
            return false;
        }
        else if (!txtSpnTypeVia.getText().toString().equals("Otro") && txtNum3.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 3... Verifiqu");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum3);
            return false;
        }
        else if (txtSpnTypeVia.getText().toString().equals("Otro") && txtInfo.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Datos adicionales...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtInfo);
            return false;
        }

        //departamento
        else if (txtSpnDpto.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Dpto... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnDpto);
            return false;
        }
        else if (txtSpnCity.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Ciudad... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnCity);
            return false;
        }
        else if (txtSpnBarrio.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Barrio... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnBarrio);
            return false;
        }

        //DIRECCION DE ENVIO DE PEDIDOS
        else if (ctxDirSend.getVisibility()==View.VISIBLE) {
            if (txtSpnTypeVia_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Tipo de vía... Verifique");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnTypeVia_2);
                return false;
            }
            else if (!txtSpnTypeVia_2.getText().toString().equals("Otro") && txtNum1_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 1... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum1_2);
                return false;
            }
            else if (!txtSpnTypeVia_2.getText().toString().equals("Otro") && txtNum2_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 2... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum2_2);
                return false;
            }
            else if (!txtSpnTypeVia_2.getText().toString().equals("Otro") && txtNum3_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 3... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum3_2);
                return false;
            }
            else if (txtSpnTypeVia_2.getText().toString().equals("Otro") && txtInfo_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Datos adicionales...");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtInfo_2);
                return false;
            }

            //departamento
            else if (txtSpnDpto_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Dpto... Verifique");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnDpto_2);
                return false;
            }
            else if (txtSpnCity_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Ciudad... Verifique");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnCity_2);
                return false;
            }
            else if (txtSpnBarrio_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Barrio... Verifique");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtSpnBarrio_2);
                return false;
            }
        }

        //contacto
        else if (txtPhone.getText().toString().isEmpty())
        {
            msgToast("Teléfono fijo... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtPhone);
            return false;
        }
        else if (txtCellphone.getText().toString().isEmpty())
        {
            msgToast("Teléfono movil... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtPhone);
            return false;
        }
        else if (txtCellphone.getText().toString().length() < 10)
        {
            msgToast("Teléfono movil (10 números)... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtCellphone);
            return false;
        }
        else if (txtPhone.getText().toString().equals(txtCellphone.getText().toString()))
        {
            msgToast("Teléfonos deben ser diferentes... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtCellphone);
            return false;
        }
        else if (valid.isValidEmail(txtEmail.getText().toString()))
        {
            msgToast("Formato de correo incorrecto... Verifique");
            valid.setLoginError(getResources().getString(R.string.formato_incorrecto), txtEmail);
            return false;
        }

        return true;

    }

    public RequiredRegister obtainDataUser()
    {

        RequiredRegister dataRegisterModel = new RequiredRegister(
            "EMPTY",
                String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnTypeId.getText().toString())),
                    txtIdenty.getText().toString(),
                    txtName.getText().toString(),
                    txtLastname.getText().toString(),
                    txtSpnDpto.getText().toString(),
                    //departamento = pickerDpto.SelectedIndex != -1 ? pickerDpto.Items[pickerDpto.SelectedIndex] : "",
                    txtZone.getText().toString(),
                    //zona_seccion = txtZona.Text.Trim(),
                    ciudad.getId_ciudad(),
                    //id_ciudad = dataMainModel.departamentos[pickerDpto.SelectedIndex].ciudades[pickerCity.SelectedIndex].id_ciudad,
                    ciudad.getName_ciudad(),
                    //name_ciudad = dataMainModel.departamentos[pickerDpto.SelectedIndex].ciudades[pickerCity.SelectedIndex].name_ciudad,
                    barrio.getName_barrio(),
                    //barrio = rowSelectedBarrioA.name_barrio,
                    barrio.getId_barrio(),
                    //id_barrio = rowSelectedBarrioA.id_barrio,
                    txtPhone.getText().toString(),
                    //telefono = txtPhone.Text.Trim(),
                    txtCellphone.getText().toString(),
                    //celular = txtCellphone.Text.Trim(),
                    txtEmail.getText().toString(),
                    //correo =  emailValidator.IsValid ? txtEmail.Text.Trim() : "",
                    txtDateBird.getText().toString(),
                    //nacimiento = datePickDate.Date.ToString(),
                    "52486488956555",
                    //imei = "52486488956555",
                    txtSpnTypeVia.getText().toString(),
                    //tipo_via = pickerTipoVia.SelectedIndex != -1 ? pickerTipoVia.Items[pickerTipoVia.SelectedIndex] : "",
                    txtNum1.getText().toString(),
                    //numero1 = (pickerBehavior_via.SelectedItem.Equals("Otro") ? "" : txtNum1.Text.Trim()),
                    //letra1 = pickerLetra1.SelectedIndex != -1 ? pickerLetra1.Items[pickerLetra1.SelectedIndex] : "",
                    txtSpnChar1.getText().toString(),
                    //bis = pickerBis.SelectedIndex != -1 ? pickerBis.Items[pickerBis.SelectedIndex] : "",
                    txtSpnBis.getText().toString(),
                    //numero2 = (pickerBehavior_via.SelectedItem.Equals("Otro") ? "" : txtNum2.Text.Trim()),
                    txtNum2.getText().toString(),
                    //letra2 = pickerLetra2.SelectedIndex != -1 ? pickerLetra2.Items[pickerLetra2.SelectedIndex] : "",
                    txtSpnChar2.getText().toString(),
                    //numero3 = (pickerBehavior_via.SelectedItem.Equals("Otro") ? "" : txtNum3.Text.Trim()),
                    txtNum3.getText().toString(),
                    //pcardinal = pickerEsteOeste.SelectedIndex != -1 ? pickerEsteOeste.Items[pickerEsteOeste.SelectedIndex] : "",
                    txtSpnSur.getText().toString(),
                    //complemento = (!pickerBehavior_via.SelectedItem.Equals("Otro") ? "" : txtComp.Text.Trim()),
                    txtInfo.getText().toString(),
                    txtConcatenateDir.getText().toString(),
                    ctxDirSend.getVisibility()==View.GONE ? "" : txtConcatenateDir_2.getText().toString(),
                    ctxDirSend.getVisibility()==View.GONE ? "" : barrio_2.getId_barrio()
        );

        dataRegisterModel.setDireccion_concatenada(
                dataRegisterModel.getTipo_via() + " " +
                        (dataRegisterModel.getNumero1() + " ") +
                        (dataRegisterModel.getLetra1() + " ") +
                        (dataRegisterModel.getBis() + " ") +
                        (dataRegisterModel.getNumero2() + " ") +
                        (dataRegisterModel.getLetra2() + " ") +
                        (dataRegisterModel.getNumero3() + " ") +
                        (dataRegisterModel.getPcardinal() + " ") +
                        dataRegisterModel.getComplemento());

        return dataRegisterModel;
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    TextWatcher mChangeText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            txtConcatenateDir.setText(concatenateDir());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher mChangeText_2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            txtConcatenateDir_2.setText(concatenateDir_2());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public String concatenateDir()
    {
       return txtSpnTypeVia.getText().toString() + " " +
               (txtNum1.getText().toString() + " ") +
               (txtSpnChar1.getText().toString() + " ") +
               (txtSpnBis.getText().toString() + " ") +
               (txtNum2.getText().toString() + " ") +
               (txtSpnChar2.getText().toString() + " ") +
               (txtNum3.getText().toString() + " ") +
               (txtSpnSur.getText().toString() + " ") +
               txtInfo.getText().toString();
    }

    public String concatenateDir_2()
    {
        return txtSpnTypeVia_2.getText().toString() + " " +
                (txtNum1_2.getText().toString() + " ") +
                (txtSpnChar1_2.getText().toString() + " ") +
                (txtSpnBis_2.getText().toString() + " ") +
                (txtNum2_2.getText().toString() + " ") +
                (txtSpnChar2_2.getText().toString() + " ") +
                (txtNum3_2.getText().toString() + " ") +
                (txtSpnSur_2.getText().toString() + " ") +
                txtInfo_2.getText().toString();
    }



    private void clearAllData(){
        txtSpnTypeId.setText("");
        txtIdenty.setText("");
        txtInputRiesgo.setHint(getResources().getString(R.string.cedula));

        txtName.setText("");
        txtLastname.setText("");
        txtDateBird.setText("");
        txtZone.setText("");

        //Direccion residencia
        txtConcatenateDir.setText("");
        txtConcatenateDir_2.setText("");

        txtSpnTypeVia.setText("");
        txtNum1.setText("");
        txtSpnChar1.setText("");
        txtSpnBis.setText("");
        txtNum2.setText("");
        txtSpnChar2.setText("");
        txtNum3.setText("");
        txtSpnSur.setText("");
        txtInfo.setText("");
        txtSpnDpto.setText("");
        txtSpnCity.setText("");
        txtSpnBarrio.setText("");

        //Direccion de envio
        txtSpnDirSend.setText("");
        txtSpnTypeVia_2.setText("");
        txtNum1_2.setText("");
        txtSpnChar1_2.setText("");
        txtSpnBis_2.setText("");
        txtNum2_2.setText("");
        txtSpnChar2_2.setText("");
        txtNum3_2.setText("");
        txtSpnSur_2.setText("");
        txtInfo_2.setText("");
        txtSpnDpto_2.setText("");
        txtSpnCity_2.setText("");
        txtSpnBarrio_2.setText("");

        //Contacto
        txtPhone.setText("");
        txtCellphone.setText("");
        txtEmail.setText("");

        setRefValidated(false);
    }

    private boolean refValidated=false;

    public boolean isRefValidated() {
        return refValidated;
    }

    public void setRefValidated(boolean refValidated) {
        this.refValidated = refValidated;

        intentRepeat="";
        txtIdenty.setEnabled(!refValidated);
        txtIdenty.setError(null);

        imgB_Searchref.setVisibility(refValidated ? View.GONE : View.VISIBLE);
        imgB_CleanRef.setVisibility(!refValidated ? View.GONE : View.VISIBLE);
        txtIdenty.setText(refValidated ? txtIdenty.getText().toString() : "");

        txtInputRiesgo.setHint(refValidated ? txtInputRiesgo.getHint() : getString(R.string.cedula));

        //habilitando campos debajo
        txtName.setEnabled(refValidated);
        txtLastname.setEnabled(refValidated);
        txtDateBird.setEnabled(refValidated);
        txtZone.setEnabled(refValidated);

        //Direccion residencia
        txtConcatenateDir.setEnabled(refValidated);
        txtConcatenateDir_2.setEnabled(refValidated);

        txtSpnTypeVia.setEnabled(refValidated);
        txtNum1.setEnabled(refValidated);
        txtSpnChar1.setEnabled(refValidated);
        txtSpnBis.setEnabled(refValidated);
        txtNum2.setEnabled(refValidated);
        txtSpnChar2.setEnabled(refValidated);
        txtNum3.setEnabled(refValidated);
        txtSpnSur.setEnabled(refValidated);
        txtInfo.setEnabled(refValidated);
        txtSpnDpto.setEnabled(refValidated);
        txtSpnCity.setEnabled(refValidated);
        txtSpnBarrio.setEnabled(refValidated);

        //Direccion de envio
        txtSpnDirSend.setEnabled(refValidated);
        txtSpnTypeVia_2.setEnabled(refValidated);
        txtNum1_2.setEnabled(refValidated);
        txtSpnChar1_2.setEnabled(refValidated);
        txtSpnBis_2.setEnabled(refValidated);
        txtNum2_2.setEnabled(refValidated);
        txtSpnChar2_2.setEnabled(refValidated);
        txtNum3_2.setEnabled(refValidated);
        txtSpnSur_2.setEnabled(refValidated);
        txtInfo_2.setEnabled(refValidated);
        txtSpnDpto_2.setEnabled(refValidated);
        txtSpnCity_2.setEnabled(refValidated);
        txtSpnBarrio_2.setEnabled(refValidated);

        //Contacto
        txtPhone.setEnabled(refValidated);
        txtCellphone.setEnabled(refValidated);
        txtEmail.setEnabled(refValidated);

    }

    public void snackBar(String msg){
        Perfil perfil = getPerfil();

        //no se xq en cada actividad funciona diferente
        Snackbar.make(perfil==null ? getActivity().findViewById(android.R.id.content) : ctnRegister, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).setDuration(5000).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG,"new setUserVisibleHint(): "+String.valueOf(isVisibleToUser));
        /*if(isVisibleToUser)
            registerBroadcat();
        else
            registerBroadcat();*/
    }
}
