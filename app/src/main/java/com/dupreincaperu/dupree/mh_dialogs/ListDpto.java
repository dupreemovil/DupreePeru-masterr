package com.dupreincaperu.dupree.mh_dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.dupreincaperu.dupree.mh_response_api.Departamento;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//import android.app.DialogFragment;

/**
 * Created by Marwuin on 11-May-17.
 */

public class ListDpto extends DialogFragment {
    private final String TAG = "ListDpto";
    public static  final String BROACAST_REG="register";
    public static  final String BROACAST_DATA="broacast_data";
    public static  final String BROACAST_REG_TYPE_DPTO="reg_type_dpto";
    public static  final String BROACAST_REG_TYPE_DPTO_2="reg_type_dpto_2";

    public static ListDpto newInstance() {

        Bundle args = new Bundle();

        ListDpto fragment = new ListDpto();
        fragment.setArguments(args);
        return fragment;
    }

    public ListDpto() {
        //new HttpGaver(getActivity()).obtainCountries();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createRadioListDialog();
    }

    private int numItem=-1;
    private List<Departamento> departamentoList;
    private String title;
    private String tagFragment;
    private String objectFragment;
    private String itemSelected;
    public void loadData(String tagFragment, String objectFragment, String title, List<Departamento> departamentoList, String itemSelected){
        this.departamentoList=departamentoList;
        this.title=title;
        this.objectFragment=objectFragment;
        this.tagFragment=tagFragment;
        this.itemSelected=itemSelected;
    }

    public AlertDialog createRadioListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        List<String> list = new ArrayList<>();
        for(int i=0;i<departamentoList.size();i++){
            list.add(departamentoList.get(i).getName_dpto());
        }

        final CharSequence[] items = new CharSequence[list.size()];
        for(int i=0;i<list.size();i++){
            items[i]=list.get(i);
            if(list.get(i).equals(itemSelected))
                numItem=i;
        }

        builder.setTitle(title)
                .setSingleChoiceItems(items, numItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                                getActivity(),
                                "Seleccionaste: " + items[which],
                                Toast.LENGTH_SHORT)
                                .show();
                        publishResult(new Gson().toJson(departamentoList.get(which)));
                        //publishResult(items[which].toString());
                        dismiss();
                    }
                });

        return builder.create();
    }


    private void publishResult(String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));

    }
}
