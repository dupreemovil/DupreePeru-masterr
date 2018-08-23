package com.dupreincaperu.dupree.mh_dialogs;

/**
 * Created by Marwuin on 24/4/2017.
 */

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dupreincaperu.dupree.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Fragmento con un diálogo de elección de fechas
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private final String TAG = "DateDialog";
    public static final String BROACAST_REG="register";
    public static final String BROACAST_DATA="broacast_data";
    public static final String BROACAST_REG_TYPE_BIRD="reg_type_bird";

    int groupPosition, childPosition;
    private GregorianCalendar date;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Obtener fecha actual
        final Calendar c = Calendar.getInstance();
        Log.e("DATADATADATAATADATAFA",c.getTime().toString());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Retornar en nueva instancia del dialogo selector de fecha
        /*
        return new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getActivity(),
                year,
                month,
                day);
                */

        DatePickerDialog date = new DatePickerDialog(getActivity(), this, year, month, day);


        Calendar minCal = Calendar.getInstance();
        minCal.set(Calendar.YEAR, minCal.get(Calendar.YEAR) - 100);
        Calendar maxCal = Calendar.getInstance();
        maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 18);
        date.getDatePicker().setMinDate(minCal.getTimeInMillis());
        date.getDatePicker().setMaxDate(maxCal.getTimeInMillis());


        return date;
    }

    private String tagFragment;
    private String objectFragment;
    public void setData(String tagFragment, String objectFragment){
        this.tagFragment = tagFragment;
        this.objectFragment = objectFragment;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //date = new GregorianCalendar(year, month, day);
        //Log.i(TAG,"onDateSet: "+date.toString());
        int myMonth = month + 1;
        publishResult(day+"/"+myMonth+"/"+year);
    }

    public GregorianCalendar getDate() {
        return date;
    }

    private String object;
    private void publishResult(String data){
        Log.i(TAG,"publishResult: "+data);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(
                new Intent(tagFragment)
                        .putExtra(tagFragment, objectFragment)
                        .putExtra(BROACAST_DATA, data));

    }


}