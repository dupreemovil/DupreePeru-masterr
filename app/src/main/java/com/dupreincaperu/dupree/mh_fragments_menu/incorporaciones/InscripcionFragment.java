package com.dupreincaperu.dupree.mh_fragments_menu.incorporaciones;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_dialogs.ListString;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.Referencia;
import com.dupreincaperu.dupree.mh_required_api.RequiredInscription;
import com.dupreincaperu.dupree.mh_required_api.RequiredValidateRef;
import com.dupreincaperu.dupree.mh_utilities.Validate;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscripcionFragment extends Fragment {
    public static final String TAG = "InscripcionFragment";
    public static final String BROACAST_INSCRIP_TYPE_VALIDATE_REF="inscrip_type_validate_ref";
    public static final String BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH ="inscrip_type_path_ced_front";
    public static final String BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH ="inscrip_type_path_ced_adver";
    public static final String BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH ="inscrip_type_path_pag_front";
    public static final String BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH ="inscrip_type_path_pag_adver";

    public static final String BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL="inscrip_type_img_ced_front_url";
    public static final String BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL="inscrip_type_img_ced_adver_url";
    public static final String BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL="inscrip_type_img_pag_front_url";
    public static final String BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL="inscrip_type_img_pag_adver_url";

    public static final String BROACAST_INSCRIP_SUSSCCEFUL="inscrip_type_susscceful";

    public static final String BROACAST_INSCRIP_ERROR="inscrip_type_error";

    private final int IMG_CED_FRT=0, IMG_CED_ADV=1, IMG_PAG_FRT=2, IMG_PAG_ADV=3;
    public InscripcionFragment() {
        // Required empty public constructor
    }

    public static InscripcionFragment newInstance() {
        Bundle args = new Bundle();

        InscripcionFragment fragment = new InscripcionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void loadData(String name, String cedula){
        txtNameIncrip.setText(name.isEmpty() ? Incorp_ListPre_Fragment.nameSelected : name);//se redunda xq a veces no llega
        txtIdentyCard.setText(cedula.isEmpty() ? Incorp_ListPre_Fragment.identySelected : cedula);
    }

    EditText txtNameIncrip, txtIdentyCard;

    EditText txtIdentyCardRef;
    TextInputLayout txtInputNameReferido;
    ImageButton imgB_Searchref, imgB_CleanRef;

    FrameLayout ctnInscription;
    ImageView imgCedFrontal, imgCedAdverso, imgPagFrontal, imgPagAdverso;

    EditText /*txtSpnTypeDoc1, txtIdentyCard1, */txtName1, txtLastName1, txtPhone1, txtCellPhone1, txtSpnParentesco1;
    EditText /*txtSpnTypeDoc2, txtIdentyCard2, */txtName2, txtLastName2, txtPhone2, txtCellPhone2, txtSpnParentesco2;

    Button btnRegister;

    //private List<String> path_image;
    private List<String> url_image;
    private File file0, file1, file2, file3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inscripcion, container, false);

        ctnInscription = v.findViewById(R.id.ctnInscription);

        txtNameIncrip = v.findViewById(R.id.txtNameIncrip);
        txtIdentyCard = v.findViewById(R.id.txtIdentyCard);

        txtIdentyCardRef = v.findViewById(R.id.txtIdentyCardRef);
        txtInputNameReferido = v.findViewById(R.id.txtInputNameReferido);

        imgB_Searchref = v.findViewById(R.id.imgB_Searchref);
        imgB_CleanRef = v.findViewById(R.id.imgB_CleanRef);
        imgB_Searchref.setOnClickListener(mListenerClick);
        imgB_CleanRef.setOnClickListener(mListenerClick);

        imgCedFrontal = v.findViewById(R.id.imgCedFrontal);
        imgCedAdverso = v.findViewById(R.id.imgCedAdverso);
        imgPagFrontal = v.findViewById(R.id.imgPagFrontal);
        imgPagAdverso = v.findViewById(R.id.imgPagAdverso);
        imgCedFrontal.setOnClickListener(mListenerClick);
        imgCedAdverso.setOnClickListener(mListenerClick);
        imgPagFrontal.setOnClickListener(mListenerClick);
        imgPagAdverso.setOnClickListener(mListenerClick);

        /*txtSpnTypeDoc1 = v.findViewById(R.id.txtSpnTypeDoc1);
        txtSpnTypeDoc1.setOnClickListener(mListenerClick);
        txtIdentyCard1 = v.findViewById(R.id.txtIdentyCard1);*/
        txtName1 = v.findViewById(R.id.txtName1);
        txtLastName1 = v.findViewById(R.id.txtLastName1);
        txtPhone1 = v.findViewById(R.id.txtPhone1);
        txtCellPhone1 = v.findViewById(R.id.txtCellPhone1);
        txtSpnParentesco1 = v.findViewById(R.id.txtSpnParentesco1);
        txtSpnParentesco1.setOnClickListener(mListenerClick);

        /*txtSpnTypeDoc2 = v.findViewById(R.id.txtSpnTypeDoc2);
        txtSpnTypeDoc2.setOnClickListener(mListenerClick);
        txtIdentyCard2 = v.findViewById(R.id.txtIdentyCard2);*/
        txtName2 = v.findViewById(R.id.txtName2);
        txtLastName2 = v.findViewById(R.id.txtLastName2);
        txtPhone2 = v.findViewById(R.id.txtPhone2);
        txtCellPhone2 = v.findViewById(R.id.txtCellPhone2);
        txtSpnParentesco2 = v.findViewById(R.id.txtSpnParentesco2);
        txtSpnParentesco2.setOnClickListener(mListenerClick);

        btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(mListenerClick);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        /*path_image = new ArrayList<>();
        path_image.add("");
        path_image.add("");
        path_image.add("");
        path_image.add("");*/

        url_image = new ArrayList<>();
        url_image.add("");
        url_image.add("");
        url_image.add("");
        url_image.add("");

        //file = new ArrayList<>();

        createProgress();

        return v;
    }

    View.OnClickListener mListenerClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                //case R.id.txtSpnTypeDoc1:
                    ///showList(ListString.BROACAST_INSCRIP_TYPE_DOC, getString(R.string.tipo_de_documento), Arrays.asList(getResources().getStringArray(R.array.typeIdenty)), txtSpnTypeDoc1.getText().toString());
                    //break;
                case R.id.txtSpnParentesco1:
                    showList(ListString.BROACAST_INSCRIP_TYPE_PARENTESCO, getString(R.string.tipo_de_documento), Arrays.asList(getResources().getStringArray(R.array.parentescoOptions)), txtSpnParentesco1.getText().toString());
                    break;
                //case R.id.txtSpnTypeDoc2:
                    //showList(ListString.BROACAST_INSCRIP_TYPE_DOC_2, getString(R.string.tipo_de_documento), Arrays.asList(getResources().getStringArray(R.array.typeIdenty)), txtSpnTypeDoc2.getText().toString());
                    //break;
                case R.id.txtSpnParentesco2:
                    showList(ListString.BROACAST_INSCRIP_TYPE_PARENTESCO_2, getString(R.string.tipo_de_documento), Arrays.asList(getResources().getStringArray(R.array.parentescoOptions)), txtSpnParentesco2.getText().toString());
                    break;
                case R.id.btnRegister:
                    if(validateIncrip()){
                        intentRepeat="";
                        //sendImageMultiPart(path_image.get(IMG_CED_FRT),TAG, BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL);
                        showProgressDialog();
                        sendImageMultiPart(file0, TAG, BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL);
                    }
                    break;
                case R.id.imgB_Searchref:
                    if(!txtIdentyCardRef.getText().toString().isEmpty()) {
                        new Http(getActivity()).validateReferido(
                                new RequiredValidateRef(
                                        txtIdentyCardRef.getText().toString(),
                                        mPreferences.getTokenSesion(getActivity())
                                ),
                                TAG,
                                BROACAST_INSCRIP_TYPE_VALIDATE_REF
                        );
                    }
                    break;
                case R.id.imgB_CleanRef:
                    setRefValidated(false);
                    break;
                case R.id.imgCedFrontal:
                    imageSelected=BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH);
                    break;
                case R.id.imgCedAdverso:
                    imageSelected=BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH);
                    break;
                case R.id.imgPagFrontal:
                    imageSelected=BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH);
                    break;
                case R.id.imgPagAdverso:
                    imageSelected=BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH);
                    break;

            }
        }
    };




    //

    //List<String> data = new ArrayList<String>();
    public void showList(String objectFragment, String title, List<String> data, String itemSelected){
        ListString dialogList = new ListString();
        dialogList.loadData(TAG, objectFragment, title, data, itemSelected);
        dialogList.show(getActivity().getSupportFragmentManager(),"mDialog");
    }


    private boolean refValidated=false;
    private boolean imgCedFrontalON=false;
    private boolean imgCedAdverosON=false;
    private boolean imgPagFrontalON=false;
    private boolean imgPagAdverosON=false;

    private boolean validateIncrip(){
        Validate validate = new Validate();

        if(txtNameIncrip.getText().toString().isEmpty()){
            msgToast("Nombre de asesora... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtNameIncrip);
            return false;
        }else if(txtIdentyCard.getText().toString().isEmpty()){
            msgToast("Cêdula de asesora... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtIdentyCard);
            return false;
        }
        else if(txtIdentyCardRef.getText().toString().isEmpty()){
            msgToast("Cêdula de referido... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtIdentyCardRef);
            return false;
        }else if(!isRefValidated()){
            msgToast("Debe validar la cédula del referido... Verifique");
            validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }
        else if(file0==null){
            msgToast("Cédula frontal... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgCedAdverosON()){
        else if(file1==null){
            msgToast("Cédula adverso... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgPagFrontalON()){
        else if(file2==null){
            msgToast("pagaré frontal... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgPagAdverosON()){
        else if(file3==null){
            msgToast("pagaré adverso... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }

        /*else if(txtSpnTypeDoc1.getText().toString().isEmpty()){
            msgToast("tipo de cêdula de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtSpnTypeDoc1);
            return false;
        }
        else if(txtIdentyCard1.getText().toString().isEmpty()){
            msgToast("Cêdula de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtIdentyCard1);
            return false;
        }*/
        else if(txtName1.getText().toString().isEmpty()){
            msgToast("Nombre de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtName1);
            return false;
        }
        else if(txtLastName1.getText().toString().isEmpty()){
            msgToast("Apellido de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtLastName1);
            return false;
        }
        else if(txtPhone1.getText().toString().isEmpty()){
            msgToast("Teléfono de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtPhone1);
            return false;
        }
        else if(txtCellPhone1.getText().toString().isEmpty()){
            msgToast("Celular de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtCellPhone1);
            return false;
        }
        else if(txtSpnParentesco1.getText().toString().isEmpty()){
            msgToast("Parentesco de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtSpnParentesco1);
            return false;
        }

        /*
        else if(txtSpnTypeDoc2.getText().toString().isEmpty()){
            msgToast("tipo de cêdula de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtSpnTypeDoc2);
            return false;
        }
        else if(txtIdentyCard2.getText().toString().isEmpty()){
            msgToast("Cêdula de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtIdentyCard2);
            return false;
        }*/
        else if(txtName2.getText().toString().isEmpty()){
            msgToast("Nombre de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtName2);
            return false;
        }
        else if(txtLastName2.getText().toString().isEmpty()){
            msgToast("Apellido de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtLastName2);
            return false;
        }
        else if(txtPhone2.getText().toString().isEmpty()){
            msgToast("Teléfono de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtPhone2);
            return false;
        }
        else if(txtCellPhone2.getText().toString().isEmpty()){
            msgToast("Celular de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtCellPhone2);
            return false;
        }
        else if(txtSpnParentesco2.getText().toString().isEmpty()){
            msgToast("Parentesco de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtSpnParentesco2);
            return false;
        }

        return true;
    }

    public boolean isRefValidated() {
        return refValidated;
    }

    public void setRefValidated(boolean refValidated) {
        this.refValidated = refValidated;

        intentRepeat="";
        txtIdentyCardRef.setEnabled(!refValidated);
        txtIdentyCardRef.setError(null);

        imgB_Searchref.setVisibility(refValidated ? View.GONE : View.VISIBLE);
        imgB_CleanRef.setVisibility(!refValidated ? View.GONE : View.VISIBLE);
        txtIdentyCardRef.setText(refValidated ? txtIdentyCardRef.getText().toString() : "");
        txtInputNameReferido.setHint(refValidated ? txtInputNameReferido.getHint() : getString(R.string.cedula));
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

    private static String intentRepeat="";
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }

            if (intentRepeat.equals(intent.getExtras().toString())) {
                Log.e(TAG, "repeat: " + intent.getExtras().toString());
                return;
            }
            intentRepeat = intent.getExtras().toString();

            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {
                    //Datos personales
                    case ListString.BROACAST_INSCRIP_TYPE_DOC:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_DOC");
                        //txtSpnTypeDoc1.setError(null);
                        //txtSpnTypeDoc1.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_INSCRIP_TYPE_DOC_2:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_DOC_2");
                        //txtSpnTypeDoc2.setError(null);
                        //txtSpnTypeDoc2.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_INSCRIP_TYPE_PARENTESCO:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_PARENTESCO");
                        txtSpnParentesco1.setError(null);
                        txtSpnParentesco1.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case ListString.BROACAST_INSCRIP_TYPE_PARENTESCO_2:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_PARENTESCO_2");
                        txtSpnParentesco2.setError(null);
                        txtSpnParentesco2.setText(intent.getStringExtra(ListString.BROACAST_DATA));
                        break;
                    case BROACAST_INSCRIP_TYPE_VALIDATE_REF:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_VALIDATE_REF");
                        String nombreReferido = intent.getStringExtra(ListString.BROACAST_DATA);
                        txtInputNameReferido.setHint(nombreReferido);
                        setRefValidated(true);
                        break;
                    /*case BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH");
                        String patch0 = intent.getStringExtra(ListString.BROACAST_DATA);
                        path_image.add(IMG_CED_FRT, patch0);
                        convertPathToImage(patch0, imgCedFrontal);
                        setImgCedFrontalON(true);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH");
                        String patch1 = intent.getStringExtra(ListString.BROACAST_DATA);
                        path_image.add(IMG_CED_ADV, patch1);
                        convertPathToImage(patch1, imgCedAdverso);
                        setImgCedAdverosON(true);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH");
                        String patch2 = intent.getStringExtra(ListString.BROACAST_DATA);
                        path_image.add(IMG_PAG_FRT, patch2);
                        convertPathToImage(patch2, imgPagFrontal);
                        setImgPagFrontalON(true);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH");
                        String patch3 = intent.getStringExtra(ListString.BROACAST_DATA);
                        path_image.add(IMG_PAG_ADV, patch3);
                        convertPathToImage(patch3, imgPagAdverso);
                        setImgPagAdverosON(true);
                        break;*/
                    case BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL");
                        String url0 = intent.getStringExtra(ListString.BROACAST_DATA);
                        url_image.add(IMG_CED_FRT, url0);
                        //sendImageMultiPart(path_image.get(IMG_CED_ADV),TAG, BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL);
                        sendImageMultiPart(file1,TAG, BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL);

                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL");
                        String url1 = intent.getStringExtra(ListString.BROACAST_DATA);
                        url_image.add(IMG_CED_ADV, url1);
                        //sendImageMultiPart(path_image.get(IMG_PAG_FRT),TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                        sendImageMultiPart(file2,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL");
                        String url2 = intent.getStringExtra(ListString.BROACAST_DATA);
                        url_image.add(IMG_PAG_FRT, url2);
                        //sendImageMultiPart(path_image.get(IMG_PAG_ADV),TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                        sendImageMultiPart(file3,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL");
                        String url3 = intent.getStringExtra(ListString.BROACAST_DATA);
                        url_image.add(IMG_PAG_ADV, url3);
                        // ya se enviaron las 4 imagenes, ahora se envian los url con el resto de los datos

                        new Http(getActivity()).inscribir(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                        break;
                    case BROACAST_INSCRIP_SUSSCCEFUL:
                        dismissProgressDialog();
                        String result = intent.getStringExtra(ListString.BROACAST_DATA);
                        msgToast(result);
                        clearData();
                        new Http(getActivity()).enlaceUpdateList();
                        //txtIdentyCardRef.setfocus
                        break;
                    case BROACAST_INSCRIP_ERROR:
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

    public void error(){
        dismissProgressDialog();
    }



    private RequiredInscription obtainData(){
        List<Referencia> referenciaList = new ArrayList<>();
        Referencia refPersonal = new Referencia();
        refPersonal.setTipo_documento_id(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnParentesco1.getText().toString())));
        //refPersonal.setTipo_documento_id(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnTypeDoc1.getText().toString())));
        //refPersonal.setCedula(txtIdentyCard1.getText().toString());
        refPersonal.setCedula("");
        refPersonal.setNombre(txtName1.getText().toString());
        refPersonal.setApellido(txtLastName1.getText().toString());
        refPersonal.setTelefono(txtPhone1.getText().toString());
        refPersonal.setCelular(txtCellPhone1.getText().toString());
        refPersonal.setParentesco(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnParentesco1.getText().toString())));
        referenciaList.add(refPersonal);
        Log.i(TAG, "JSON 1"+new Gson().toJson(refPersonal));

        Referencia refFamiliar = new Referencia();
        refFamiliar.setTipo_documento_id(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnParentesco2.getText().toString())));
        //refFamiliar.setTipo_documento_id(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnTypeDoc2.getText().toString())));
        //refFamiliar.setCedula(txtIdentyCard2.getText().toString());
        refFamiliar.setCedula("");
        refFamiliar.setNombre(txtName2.getText().toString());
        refFamiliar.setApellido(txtLastName2.getText().toString());
        refFamiliar.setTelefono(txtPhone2.getText().toString());
        refFamiliar.setCelular(txtCellPhone2.getText().toString());
        refFamiliar.setParentesco(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnParentesco2.getText().toString())));
        referenciaList.add(refFamiliar);
        Log.i(TAG, "JSON 2"+new Gson().toJson(refFamiliar));

        RequiredInscription requiredInscription = new RequiredInscription();
        requiredInscription.setCedula(Validate.stringToLong(txtIdentyCard.getText().toString()));
        requiredInscription.setImg_cedula(url_image.subList(0,2));
        requiredInscription.setPagare(url_image.subList(2,4));
        requiredInscription.setReferenciado_por(txtIdentyCardRef.getText().toString());
        requiredInscription.setUser(txtInputNameReferido.getHint()!= null ? txtInputNameReferido.getHint().toString() : "");
        requiredInscription.setReferencia(referenciaList);
        Log.i(TAG, "JSON 3"+new Gson().toJson(requiredInscription));

        return requiredInscription;
    }



    public void convertPathToImage(String pathImage, ImageView image){
        /*File imgFile = new  File(pathImage);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);
        } else {
            msgToast("Ocurrio un problema al leer la imagen");
        }*/
        image.setImageBitmap(resizeBitmap(pathImage, 256, 256));//para mostrar en pantalla con poca resolusion 960, 1200 en xamarin para enviar, 256x256 para mostrar
    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;//potencias de 2 (1= mitad , 4 , carta parte, 8 octaba parte)
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    private File convertFileFromBitmap(Bitmap bitmap){
        //create a file to write bitmap data
        try {
            File f = new File(getActivity().getCacheDir(), Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            f.createNewFile();
            //Convert bitmap to byte array
            //Bitmap bitmap = bitmap;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            return f;
        }catch (IOException e){
            return null;
        }

    }
    //sendImageMultiPart(pathImage, RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE);
    private void sendImageMultiPart(String filePath, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_file(
                filePath, tagFragment, objectFragment
        );
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public boolean isImgCedFrontalON() {
        return imgCedFrontalON;
    }

    public void setImgCedFrontalON(boolean imgCedFrontalON) {
        this.imgCedFrontalON = imgCedFrontalON;
    }

    public boolean isImgCedAdverosON() {
        return imgCedAdverosON;
    }

    public void setImgCedAdverosON(boolean imgCedAdverosON) {
        this.imgCedAdverosON = imgCedAdverosON;
    }

    public boolean isImgPagFrontalON() {
        return imgPagFrontalON;
    }

    public void setImgPagFrontalON(boolean imgPagFrontalON) {
        this.imgPagFrontalON = imgPagFrontalON;
    }

    public boolean isImgPagAdverosON() {
        return imgPagAdverosON;
    }

    public void setImgPagAdverosON(boolean imgPagAdverosON) {
        this.imgPagAdverosON = imgPagAdverosON;
    }

    private void clearData(){
        txtNameIncrip.setText("");
        txtIdentyCard.setText("");

        txtIdentyCardRef.setText("");
        txtInputNameReferido.setHint(getResources().getString(R.string.cedula));

        imgCedFrontal.setImageResource(R.drawable.ph_add_image2);
        imgCedAdverso.setImageResource(R.drawable.ph_add_image2);
        imgPagFrontal.setImageResource(R.drawable.ph_add_image2);
        imgPagAdverso.setImageResource(R.drawable.ph_add_image2);

        //txtSpnTypeDoc1.setText("");
        //txtIdentyCard1.setText("");
        txtName1.setText("");
        txtLastName1.setText("");
        txtPhone1.setText("");
        txtCellPhone1.setText("");
        txtSpnParentesco1.setText("");

        //txtSpnTypeDoc2.setText("");
        //txtIdentyCard2.setText("");
        txtName2.setText("");
        txtLastName2.setText("");
        txtPhone2.setText("");
        txtCellPhone2.setText("");
        txtSpnParentesco2.setText("");

        /*path_image = new ArrayList<>();
        path_image.add("");
        path_image.add("");
        path_image.add("");
        path_image.add("");*/
        file0=null;
        file1=null;
        file2=null;
        file3=null;

        url_image = new ArrayList<>();
        url_image.add("");
        url_image.add("");
        url_image.add("");
        url_image.add("");

        setImgPagAdverosON(false);
        setRefValidated(false);
        setImgPagFrontalON(false);
        setImgCedAdverosON(false);
        setImgCedFrontalON(false);
    }

    private File createNewFile(String prefix){
        if(prefix==null || "".equalsIgnoreCase(prefix)){
            prefix="IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory()+"/mypics/");
        if(!newDirectory.exists()){
            if(newDirectory.mkdir()){
                Log.d(getActivity().getClass().getName(), newDirectory.getAbsolutePath()+" directory created");
            }
        }
        File file = new File(newDirectory,(prefix+ System.currentTimeMillis()+".jpg"));
        if(file.exists()){
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    private void copyFile(File sourceFile, File destFile)  {
        try {
            if (!sourceFile.exists()) {
                return;
            }

            FileChannel source = null;
            FileChannel destination = null;
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            if (destination != null && source != null) {
                destination.transferFrom(source, 0, source.size());
            }
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }catch (FileNotFoundException e){
            msgToast("Enorelis error 1");

        }catch (IOException e){
            msgToast("Enorelis error 2");
        }
    }

    private class fileFromBitmap extends AsyncTask<Void, Integer, String> {
        File file;
        Context context;
        Bitmap bitmap;
        int indexFile=0;
        String name;
        public fileFromBitmap(Bitmap bitmap, String name, int indexFile, Context context) {
            this.bitmap = bitmap;
            this.name= name;
            this.indexFile= indexFile;
            this.context= context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            //bitmap = resize(bitmap, 1406, 2500);
            //bitmap = resize(bitmap, 960, 1200);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            //si la quieres en discofile = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            file = new File(getActivity().getCacheDir() + "dupree_"+name+"_temporary_file"+String.valueOf(indexFile)+".jpg");//en cache
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            saveFile(file, indexFile);
        }

        private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
            if (maxHeight > 0 && maxWidth > 0) {
                int width = image.getWidth();
                int height = image.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > 1) {
                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, false);
                return image;
            } else {
                return image;
            }
        }
    }

    private void saveFile(File file, int indexFile){
        Log.e(TAG,"File: "+String.valueOf(indexFile)+", "+file.getAbsolutePath());
        switch (indexFile){
            case IMG_CED_FRT:
                this.file0=file;
                break;
            case IMG_CED_ADV:
                this.file1=file;
                break;
            case IMG_PAG_FRT:
                this.file2=file;
                break;
            case IMG_PAG_ADV:
                this.file3=file;
                break;
        }
        //this.file.add(indexFile,file);
    }

    private void sendImageMultiPart(File file, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_File(
                file, tagFragment, objectFragment, BROACAST_INSCRIP_ERROR
        );
    }


    public static final int CONST_PERMISSION_IMAGE=123;
    @AfterPermissionGranted(CONST_PERMISSION_IMAGE)
    private void permissionImage() {
        //private void connect(int mode, String roomId, String nameCall, String numberCall, String idDevice) {
        String[] perms = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            //hacer
            Log.e(TAG,"----------------------------------");
            takeImage(imageSelected);
        } else {
            EasyPermissions.requestPermissions(this, "Necesita habilitar permisos", CONST_PERMISSION_IMAGE, perms);
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

    String imageSelected="";
    String pathImage=null;
    private void takeImage(final String objectFragm){
        //new Http(getActivity()).enlaceLoadCamera(TAG, objectFragm);
        PickSetup pickSetup = new PickSetup();
        pickSetup.setHeight(1200);
        pickSetup.setWidth(960);
        PickImageDialog.build(pickSetup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //fileBitmap=null;
                        if(r.getBitmap()!=null) {
                            //fileBitmap = new Bitmap(r.getBitmap());
                            //fileBitmap=getResizedBitmap(r.getBitmap(), 960, 1200);
                            Log.e(TAG, "pathImage: "+r.getPath());
                            /*
                            pathImage = r.getPath();
                            File fDestination = createNewFile("Dupree");
                            Log.e(TAG, "fDestination: "+fDestination.getAbsolutePath());
                            copyFile(new File(pathImage), fDestination);
                            pathImage = fDestination.getAbsolutePath();
                            */
                            //Log.e(TAG, "fDestination2: "+fDestination.getAbsolutePath());
                            pathImage = r.getPath();

                            switch (objectFragm){
                                case BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH:
                                    imgCedFrontal.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                                    new fileFromBitmap(r.getBitmap(), "documents", IMG_CED_FRT, getActivity().getApplicationContext()).execute();
                                    //path_image.add(IMG_CED_FRT, pathImage);
                                    setImgCedFrontalON(true);
                                    break;
                                case BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH:
                                    imgCedAdverso.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                                    new fileFromBitmap(r.getBitmap(), "documents", IMG_CED_ADV, getActivity().getApplicationContext()).execute();
                                    //path_image.add(IMG_CED_ADV, pathImage);
                                    setImgCedAdverosON(true);
                                    break;
                                case BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH:
                                    imgPagFrontal.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                                    new fileFromBitmap(r.getBitmap(), "documents", IMG_PAG_FRT, getActivity().getApplicationContext()).execute();
                                    //path_image.add(IMG_PAG_FRT, pathImage);
                                    setImgPagFrontalON(true);
                                    break;
                                case BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH:
                                    imgPagAdverso.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                                    new fileFromBitmap(r.getBitmap(), "documents", IMG_PAG_ADV, getActivity().getApplicationContext()).execute();
                                    //path_image.add(IMG_PAG_ADV, pathImage);
                                    setImgPagAdverosON(true);
                                    break;
                            }

                        } else {
                            msgToast("No se logro cargar la imagen");
                        }
                    }
                }).show(getChildFragmentManager());
    }

    public void snackBar(String msg){
        Snackbar.make(ctnInscription, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).setDuration(5000).show();
    }

}
