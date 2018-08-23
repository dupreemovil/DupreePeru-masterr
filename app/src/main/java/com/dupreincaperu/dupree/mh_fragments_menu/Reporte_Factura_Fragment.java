package com.dupreincaperu.dupree.mh_fragments_menu;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.PDFActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_interface_api.iFile;
import com.dupreincaperu.dupree.mh_required_api.RequiredIdenty;
import com.dupreincaperu.dupree.mh_response_api.Factura;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Factura;
import com.dupreincaperu.dupree.mh_dialogs.SimpleDialog;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_response_api.ResponseFactura;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class Reporte_Factura_Fragment extends Fragment {

    private final String TAG = "Reporte_Factura_Frag.";
    private final String BROACAST_S_DIALOG_FACTURA = "broadcast_simple_dialog_factura";
    private final String BROACAST_FACTURA_ASESORA = "broadcast_factura_asesora";

    private MH_Adapter_Factura adapter_factura;
    private List<Factura> listFactura, listFilter;

    public Reporte_Factura_Fragment() {
        // Required empty public constructor
    }

    CardView cardViewBackGround;
    TextView tvNombreAsesora;
    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte_factura, container, false);

        cardViewBackGround = v.findViewById(R.id.cardViewBackGround);
        cardViewBackGround.setVisibility(View.INVISIBLE);
        tvNombreAsesora = v.findViewById(R.id.tvNombreAsesora);
        tvNombreAsesora.setText("");

        RecyclerView rcvFactura = v.findViewById(R.id.rcvFactura);
        rcvFactura.setLayoutManager(new GridLayoutManager(getActivity(),1));
        rcvFactura.setHasFixedSize(true);


        listFactura = new ArrayList<>();
        listFilter = new ArrayList<>();

        //listFactura = getFactura();
        //listFilter.addAll(listFactura);


        adapter_factura = new MH_Adapter_Factura(listFactura, listFilter, getActivity());
        rcvFactura.setAdapter(adapter_factura);

        adapter_factura.setRVOnDownloadClickListener(new MH_Adapter_Factura.DownloadClickListener() {
            @Override
            public void onDownloadClick(View v, int position) {
                testDescargar();

            }
        });

        localBroadcastReceiver = new LocalBroadcastReceiver();

        checkFactura();

        return v;
    }

    public void viewFactura(String urlFile){
        Intent intent = new Intent(getActivity(), PDFActivity.class);
        intent.putExtra(PDFActivity.URL_FILE, urlFile);
        startActivity(intent);
    }

    private void checkFactura(){
        if(perfil != null){
            if(perfil.getPerfil().equals(Perfil.ADESORA)){
                searchIdenty("");
            }
        }
    }

    private void updateView(){
        listFactura.clear();
        listFilter.clear();
        listFactura.addAll(responseFactura.getResult());
        listFilter.addAll(responseFactura.getResult());

        cardViewBackGround.setVisibility(View.VISIBLE);
        tvNombreAsesora.setText(responseFactura.getAsesora());

        adapter_factura.notifyDataSetChanged();
    }
    /*
    public List<Factura>  getFactura(){
        List<Factura> facturas = new ArrayList<>();

        facturas.add(new Factura("201618","9969010","12/05/2016","https://intranet.dupree.co/temporales/9969010.pdf",1));
        facturas.add(new Factura("201618","9964625","12/02/2016","https://intranet.dupree.co/temporales/9964625.pdf",2));

        return facturas;
    }
    */

    public void filterFactura(String textFilter){
        //adapter_catalogo.getmFilter().filter(textFilter);
        Log.e("newText to: ", textFilter);
        adapter_factura.getmFilter().filter(textFilter);
    }

    public void testDescargar(){
        SimpleDialog simpleDialog = new SimpleDialog();
        simpleDialog.loadData(getString(R.string.descargar), getString(R.string.desea_descargar));
        simpleDialog.activateBroadcast(TAG, BROACAST_S_DIALOG_FACTURA);
        simpleDialog.show(getActivity().getSupportFragmentManager(),"mDialog");
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

    ResponseFactura responseFactura;
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            Log.i(TAG, "BroadcastReceiver");
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {//pregunta cual elemento envio este broadcast
                    //Datos personales
                    case BROACAST_S_DIALOG_FACTURA:
                        if(intent.getStringExtra(BROACAST_S_DIALOG_FACTURA).equals(SimpleDialog.BROACAST_S_DIALOG_YES)){
                            Log.i(TAG, "BROACAST_S_DIALOG_YES");
                            //callPhone();
                            int position = adapter_factura.getPosSelected();
                            if(position!=-1){
                                //downloadFile(listFilter.get(position).getLink());
                                viewFactura(listFilter.get(position).getLink());
                            }else{
                                msgToast("Error! leyendo el archivo... Intente nuevamente");
                            }

                        } else if(intent.getStringExtra(BROACAST_S_DIALOG_FACTURA).equals(SimpleDialog.BROACAST_S_DIALOG_NO)){
                            Log.i(TAG, "BROACAST_S_DIALOG_NO");
                        }
                        //txtSpnTypeId.setText(intent.getStringExtra(ListString.BROACAST_REG_DATA));
                        break;
                    case BROACAST_FACTURA_ASESORA:
                        String jsonFactura = intent.getStringExtra(Http.BROACAST_DATA);
                        if(jsonFactura!=null){
                            responseFactura = new Gson().fromJson(jsonFactura, ResponseFactura.class);
                            updateView();
                        }
                        break;
                }
            }
        }
    }

    /*
    public void downloadFile(String url){
        //EXAMPLE URL "https://alcor.dupree.co/image/banner/catalogos400x300.jpg"
        String[] separate = url.split("/");//divide trama en un arreglo separado por "/"
        String fileName = separate[separate.length-1];

        new Http(getActivity()).downloadFile(url, fileName, DownloadFileAsyncTask.DIRECTORY_DOCUMENTS);
        //downloadFile2(url);
    }*/


    public void downloadFile2(String fileUrl){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://intranet.dupree.co/")
                //.addConverterFactory(GsonConverterFactory.create())
                //.client(client.build())
                .build();
        //final iReport service = retrofit.create(iReport.class);

        iFile service = retrofit.create(iFile.class);

        Call<ResponseBody> call = service.downloadFileWithDynamicUrlSync("temporales/9624785.pdf");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");

                    boolean writtenToDisk = /*((MenuActivity) myContext).*/writeResponseBodyToDisk(response.body());

                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "error");
            }
        });

    }

    public boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getActivity().getExternalFilesDir(null) + File.separator + "FileDownloaded.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void msgToast(String msg){
        Log.e("Toast", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void searchIdenty(String cedula){
        new Http(getActivity()).getFactura(new RequiredIdenty(cedula), TAG, BROACAST_FACTURA_ASESORA);
    }

}
