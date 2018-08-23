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

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_required_api.ListBoolean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import android.app.DialogFragment;

/**
 * Created by Marwuin on 11-May-17.
 */

public class ListString_Check extends DialogFragment {
    private final String TAG = "ListString_Check";
    public static  final String BROACAST_DATA="broacast_data";

    public static ListString_Check newInstance() {

        Bundle args = new Bundle();

        ListString_Check fragment = new ListString_Check();
        fragment.setArguments(args);
        return fragment;
    }

    public ListString_Check() {
        //new HttpGaver(getActivity()).obtainCountries();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createRadioListDialog();
    }

    private String[] itemsSelected;//true false de itams seleccionados
    private List<String> list;
    private String title;
    private String tagFragment;
    private String objectFragment;
    public void loadData(String tagFragment, String objectFragment, String title, List<String> list, String[] itemsSelected){
        this.list=list;
        this.title=title;
        this.tagFragment=tagFragment;
        this.objectFragment=objectFragment;
        this.itemsSelected=itemsSelected;
    }

    boolean[] posSelected;
    ListBoolean listBoolean;

    //List<Boolean> list;

    public AlertDialog createRadioListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final CharSequence[] items = new CharSequence[list.size()];

        posSelected = new boolean[list.size()];

        List<Boolean> booleanList = new ArrayList<Boolean>(Arrays.asList(new Boolean[list.size()]));
        Collections.fill(booleanList, Boolean.FALSE);
        listBoolean = new ListBoolean(booleanList);


        for(int i=0;i<list.size();i++){
            items[i]=list.get(i);
            posSelected[i]=false;

            if(itemsSelected!=null) {//elementos preselecionados
                for (int j = 0; j < itemsSelected.length; j++) {
                    if (list.get(i).equals(itemsSelected[j])) {
                        posSelected[i] = true;
                        //posSelected_serializable[i] = true;
                        listBoolean.getBooleanList().set(i, true);
                        break;
                    }
                }
            }
        }

        builder.setTitle(title)
                .setMultiChoiceItems(items, posSelected, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        Toast.makeText(
                                getActivity(),
                                "Seleccionaste: " + items[i],
                                Toast.LENGTH_SHORT)
                                .show();
                        listBoolean.getBooleanList().set(i, b);
                    }
                });

        builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*
                if(list.size()==2) {
                    if (!posSelected[1] && !posSelected[0]) {

                    } else if (posSelected[1] && !posSelected[0]) {
                        publishResult("1");
                    } else if (!posSelected[1] && posSelected[0]) {
                        publishResult("2");
                    } else if (posSelected[1] && posSelected[0]) {
                        publishResult("3");
                    }
                } else if(list.size()==1) {
                    if(posSelected[0]){
                        publishResult("1");
                    }
                }
                */

                publishResult(new Gson().toJson(listBoolean));

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
                        .putExtra(BROACAST_DATA, data)
        );
    }
}
