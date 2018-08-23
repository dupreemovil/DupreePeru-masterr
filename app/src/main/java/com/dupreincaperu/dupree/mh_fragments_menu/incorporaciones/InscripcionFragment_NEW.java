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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_dialogs.DateDialog;
import com.dupreincaperu.dupree.mh_dialogs.ListCity;
import com.dupreincaperu.dupree.mh_dialogs.ListDpto;
import com.dupreincaperu.dupree.mh_dialogs.ListString;
import com.dupreincaperu.dupree.mh_dialogs.MH_Dialogs_Barrio;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.Referencia;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_required_api.RequiredInscription;
import com.dupreincaperu.dupree.mh_required_api.RequiredInscription_NEW;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister;
import com.dupreincaperu.dupree.mh_required_api.RequiredValidateRef;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.dupreincaperu.dupree.mh_response_api.Ciudad;
import com.dupreincaperu.dupree.mh_response_api.Departamento;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.PinchZoomImageView;
import com.dupreincaperu.dupree.mh_utilities.Validate;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class InscripcionFragment_NEW extends Fragment {
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
    public static final String BROACAST_REG_TYPE_BARRIOS="reg_type_barriosB";
    public static final String BROACAST_REG_TYPE_BARRIOS_2="reg_type_barrios_2B";

    public static final String BROACAST_INSCRIP_EDIT="inscrip_type_edit";

    public static final String BROACAST_INSCRIP_SUSSCCEFUL="inscrip_type_susscceful";

    public static final String BROACAST_INSCRIP_ERROR="inscrip_type_error";

    private final int IMG_CED_FRT=0, IMG_CED_ADV=1, IMG_PAG_FRT=2, IMG_PAG_ADV=3;

    private List<Barrio> barrioList;
    private List<Barrio> filterBarrio;

    public InscripcionFragment_NEW() {
        // Required empty public constructor
    }

    public static InscripcionFragment_NEW newInstance() {
        Bundle args = new Bundle();

        InscripcionFragment_NEW fragment = new InscripcionFragment_NEW();
        fragment.setArguments(args);
        return fragment;
    }

    boolean modeEdit = false;
    public void loadData(String name, String cedula, boolean modeEdit){
        clearData();
        this.modeEdit = modeEdit;
        txtNameIncrip.setText(name.isEmpty() ? Incorp_ListPre_Fragment.nameSelected : name);//se redunda xq a veces no llega
        txtIdentyCard.setText(cedula.isEmpty() ? Incorp_ListPre_Fragment.identySelected : cedula);
        if(modeEdit){//cuando se debe editar una inscripcion
            new Http(getActivity()).getDataEditInscripcion(new RequiredIdenty(cedula), TAG, BROACAST_INSCRIP_EDIT, BROACAST_INSCRIP_ERROR);
        }
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


    // --------- DE CAMBIOS SOLICITADOS
    EditText txtDateBird, txtZone, txtSpnTypeVia,
            txtNum1, txtSpnChar1, txtSpnBis, txtNum2, txtSpnChar2, txtNum3, txtSpnSur,
            txtInfo, txtSpnDpto, txtSpnCity, txtSpnBarrio,txtSpnDirSend, txtPhone, txtCellphone,
            txtEmail;
    TextView txtConcatenateDir, txtConcatenateDir_2;
    LinearLayout ctxDirSend;
    EditText txtSpnTypeVia_2,
            txtNum1_2, txtSpnChar1_2, txtSpnBis_2, txtNum2_2, txtSpnChar2_2, txtNum3_2, txtSpnSur_2,
            txtInfo_2, txtSpnDpto_2, txtSpnCity_2, txtSpnBarrio_2;
    // --------- DE CAMBIOS SOLICITADOS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_inscripcion_new, container, false);

        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));

        // --------- DE CAMBIOS SOLICITADOS
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

        getListDpto(mPreferences.getDpto(getActivity()));
        // --------- DE CAMBIOS SOLICITADOS

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

        url_image = new ArrayList<>();
        url_image.add("");
        url_image.add("");
        url_image.add("");
        url_image.add("");


        createProgress();

        return v;
    }

    public Perfil getPerfil(){
        String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());
        if(jsonPerfil!=null)
            return new Gson().fromJson(jsonPerfil, Perfil.class);

        return null;
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

    public List<Barrio> extractListBarrio(String data){
        Type listType = new TypeToken<ArrayList<Barrio>>(){}.getType();
        return new Gson().fromJson(data, listType);
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
                    if(validateRegister()){
                        if(validateIncrip()) {
                            intentRepeat = "";
                            //sendImageMultiPart(path_image.get(IMG_CED_FRT),TAG, BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL);
                            showProgressDialog();
                            //aqui hubo un cambio... se solicito poder editar datos enviados
                            if(modeEdit) {
                                if(file0 != null){
                                    sendImageMultiPart(file0, TAG, BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL);
                                } else if(file1 != null){
                                    sendImageMultiPart(file1, TAG, BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL);
                                } else if(file2 != null){
                                    sendImageMultiPart(file2, TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                                } else if(file3 != null){
                                    sendImageMultiPart(file3, TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                                } else {
                                    new Http(getActivity()).inscribir_NEW(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                                }
                            } else {
                                sendImageMultiPart(file0, TAG, BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL);
                            }
                        }
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
                    imageSelected = BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_PATH);
                    break;
                case R.id.imgCedAdverso:
                    imageSelected = BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_PATH);
                    break;
                case R.id.imgPagFrontal:
                    imageSelected = BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_PATH);
                    break;
                case R.id.imgPagAdverso:
                    imageSelected = BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH;
                    permissionImage();
                    //takeImage(BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_PATH);
                    break;


                //////////// DE NUEVOS CAMBIOS
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
                //////////// DE NUEVOS CAMBIOS


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    private boolean refValidated=false;
    private boolean imgCedFrontalON=false;
    private boolean imgCedAdverosON=false;
    private boolean imgPagFrontalON=false;
    private boolean imgPagAdverosON=false;

    private boolean validateIncrip(){
        Validate validate = new Validate();

        /*if(txtNameIncrip.getText().toString().isEmpty()){
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


        else */if(file0==null && !modeEdit){
            msgToast("Cédula frontal... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgCedAdverosON()){
        else if(file1==null && !modeEdit){
            msgToast("Cédula adverso... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgPagFrontalON()){
        else if(file2==null && !modeEdit){
            msgToast("pagaré frontal... Verifique");
            //validate.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        }//else if(!isImgPagAdverosON()){
        else if(file3==null && !modeEdit){
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
        /*else if(txtPhone1.getText().toString().isEmpty()){
            msgToast("Teléfono de ref. personal... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtPhone1);
            return false;
        }*/
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
        /*
        else if(txtPhone2.getText().toString().isEmpty()){
            msgToast("Teléfono de ref. familiar... Verifique");
            validate.setLoginError(getString(R.string.campo_requerido),txtPhone2);
            return false;
        }*/
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

        setRefValidated_NEW(refValidated);
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
                switch (intent.getStringExtra(TAG)) {
                    //Datos personales

                    ///CAMBIOS NUEVOS
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
                    ///CAMBIOS NUEVOS
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
                    case BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_FRONT_URL");
                        String url0 = intent.getStringExtra(ListString.BROACAST_DATA);
                        //url_image.add(IMG_CED_FRT, url0);
                        url_image.set(IMG_CED_FRT, url0);

                        if(!modeEdit || file1 != null){//en modo edicion si file == null, no se ha modificado y se llama al sigueinet caso
                            sendImageMultiPart(file1,TAG, BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL);
                        } else {
                            if(file2 != null){
                                sendImageMultiPart(file2, TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                            } else if(file3 != null){
                                sendImageMultiPart(file3, TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                            } else {
                                new Http(getActivity()).inscribir_NEW(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                            }
                        }
                        //sendImageMultiPart(file1,TAG, BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_CED_ADVER_URL");
                        String url1 = intent.getStringExtra(ListString.BROACAST_DATA);
                        //url_image.add(IMG_CED_ADV, url1);
                        url_image.set(IMG_CED_ADV, url1);

                        if(!modeEdit || file2 != null){//en modo edicion si file == null, no se ha modificado y se llama al sigueinet caso
                            sendImageMultiPart(file2,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                        } else if(file3 != null){
                            sendImageMultiPart(file3, TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                        } else {
                            new Http(getActivity()).inscribir_NEW(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                        }
                        //sendImageMultiPart(file2,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_FRONT_URL");
                        String url2 = intent.getStringExtra(ListString.BROACAST_DATA);
                        //url_image.add(IMG_PAG_FRT, url2);
                        url_image.set(IMG_PAG_FRT, url2);


                        if(!modeEdit || file3 != null){//en modo edicion si file == null, no se ha modificado y se llama al sigueinet caso
                            sendImageMultiPart(file3,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                        } else {
                            new Http(getActivity()).inscribir_NEW(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                        }
                        //sendImageMultiPart(file3,TAG, BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL);
                        break;
                    case BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL:
                        Log.i(TAG, "BROACAST_INSCRIP_TYPE_IMG_PAG_ADVER_URL");
                        String url3 = intent.getStringExtra(ListString.BROACAST_DATA);
                        //url_image.add(IMG_PAG_ADV, url3);
                        url_image.set(IMG_PAG_ADV, url3);

                        new Http(getActivity()).inscribir_NEW(obtainData(), TAG, BROACAST_INSCRIP_SUSSCCEFUL, BROACAST_INSCRIP_ERROR);
                        break;
                    case BROACAST_INSCRIP_SUSSCCEFUL:
                        dismissProgressDialog();
                        String result = intent.getStringExtra(ListString.BROACAST_DATA);
                        msgToast(result);
                        clearData();
                        new Http(getActivity()).enlaceUpdateList();
                        //txtIdentyCardRef.setfocus
                        break;
                    case BROACAST_INSCRIP_EDIT:
                        String resultEdit = intent.getStringExtra(ListString.BROACAST_DATA);

                        loadDataInit(new Gson().fromJson(resultEdit, RequiredInscription_NEW.class));

                        break;
                    case BROACAST_INSCRIP_ERROR:
                        dismissProgressDialog();
                        break;
                }
            }
        }
    }

    /**
     * BROADCAST
     * @param tagFragment
     * @param objectFragment
     * @param data
     */
    private void publishResultRegister(String tagFragment, String objectFragment, String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(ListString.BROACAST_DATA, data));
    }

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

    public String concatenateDir_2() {
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

    public String concatenateDir_2_Protocol() {
        return txtSpnTypeVia_2.getText().toString() + " | " +
                (txtNum1_2.getText().toString() + " | ") +
                (txtSpnChar1_2.getText().toString() + " | ") +
                (txtSpnBis_2.getText().toString() + " | ") +
                (txtNum2_2.getText().toString() + " | ") +
                (txtSpnChar2_2.getText().toString() + " | ") +
                (txtNum3_2.getText().toString() + " | ") +
                (txtSpnSur_2.getText().toString() + " | ") +
                txtInfo_2.getText().toString() + " | " +

                (txtSpnDpto_2.getTag() != null ? txtSpnDpto_2.getTag().toString() : "") + " | " +
                (txtSpnDpto_2.getText() != null ? txtSpnDpto_2.getText().toString() : "") + " | " +

                (ciudad_2 != null && ciudad_2.getId_ciudad() != null ? ciudad_2.getId_ciudad() : "") + " | " +
                (ciudad_2 != null && ciudad_2.getName_ciudad() != null ? ciudad_2.getName_ciudad() : "") + " | " +

                (barrio_2 != null && barrio_2.getId_barrio() != null ? barrio_2.getId_barrio() : "") + " | " +
                (barrio_2 != null && barrio_2.getName_barrio() != null ? barrio_2.getName_barrio() : "");
    }

    private void clearAllData_NEW(){
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

    public void setRefValidated_NEW(boolean refValidated) {
        this.refValidated = refValidated;
        intentRepeat="";

        imgB_Searchref.setVisibility(refValidated ? View.GONE : View.VISIBLE);
        imgB_CleanRef.setVisibility(!refValidated ? View.GONE : View.VISIBLE);


        //habilitando campos debajo
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

    public boolean validateRegister()
    {
        Validate valid=new Validate();
        //datos personales

        if(txtNameIncrip.getText().toString().isEmpty()){
            msgToast("Nombre de asesora... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),txtNameIncrip);
            return false;
        }else if(txtIdentyCard.getText().toString().isEmpty()){
            msgToast("Cêdula de asesora... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),txtIdentyCard);
            return false;
        }

        else if(txtIdentyCardRef.getText().toString().isEmpty()){
            msgToast("Cêdula de referido... Verifique");
            valid.setLoginError(getString(R.string.campo_requerido),txtIdentyCardRef);
            return false;
        }else if(!isRefValidated()){
            msgToast("Debe validar la cédula del referido... Verifique");
            valid.setLoginError(getString(R.string.deba_validar),txtIdentyCardRef);
            return false;
        } else
        if (txtDateBird.getText().toString().isEmpty())
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
        else if (!txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase()) && txtNum1.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 1... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum1);
            return false;
        }
        else if (!txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase()) && txtNum2.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 2... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum2);
            return false;
        }
        else if (!txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase()) && txtNum3.getText().toString().isEmpty())
        {
            msgToast("Dir. Res. > Número 3... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum3);
            return false;
        }
        else if (txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase())  && txtInfo.getText().toString().isEmpty())
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
            else if (!txtSpnTypeVia_2.getText().toString().toUpperCase().equals("Otro".toUpperCase()) && txtNum1_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 1... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum1_2);
                return false;
            }
            else if (!txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase())  && txtNum2_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 2... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum2_2);
                return false;
            }
            else if (!txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase())  && txtNum3_2.getText().toString().isEmpty())
            {
                msgToast("Dir. Envío. > Número 3... Verifiqu");
                valid.setLoginError(getResources().getString(R.string.campo_requerido), txtNum3_2);
                return false;
            }
            else if (txtSpnTypeVia.getText().toString().toUpperCase().equals("Otro".toUpperCase())  && txtInfo_2.getText().toString().isEmpty())
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
            //contacto
            else if (!validatenew())
            {
                return false;
            }
        }
        //contacto
        else if (!validatenew())
        {
            return false;
        }

        return true;

    }

    private boolean validatenew(){
        Validate valid=new Validate();
        //contacto
        /*
        if (txtPhone.getText().toString().isEmpty())
        {
            msgToast("Teléfono fijo... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtPhone);
            return false;
        }
        else if (txtCellphone.getText().toString().isEmpty())
        {
            msgToast("Teléfono movil... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtCellphone);
            return false;
        }
        else */if (!txtCellphone.getText().toString().isEmpty() && txtCellphone.getText().toString().length() < 10)
        {
            msgToast("Teléfono movil (10 números)... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtCellphone);
            return false;
        }
        else if (!txtPhone.getText().toString().isEmpty() && !txtCellphone.getText().toString().isEmpty() && txtPhone.getText().toString().equals(txtCellphone.getText().toString()))
        {
            msgToast("Teléfonos deben ser diferentes... Verifique");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtCellphone);
            return false;
        }
        /*
        else if (valid.isValidEmail(txtEmail.getText().toString()))
        {
            msgToast("Formato de correo incorrecto... Verifique");
            valid.setLoginError(getResources().getString(R.string.formato_incorrecto), txtEmail);
            return false;
        }*/
        return true;
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

        clearAllData_NEW();
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
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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

    private RequiredInscription_NEW obtainData(){
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


        refPersonal.setParentesco(
                String.valueOf(
                        Arrays.asList(getResources().getStringArray(R.array.parentescoOptions)).indexOf(
                                txtSpnParentesco1.getText().toString()
                        )
                )
        );


        referenciaList.add(refPersonal);
        Log.i(TAG, "JSON 1"+new Gson().toJson(refPersonal));

        Referencia refFamiliar = new Referencia();
        refFamiliar.setTipo_documento_id(
                String.valueOf(
                        Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(
                                txtSpnParentesco2.getText().toString()
                        )
                )
        );
        //refFamiliar.setTipo_documento_id(String.valueOf(Arrays.asList(getResources().getStringArray(R.array.typeIdenty)).indexOf(txtSpnTypeDoc2.getText().toString())));
        //refFamiliar.setCedula(txtIdentyCard2.getText().toString());
        refFamiliar.setCedula("");
        refFamiliar.setNombre(txtName2.getText().toString());
        refFamiliar.setApellido(txtLastName2.getText().toString());
        refFamiliar.setTelefono(txtPhone2.getText().toString());
        refFamiliar.setCelular(txtCellPhone2.getText().toString());
        refFamiliar.setParentesco(
                String.valueOf(
                        Arrays.asList(
                                getResources().getStringArray(R.array.parentescoOptions)).indexOf(
                                        txtSpnParentesco2.getText().toString()
                        )
                )
        );
        referenciaList.add(refFamiliar);
        Log.i(TAG, "JSON 2"+new Gson().toJson(refFamiliar));

        //RequiredInscription requiredInscription = new RequiredInscription();

        RequiredInscription_NEW requiredInscription = obtainDataUser();


        requiredInscription.setCedula(Validate.stringToLong(txtIdentyCard.getText().toString()));
        requiredInscription.setImg_cedula(url_image.subList(0,2));
        requiredInscription.setPagare(url_image.subList(2,4));
        requiredInscription.setReferenciado_por(txtIdentyCardRef.getText().toString());
        //requiredInscription.setUser(txtInputNameReferido.getHint()!= null ? txtInputNameReferido.getHint().toString() : "");
        requiredInscription.setReferencia(referenciaList);
        Log.i(TAG, "JSON 3"+new Gson().toJson(requiredInscription));

        return requiredInscription;
    }

    public RequiredInscription_NEW obtainDataUser()
    {

        RequiredInscription_NEW dataRegisterModel = new RequiredInscription_NEW(
                Long.parseLong(txtIdentyCard.getText().toString()),
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
                ctxDirSend.getVisibility()==View.GONE ? "" : concatenateDir_2_Protocol() /*txtConcatenateDir_2.getText().toString()*/,
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

    private void estractDirEnvio(String dirConcatenate){

    }
    private void loadDataInit(RequiredInscription_NEW data){
        setRefValidated(true);

        txtDateBird.setText(data.getNacimiento());
        txtIdentyCardRef.setText(data.getReferenciado_por());
        txtInputNameReferido.setHint(data.getReferenciado_nombre());
        txtZone.setText(data.getZona_seccion());


        txtPhone.setText(data.getTelefono());
        txtCellphone.setText(data.getCelular());
        txtEmail.setText(data.getCorreo());
        txtDateBird.setText(data.getNacimiento());
        //"52486488956555",

        txtSpnTypeVia.setText(data.getTipo_via());
        txtNum1.setText(data.getNumero1());
        txtSpnChar1.setText(data.getLetra1());
        txtSpnBis.setText(data.getBis());
        txtNum2.setText(data.getNumero2());
        txtSpnChar2.setText(data.getLetra2());
        txtNum3.setText(data.getNumero3());
        txtSpnSur.setText(data.getPcardinal());
        txtInfo.setText(data.getComplemento());

        txtSpnDpto.setText(data.getDepartamento());
        ciudad = new Ciudad(String.valueOf(data.getId_ciudad()), data.getName_ciudad());
        txtSpnCity.setText(ciudad.getName_ciudad());

        barrio = new Barrio(String.valueOf(data.getId_barrio()), data.getBarrio());
        txtSpnBarrio.setText(barrio.getName_barrio());

        txtConcatenateDir.setText(data.getDireccion_concatenada());
        //ctxDirSend.getVisibility()==View.GONE ? "" : txtConcatenateDir_2.getText().toString(),
        //ctxDirSend.getVisibility()==View.GONE ? "" : barrio_2.getId_barrio()

        ////SET
        String dir_envio = data.getDireccion_envio();
        if(!dir_envio.isEmpty()) {
             String[] dataArray = dir_envio.split(" \\| ");

            Log.e(TAG,"nume elementos: "+dataArray.length);

            txtSpnDirSend.setError(null);
            txtSpnDirSend.setText(Arrays.asList(getResources().getStringArray(R.array.dirSend)).get(1));

            ctxDirSend.setVisibility(View.VISIBLE);

            if(dataArray.length == 15){
                txtSpnTypeVia_2.setText(dataArray[0]);
                txtNum1_2.setText(dataArray[1]);
                txtSpnChar1_2.setText(dataArray[2]);
                txtSpnBis_2.setText(dataArray[3]);
                txtNum2_2.setText(dataArray[4]);
                txtSpnChar2_2.setText(dataArray[5]);
                txtNum3_2.setText(dataArray[6]);
                txtSpnSur_2.setText(dataArray[7]);
                txtInfo_2.setText(dataArray[8]);

                txtSpnDpto_2.setTag(dataArray[9]);
                txtSpnDpto_2.setText(dataArray[10]);

                ciudad_2 = new Ciudad(dataArray[11], dataArray[12]);
                txtSpnCity_2.setTag(ciudad_2.getId_ciudad());
                txtSpnCity_2.setText(ciudad_2.getName_ciudad());

                barrio_2 = new Barrio(dataArray[13], dataArray[14]);
                txtSpnBarrio_2.setTag(barrio_2.getId_barrio());
                txtSpnBarrio_2.setText(barrio_2.getName_barrio());
            } else {
                msgToast("Debe ingresar nuevamente la dirección de envio");
            }

        } else {
            txtSpnDirSend.setText(Arrays.asList(getResources().getStringArray(R.array.dirSend)).get(0));
        }

        url_image = new ArrayList<>();
        img.displayImage(data.getImg_cedula().get(0), imgCedFrontal);
        url_image.add(data.getImg_cedula().get(0));
        setImgCedFrontalON(true);

        img.displayImage(data.getImg_cedula().get(1), imgCedAdverso);
        url_image.add(data.getImg_cedula().get(1));
        setImgCedAdverosON(true);

        img.displayImage(data.getPagare().get(0), imgPagFrontal);
        url_image.add(data.getPagare().get(0));
        setImgPagFrontalON(true);

        img.displayImage(data.getPagare().get(1), imgPagAdverso);
        url_image.add(data.getPagare().get(1));
        setImgPagAdverosON(true);

        txtName1.setText(data.getReferencia().get(0).getNombre());
        txtLastName1.setText(data.getReferencia().get(0).getApellido());
        txtPhone1.setText(data.getReferencia().get(0).getTelefono());
        txtCellPhone1.setText(data.getReferencia().get(0).getCelular());
        //txtSpnParentesco1.setText(data.getReferencia().get(0).getParentesco());
        int parent1 = Integer.parseInt(
                data.getReferencia().get(0).getParentesco().isEmpty() ? "0" : data.getReferencia().get(0).getParentesco()
        );
        txtSpnParentesco1.setText(Arrays.asList(
                getResources().getStringArray(R.array.parentescoOptions)).get(
                    parent1 < 0 ? 0 : parent1
                )
        );

        txtName2.setText(data.getReferencia().get(1).getNombre());
        txtLastName2.setText(data.getReferencia().get(1).getApellido());
        txtPhone2.setText(data.getReferencia().get(1).getTelefono());
        txtCellPhone2.setText(data.getReferencia().get(1).getCelular());
        //txtSpnParentesco2.setText(data.getReferencia().get(1).getParentesco());
        int parent2 = Integer.parseInt(
                data.getReferencia().get(1).getParentesco().isEmpty() ? "0" : data.getReferencia().get(1).getParentesco()
        );
        txtSpnParentesco2.setText(Arrays.asList(
                getResources().getStringArray(R.array.parentescoOptions)).get(
                    parent2 < 0 ? 0 : parent2
                )
        );

    }

    private ImageLoader img;
}
