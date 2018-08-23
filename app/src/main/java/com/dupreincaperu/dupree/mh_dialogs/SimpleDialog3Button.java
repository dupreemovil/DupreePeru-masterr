package com.dupreincaperu.dupree.mh_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dupreincaperu.dupree.R;

/**
 * Fragmento con diálogo básico
 */
public class SimpleDialog3Button extends DialogFragment {

    private final String TAG = "SimpleDialog3Button";
    public static final String BROACAST_S_DIALOG_YES = "broadcast_simple_dialog_call_yes";
    public static final String BROACAST_S_DIALOG_NO = "broadcast_simple_dialog_call_no";

    private String titulo="";
    private String message="";

    //datos de respuesta broadcast
    private boolean broadcastON=false;
    private String idClase_Broadcast="";
    private String idObjeto_Broadcast="";
    private String responseYES="";
    private String responseNO="";

    public SimpleDialog3Button() {
    }

    private String titleAprobar="";
    private String titleRechazar="";
    public void loadData(String titulo, String message, String titleAprobar, String titleRechazar) {
        this.titulo = titulo;
        this.message = message;
        this.titleAprobar = titleAprobar;
        this.titleRechazar = titleRechazar;
    }

    public void activateBroadcast(String idClase_Broadcast, String idObjeto_Broadcast) {
        broadcastON=true;
        this.idClase_Broadcast = idClase_Broadcast;
        this.idObjeto_Broadcast = idObjeto_Broadcast;
        this.responseYES = BROACAST_S_DIALOG_YES;
        this.responseNO = BROACAST_S_DIALOG_NO;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(titulo)
                .setMessage(message)
                .setPositiveButton(titleAprobar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones
                                if(broadcastON)
                                    publishResult(responseYES);
                                dismiss();
                            }
                        })
                .setNegativeButton(titleRechazar,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Acciones
                                if(broadcastON)
                                    publishResult(responseNO);
                                dismiss();
                            }
                        })
                .setNeutralButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        });

        return builder.create();
    }

    private void publishResult(String data){
        Log.i(TAG,"publishResult: "+data);
        Log.i(TAG,"idClase_Broadcast: "+idClase_Broadcast);
        Log.i(TAG,"idObjeto_Broadcast: "+idObjeto_Broadcast);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(idClase_Broadcast)
                        .putExtra(idClase_Broadcast, idObjeto_Broadcast)
                        .putExtra(idObjeto_Broadcast, data));
    }
}