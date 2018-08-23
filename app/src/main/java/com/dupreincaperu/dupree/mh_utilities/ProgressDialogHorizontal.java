package com.dupreincaperu.dupree.mh_utilities;

/*
 * Created by Marwuin on 15/12/2016.
 */

import android.app.ProgressDialog;
import android.content.Context;

import com.dupreincaperu.dupree.R;

public class ProgressDialogHorizontal {
    private ProgressDialog pDialog;

    Context myContext;

    public ProgressDialogHorizontal(Context myContext) {
        this.myContext = myContext;
    }

    public void showProgressDialog(String msg){
        pDialog = new ProgressDialog(myContext);
        pDialog.setProgressDrawable(myContext.getResources().getDrawable(R.drawable.my_progress_bar));//personaliza los colores
        pDialog.setMessage(msg);
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//K0LM6F
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
    }


    public ProgressDialog getpDialog() {
        return pDialog;
    }
}
