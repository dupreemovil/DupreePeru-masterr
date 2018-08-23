package com.dupreincaperu.dupree.mh_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_utilities.Validate;

/**
 * Created by cloudemotion on 6/8/17.
 */

public class MH_Dialogs_Input extends DialogFragment {
    String TAG = "MH_Dialogs_Input";
    public static  final String BROACAST_DATA="broacast_data";

    public MH_Dialogs_Input() {
    }

    public static MH_Dialogs_Input newInstance() {
        Bundle args = new Bundle();

        MH_Dialogs_Input fragment = new MH_Dialogs_Input();
        fragment.setArguments(args);
        return fragment;
    }

    private String title;
    private String tagFragment;
    private String objectFragment;
    private String placeHolder;
    public void loadData(String tagFragment, String objectFragment, String title, String placeHolder){
        this.tagFragment=tagFragment;
        this.objectFragment=objectFragment;
        this.title=title;
        this.placeHolder=placeHolder;
    }

    int style = DialogFragment.STYLE_NO_TITLE;
    int theme = R.style.MyDialogTransparent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(style, theme);
    }

    private String m_Text = "";
    EditText input;
    EditText code_txtCode1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("Title");

        // Set up the input
        input = new EditText(getActivity());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextSize(22);
        input.setHint(placeHolder);
        builder.setView(input);

        // Set up the buttons
        builder.setTitle(title);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(ValidateImput()){
                    publishResult(m_Text);
                    //publishResult(items[which].toString());
                    dismiss();

                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.cancel();
                dismiss();
            }
        });

        return builder.show();
    }

    public Boolean ValidateImput() {
        Validate valid = new Validate();
        if ( m_Text.isEmpty() )
        {
            msgToast("Debe ingresar una cédula valida...");
            valid.setLoginError(getResources().getString(R.string.campo_requerido), input);
            return false;
        }

        return  true;
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void publishResult(String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));

    }
}
