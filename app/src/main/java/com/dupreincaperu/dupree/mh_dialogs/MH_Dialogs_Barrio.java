package com.dupreincaperu.dupree.mh_dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Autocomplete;
import com.dupreincaperu.dupree.mh_response_api.Barrio;
import com.dupreincaperu.dupree.mh_utilities.Validate;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by cloudemotion on 6/8/17.
 */

public class MH_Dialogs_Barrio extends DialogFragment {

    public static  final String BROACAST_DATA="broacast_data";
    private final String TAG = "MH_Dialogs_Barrio";
    public static  final String BROACAST_REG="register";
    public static  final String BROACAST_REG_DATA="reg_data";
    public static  final String BROACAST_REG_TYPE_BARRIO="reg_type_barrio";
    public static  final String BROACAST_REG_TYPE_BARRIO_2="reg_type_barrio_2";

    public MH_Dialogs_Barrio() {
    }



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
        //window.setLayout((int) (size.x * 0.85), (int) (size.y * 0.85));
        window.setLayout((int) (size.x * 1.00), (int) (size.y * 1.00));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public static MH_Dialogs_Barrio newInstance() {
        Bundle args = new Bundle();

        MH_Dialogs_Barrio fragment = new MH_Dialogs_Barrio();
        fragment.setArguments(args);
        return fragment;

    }

    private List<Barrio> barrioList;
    private List<Barrio> filterBarrio;
    private String title;
    private String tagFragment;
    private String objectFragment;
    private String itemSelected;
    public void loadData(String tagFragment, String objectFragment, String title, List<Barrio> filterBarrio, List<Barrio> barrioList, String itemSelected){
        this.barrioList=barrioList;
        this.filterBarrio=filterBarrio;
        this.title=title;
        this.tagFragment=tagFragment;
        this.objectFragment=objectFragment;
        this.itemSelected=itemSelected;
    }

    int style = DialogFragment.STYLE_NO_TITLE;
    int theme = R.style.MyDialogTransparent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(style, theme);
    }

    EditText txtFilter;
    Button btnCancelarBarrio, btnAgregarBarrio;
    MH_Adapter_Autocomplete adapterAutocomplete;
    RecyclerView rcvBarrio;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.mh_recycler_autocomplete_barrio, null);

        final Drawable d = new ColorDrawable(Color.BLACK);//DUPREE RGB
        d.setAlpha(170);
        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(true);

        txtFilter = (EditText) view.findViewById(R.id.txtFilter);
        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapterAutocomplete.getmFilter().filter(s.toString());
            }
        });
        rcvBarrio = (RecyclerView) view.findViewById(R.id.rcvBarrio);
        rcvBarrio.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvBarrio.setHasFixedSize(true);

        adapterAutocomplete = new MH_Adapter_Autocomplete(filterBarrio, barrioList, getActivity().getApplicationContext());
        adapterAutocomplete.setRVOnItemClickListener(new MH_Adapter_Autocomplete.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                publishResult(new Gson().toJson(filterBarrio.get(position)));
                dismiss();
            }
        });
        rcvBarrio.setAdapter(adapterAutocomplete);

        btnAgregarBarrio = (Button) view.findViewById(R.id.btnAgregarBarrio);
        btnCancelarBarrio = (Button) view.findViewById(R.id.btnCancelarBarrio);

        btnCancelarBarrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAgregarBarrio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateTermins()){
                    publishResult(new Gson().toJson(new Barrio("0",txtFilter.getText().toString())));
                    //publishResult(items[which].toString());
                    dismiss();
                }
            }
        });

        return dialog;
    }

    public Boolean ValidateTermins() {
        Validate valid = new Validate();

        if ( txtFilter.getText().toString().isEmpty() )
        {
            valid.setLoginError(getResources().getString(R.string.campo_requerido), txtFilter);
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
