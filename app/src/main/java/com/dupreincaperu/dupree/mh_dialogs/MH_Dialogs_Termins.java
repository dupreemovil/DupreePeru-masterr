package com.dupreincaperu.dupree.mh_dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_required_api.RequiredRegister;
import com.dupreincaperu.dupree.mh_utilities.Validate;

/**
 * Created by cloudemotion on 6/8/17.
 */

public class MH_Dialogs_Termins extends DialogFragment {

    String TAG = "MH_Dialogs_Termins";

    public MH_Dialogs_Termins() {
    }


    public static MH_Dialogs_Termins newInstance() {
        Bundle args = new Bundle();

        MH_Dialogs_Termins fragment = new MH_Dialogs_Termins();
        fragment.setArguments(args);
        return fragment;

    }

    String termins;
    RequiredRegister requiredRegister;
    RegisterAsesoraFragment registerAsesoraFragment;
    public void loadData(RequiredRegister requiredRegister, String termins, RegisterAsesoraFragment registerAsesoraFragment){
        this.requiredRegister=requiredRegister;
        this.termins=termins;
        this.registerAsesoraFragment=registerAsesoraFragment;
    }
    /*
    @Override
    public void onResume() {
        super.onResume();
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        //window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setLayout((int) (size.x * 0.95), (int) (size.y * 0.75));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }*/

    int style = DialogFragment.STYLE_NO_TITLE;
    int theme = R.style.MyDialogTransparent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(style, theme);
    }
/*
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mh_dialogo_termins, container);
    }


*/


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Retrieve layout elements



        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    EditText code_txtCode1, code_txtCode2, code_txtCode3, code_txtCode4;
    SwitchCompat swtTermins;
    TextView tvTermins;
    Button btnCancelar, btnAceptar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.mh_dialogo_termins, null);

        final Drawable d = new ColorDrawable(Color.GRAY);//DUPREE RGB
        d.setAlpha(200);
        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(true);

        code_txtCode1 = (EditText) view.findViewById(R.id.code_txtCode1);
        swtTermins = (SwitchCompat) view.findViewById(R.id.swtTermins);
        tvTermins = (TextView) view.findViewById(R.id.tvTermins);
        tvTermins.setText(termins);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateTermins()){
                    requiredRegister.setCode_gsm(code_txtCode1.getText().toString());
                    sendDataRegister();
                }
            }
        });

        return dialog;
    }

    public Boolean ValidateTermins() {
        Validate valid = new Validate();

        if (code_txtCode1.getText().toString().isEmpty() || code_txtCode1.getText().toString().length()<4 /*||code_txtCode2.getText().toString().isEmpty()||code_txtCode3.getText().toString().isEmpty()||code_txtCode4.getText().toString().isEmpty()*/)
        {
            msgToast("Debe ingresar el código SMS...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), code_txtCode1);
            return false;
        }
        else if (!swtTermins.isChecked())
        {
            msgToast("NO aceptó los téminos...");
            return false;
        }

        return  true;
    }

    private void sendDataRegister(){
        registerAsesoraFragment.showProgressDialog();
        new Http(getActivity()).register(requiredRegister, RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_REGISTER_TERMINS, RegisterAsesoraFragment.BROACAST_REG_ERROR);
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
