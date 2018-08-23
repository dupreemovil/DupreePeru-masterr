package com.dupreincaperu.dupree.mh_fragments_menu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_response_api.mPerfil;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_utilities.Validate;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 */
public class Config_ModPerfil_Fragment extends Fragment {
    private final String TAG = "Conf_ModPerfil_Frag-->";
    private final String BROACAST_PERF_TYPE_OBTAIN_DATA="perf_type_obtain_data";
    private final String BROACAST_PERF_TYPE_EDIT_DATA="perf_type_edit_data";

    public Config_ModPerfil_Fragment() {
        // Required empty public constructor
    }


    EditText txtNamePerfil, txtLastnamePerfil, txtPhonePerfil, txtCellphonePerfil, txtEmail;
    Button btnEditPerfil;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_config_mod_perfi, container, false);

        localBroadcastReceiver = new LocalBroadcastReceiver();

        txtNamePerfil = v.findViewById(R.id.txtNamePerfil);
        txtLastnamePerfil = v.findViewById(R.id.txtLastnamePerfil);
        txtPhonePerfil = v.findViewById(R.id.txtPhonePerfil);
        txtCellphonePerfil = v.findViewById(R.id.txtCellphonePerfil);
        txtEmail = v.findViewById(R.id.txtEmail);
        btnEditPerfil = v.findViewById(R.id.btnEditPerfil);

        btnEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePerfil()){
                    new Http(getActivity()).editarPerfil(
                            new mPerfil(
                                txtNamePerfil.getText().toString(),
                                txtLastnamePerfil.getText().toString(),
                                txtPhonePerfil.getText().toString(),
                                txtCellphonePerfil.getText().toString(),
                                txtEmail.getText().toString()),
                            TAG,
                            BROACAST_PERF_TYPE_EDIT_DATA);
                }
            }
        });

        checkPerfil();

        return v;
    }

    private void checkPerfil(){
        String jsonPerfil = mPreferences.getJSON_PerfilUser(getActivity());
        if(jsonPerfil!=null){
            setData(jsonPerfil);
        } else {
            new Http(getActivity()).getPerfilUser(TAG,BROACAST_PERF_TYPE_OBTAIN_DATA);
        }
    }

    private boolean validatePerfil(){
        Validate validate = new Validate();

        if(txtNamePerfil.getText().toString().isEmpty()){
            validate.setLoginError(getString(R.string.campo_requerido), txtNamePerfil);
            return false;
        }else if(txtLastnamePerfil.getText().toString().isEmpty()){
            validate.setLoginError(getString(R.string.campo_requerido), txtLastnamePerfil);
            return false;
        }else if(txtPhonePerfil.getText().toString().isEmpty()){
            validate.setLoginError(getString(R.string.campo_requerido), txtPhonePerfil);
            return false;
        }else if(txtCellphonePerfil.getText().toString().length()<10){
            validate.setLoginError(getString(R.string.campo_requerido), txtCellphonePerfil);
            return false;
        }else if(txtPhonePerfil.getText().toString().equals(txtCellphonePerfil.getText().toString())){
            validate.setLoginError(getString(R.string.telefonos_iguales), txtCellphonePerfil);
            return false;
        }else if(validate.isValidEmail(txtEmail.getText().toString())){
            validate.setLoginError(getString(R.string.campo_requerido), txtEmail);
            return false;
        }

        return  true;
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
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {
                    //Datos personales
                    case BROACAST_PERF_TYPE_OBTAIN_DATA:
                        Log.i(TAG, "BROACAST_PERF_TYPE_OBTAIN_DATA");
                        setData(intent.getStringExtra(Http.BROACAST_DATA));
                        break;
                    case BROACAST_PERF_TYPE_EDIT_DATA:
                        Log.i(TAG, "BROACAST_PERF_TYPE_EDIT_DATA");
                        //setData(intent.getStringExtra(Http.BROACAST_DATA));
                        msgToast(intent.getStringExtra(Http.BROACAST_DATA));
                        break;
                }
            }
        }
    }

    private void setData(String jsonPerfil){
        mPerfil perfilUser = new Gson().fromJson(jsonPerfil, mPerfil.class);
        if(perfilUser!=null){
            txtNamePerfil.setText(perfilUser.getNombre());
            txtLastnamePerfil.setText(perfilUser.getApellido());
            txtPhonePerfil.setText(perfilUser.getTelefono());
            txtCellphonePerfil.setText(perfilUser.getCelular());
            txtEmail.setText(perfilUser.getCorreo());
        }
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
