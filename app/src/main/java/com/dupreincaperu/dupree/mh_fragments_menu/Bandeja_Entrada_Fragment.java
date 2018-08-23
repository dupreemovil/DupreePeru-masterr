package com.dupreincaperu.dupree.mh_fragments_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_Adapter_Bandeja;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_local_data.Data;
import com.dupreincaperu.dupree.mh_required_api.RequiredUsuario;
import com.dupreincaperu.dupree.mh_required_api.Required_IdMessages;
import com.dupreincaperu.dupree.mh_response_api.ResponseBandeja;
import com.dupreincaperu.dupree.mh_utilities.ImageZoomActivity_Scroll;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
//import java.util.function.Predicate;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bandeja_Entrada_Fragment extends Fragment {

    //Menu myMenu;
    SwipeRefreshLayout refreshFilesDetail;
    RecyclerView rcvFilesDetail;
    private MH_Adapter_Bandeja adapterFilesDetail;

    //Toolbar Toolbar_Act;
    //TextView titleToolbar;
    FloatingActionButton fabDelete;

    String folder;

    int lastNumItemSelected=-1;

    public Bandeja_Entrada_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bandeja_entrada, container, false);

        //TOOLBAR
        /*
        Toolbar_Act = (Toolbar) findViewById(R.id.Toolbar_Act);
        Toolbar_Act.setTitle("");
        setSupportActionBar(Toolbar_Act);
        titleToolbar = (TextView) Toolbar_Act.findViewById(R.id.titleToolbar);
        titleToolbar.setText(folder);
        */


        fabDelete = (FloatingActionButton) v.findViewById(R.id.fabDelete);

        refreshFilesDetail = (SwipeRefreshLayout) v.findViewById(R.id.RefreshFilesDetail);
        rcvFilesDetail = (RecyclerView) refreshFilesDetail.findViewById(R.id.rcvFilesDetail);
        //rcvFilesDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvFilesDetail.setHasFixedSize(true);
        //QUITA ANIMACIONES AL ACTUALIZAR VISTA.... POR TSNTO NO SE VE PARPADEO AL ACTUALIZAR PROGRESSBAR
        ((SimpleItemAnimator) rcvFilesDetail.getItemAnimator()).setSupportsChangeAnimations(false);

        Data.msg.clear();
        adapterFilesDetail = new MH_Adapter_Bandeja(Data.msg, getActivity());
        adapterFilesDetail.setRVOnItemClickListener(new MH_Adapter_Bandeja.ItemsClickListener() {
            @Override
            public void onClickItem(View v, int position) {
                Log.e("onClickItem: ",String.valueOf(position));
                Log.e("getNumItemSelected(): ",String.valueOf(adapterFilesDetail.getNumItemSelected()));
                if((lastNumItemSelected==1 && adapterFilesDetail.getNumItemSelected()==0) || adapterFilesDetail.getNumItemSelected()>0){
                    updateView(adapterFilesDetail.getNumItemSelected());                // Actualiza Toolbar
                }else{
                    //gotoZoomImage(Data.mh_folders.get(Position).getImages().get(position).getImage());
                    //String[] array_images = new String[Data.msg.size()];
                    //for(int i=0; i<Data.msg.size(); i++){
                    //    array_images[i] = Data.msg.get(i).getImage();
                    //}
                    //gotoZoomImage_Scroll(array_images, position);
                }

                lastNumItemSelected = adapterFilesDetail.getNumItemSelected();

            }
        });

        adapterFilesDetail.setRVOnItemLongClickListener(new MH_Adapter_Bandeja.ItemsLongClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                Log.e("onLongClick: ",String.valueOf(position));
                updateView(adapterFilesDetail.getNumItemSelected());                // Actualiza Toolbar
                lastNumItemSelected = adapterFilesDetail.getNumItemSelected();
            }
        });
        adapterFilesDetail.setRVOnImageClickListener(new MH_Adapter_Bandeja.ImageClickListener() {
            @Override
            public void onClickImage(View v, int position) {
                String[] array_images = new String[1];
                //for(int i=0; i<mh_chat_details.get(position).getMessage_file().size(); i++){
                    array_images[0] = Data.msg.get(position).getImagen();
                //}
                gotoZoomImage_Scroll(array_images);
            }
        });
        rcvFilesDetail.setAdapter(adapterFilesDetail);
        rcvFilesDetail.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshFilesDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFilesDetail.setRefreshing(false);
                obtainFilesFolder();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogoDelete();
            }
        });

        obtainFilesFolder();
        //////////////////////////
        ///updateUserProfile(Data.mh_folders);
        //createProgress();


        return v;
    }

    private void gotoZoomImage_Scroll(String[] images_array){
        Intent intent = new Intent(getActivity(), ImageZoomActivity_Scroll.class);
        intent.putExtra(ImageZoomActivity_Scroll.ARRAY_IMAGES, images_array);
        intent.putExtra(ImageZoomActivity_Scroll.POSITION_IMAGES, 0);
        startActivity(intent);
    }

    private void deleteMessages(final List<Integer> idMessages){
        showProgressDialog();
        Http http = new Http(getActivity());
        http.deleteMessages(new Required_IdMessages(idMessages));
        http.setBandejaListener(new Http.BandejaListener() {
            @Override
            public void onProcess(ResponseBandeja responseBandeja) {
                dismissProgressDialog();

                filesFolderDeleted(idMessages);
            }

            @Override
            public void onFailed() {
                dismissProgressDialog();
            }
        });
    }

    private void readMessages(List<Integer> idMessages){
        showProgressDialog();
        Http http = new Http(getActivity());
        http.readMessages(new Required_IdMessages(idMessages));
        http.setBandejaListener(new Http.BandejaListener() {
            @Override
            public void onProcess(ResponseBandeja responseBandeja) {
                dismissProgressDialog();

                //Data.msg.clear();
                //Data.msg.addAll(responseBandeja.getResult());

                adapterFilesDetail.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                dismissProgressDialog();
            }
        });
    }

    private void obtainFilesFolder(){
        showProgressDialog();
        Http http = new Http(getActivity());
        http.obtainBandejaEntrada();
        http.setBandejaListener(new Http.BandejaListener() {
            @Override
            public void onProcess(ResponseBandeja responseBandeja) {
                dismissProgressDialog();

                Data.msg.clear();
                Data.msg.addAll(responseBandeja.getResult());

                adapterFilesDetail.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                dismissProgressDialog();
            }
        });
    }

    public void RefreshFolder(List<ResponseBandeja.ResultBandeja> responseListImages){
        Data.msg.clear();
        Data.msg.addAll(responseListImages);


        //se debe refrezcar la carpeta que enviado archivos
        adapterFilesDetail.notifyDataSetChanged();

    }

    public void filesFolderDeleted(List<Integer> arrayIdImages){
        adapterFilesDetail.clearItemSelected();
        updateView(0);
        //obtainFilesFolder();
        //este procedimiento evita la consulta anterior
        refreshAll(DELETED, arrayIdImages);
    }

    private int DELETED=0;
    private int ADDED=1;
    private void refreshAll(int action, List<Integer> arrayIdImages){
        adapterFilesDetail.clearItemSelected();
        updateView(0);

        if(action == DELETED){
            for(int i=0; i<arrayIdImages.size(); i++) {
                /*final int j = i;
                Predicate<ResponseBandeja.ResultBandeja> imagesPredicate = p -> p.getId_mensaje().equals(arrayIdImages.get(j));
                Data.msg.removeIf(imagesPredicate);
                */
                for(int j=0; j<Data.msg.size(); j++){
                    if(arrayIdImages.get(i) == Integer.parseInt(Data.msg.get(j).getId_mensaje())){
                        Data.msg.remove(j);
                    }
                }

            }
        } else if(action == ADDED){// esta requiere que backend envie el id de la imagen al agregar, lo cual evita la consulta luego de agregar

        }

        adapterFilesDetail.notifyDataSetChanged();
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("BottomSheets", "onKeyDown A: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Log.e("BottomSheets", "onKeyDown B: " + keyCode);
            // Esto es lo que hace mi botón al pulsar ir a atrás
            boolean noBack=false;

            if(mStatus == Status.OPEN) {
                Log.e("BottomSheets", "onKeyDown: mStatus == Status.OPEN");
                adapterFilesDetail.clearItemSelected();
                //adapterFilesDetail.notifyDataSetChanged();
                //updateToolbar(0);
                updateView(0);
                noBack=true;
            }

            if(!noBack) {
                Log.e("BottomSheets", "onKeyDown: !noBack");
                //onBackPressed();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

*/

    public void setRefreshUserProfile(Boolean refresh){
        refreshFilesDetail.setRefreshing(refresh);
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                if(mStatus == Status.OPEN) {
                    adapterFilesDetail.clearItemSelected();
                    //updateToolbar(0);
                    updateView(0);
                }else{
                    //onBackPressed();
                }
                break;
            case R.id.menuDeleteFile:
                if(adapterFilesDetail.getNumItemSelected()>0){
                    showDialogoDelete();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    */


    public void showDialogoDelete() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.eliminar);
        alertDialog.setMessage(R.string.desea_remover_mensajes);
        alertDialog.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //deleteFiles();
                deleteMessages(arrayIdFiles());
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuToolbar=menu;
        getMenuInflater().inflate(R.menu.mh_menu_file, menu);
        return super.onCreateOptionsMenu(menu);
    }
    */


    public List<Integer> arrayIdFiles(){
        List<Integer> requiredIds=new ArrayList<>();
        if(Data.msg.size()>0){
            for(int i=0;i<Data.msg.size();i++){
                if(Data.msg.get(i).getItemSelected()){
                    requiredIds.add(Integer.parseInt(Data.msg.get(i).getId_mensaje()));
                }
            }

        }
        return requiredIds;
    }

    public void updateView(int numItemSelected){
        updateToolbar(numItemSelected);
        //fab.UpdateFab(numItemSelected);
        //rvAdapter.updateSbSwipeRecycler(numItemSelected);
    }

    private Menu menuToolbar;
    public Status updateToolbar(int numItemSelected){
        //Drawable upArrow = null;
        //menuToolbar.findItem(R.id.menu_toolbar_refresh).setVisible(true);
        //Log.e("11111111111111111", String.valueOf(adapterFilesDetail.getNumItemSelected()));
        if(numItemSelected==0){
            if(mStatus == Status.OPEN) {
                mStatus = Status.CLOSE;
                fabDelete.setVisibility(View.GONE);
                //titleToolbar.setText(folder);
                //titleToolbar.setTextColor(getResources().getColor(R.color.gray_6));
                //Toolbar_Act.setBackgroundColor(getResources().getColor(R.color.gray_1));
                //logoMenu.setVisibility(View.VISIBLE);

                //menuToolbar.findItem(R.id.menuMoveFile).setVisible(false);
                //menuToolbar.findItem(R.id.menuDeleteFile).setVisible(false);
            }
        } else {
            if(mStatus == Status.CLOSE){
                mStatus = Status.OPEN;
                fabDelete.setVisibility(View.VISIBLE);
                //titleToolbar.setTextColor(getResources().getColor(R.color.gray_1));
                //Toolbar_Act.setBackgroundColor(getResources().getColor(R.color.gray_6));
                //logoMenu.setVisibility(View.GONE);

                //menuToolbar.findItem(R.id.menuMoveFile).setVisible(Data.mh_folders.size()>1);//solo si hay mas de una carpeta
                //menuToolbar.findItem(R.id.menuDeleteFile).setVisible(true);
            }
            //titleToolbar.setText("( " + adapterFilesDetail.getNumItemSelected() + " )");
            //menuToolbar.findItem(R.id.menuProfileFile).setVisible(numItemSelected==1);
            //menuToolbar.findItem(R.id.menuShareFile).setVisible(numItemSelected==1);
        }

        return mStatus;
    }

    private Status mStatus = Status.CLOSE;
    private enum Status {
        OPEN,
        CLOSE
    }

    ////////////////// HTTP IMAGES   /////////////////

    private void createProgress(){
        pDialog = ProgressDialog.show(getActivity(), null, null, true);
        pDialog.setContentView(R.layout.adapter_progress);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();

    }

    private ProgressDialog pDialog;
    public void showProgressDialog(){
        pDialog = ProgressDialog.show(getActivity(), null, null, true);
        pDialog.setContentView(R.layout.adapter_progress);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
    }

}
