package com.dupreincaperu.dupree.mh_fragments_menu;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.ImageZoomActivity;
import com.dupreincaperu.dupree.MenuActivity;
import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_adapters.MH_PagerAdapter_PanelAsesora;
import com.dupreincaperu.dupree.mh_dialogs.ListString;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_response_api.PanelAsesora;
import com.dupreincaperu.dupree.mh_response_api.Perfil;
import com.dupreincaperu.dupree.mh_utilities.PinchZoomImageView;
import com.dupreincaperu.dupree.mh_utilities.mPreferences;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Panel_Asesora_Fragment extends Fragment {

    private final String TAG = "Panel_Asesora_Fragment";
    public static  final String BROACAST_ASESORA_TYPE_DATA="asespra_type_data";
    public static  final String BROACAST_INSCRIP_IMG_PROFILE="broadcast_inscrip_img_profiel";

    public static final String BROACAST_PANEL_ERROR ="panel_type_error";
    public static final String BROACAST_PANEL_IMG_PROFILE_URL="panel_img_profiel_url";

    private final int IMG_CED_PROFILE=0;
    //private File file;


    SwipeRefreshLayout swipePanelAsesora;


    AppBarLayout appBarLayout;
    TabLayout tabsPanelAsesora;

    ViewPager pagerPanelAsesoras;
    LinearLayout layoutDotPanelAsesoras;

    MH_PagerAdapter_PanelAsesora pagerAdapter_panelAsesora;

    PanelAsesora panelAsesora;

    FloatingTextButton fabMessages;

    private Perfil perfil;
    public void loadData(Perfil perfil){
        this.perfil=perfil;
    }

    public Panel_Asesora_Fragment() {
        // Required empty public constructor
    }

    TextView tvCamp, tvNameAsesora, tvSaldoAsesora, tvCupoAsesora;
    CircleImageView profile_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_panel_asesora, container, false);

        fabMessages = v.findViewById(R.id.fabMessages);
        profile_image = v.findViewById(R.id.profile_image);
        profile_image.setVisibility(View.INVISIBLE);

        //SOlicitaron quitar foto de perfil por temas legales
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(perfil!=null && perfil.getImagen_perfil()!=null && !perfil.getImagen_perfil().isEmpty())
                //    gotoZoomImage(perfil.getImagen_perfil());
            }
        });
        profile_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //imageSelected=BROACAST_INSCRIP_IMG_PROFILE;
                //permissionImage();
                return false;
            }
        });

        if(perfil.getImagen_perfil()!=null && !perfil.getImagen_perfil().isEmpty()){
            Log.e(TAG, "JSON IMAGE" + perfil.getImagen_perfil());
            //imageProfile(perfil.getImagen_perfil());
        }


        tvCamp = v.findViewById(R.id.tvCamp);
        tvCamp.setText("");
        tvNameAsesora = v.findViewById(R.id.tvNameAsesora);
        tvNameAsesora.setText("");
        tvSaldoAsesora = v.findViewById(R.id.tvSaldoAsesora);
        tvSaldoAsesora.setText("");
        tvCupoAsesora = v.findViewById(R.id.tvCupoAsesora);
        tvCupoAsesora.setText("");

        ///PÁGINAS
        appBarLayout = v.findViewById(R.id.appBarLayout);
        appBarLayout.setVisibility(View.INVISIBLE);
        tabsPanelAsesora = v.findViewById(R.id.tabsPanelAsesora);
        tabsPanelAsesora.setVisibility(View.INVISIBLE);
        swipePanelAsesora = v.findViewById(R.id.swipePanelAsesora);
        swipePanelAsesora.setOnRefreshListener(mOnRefreshListener);
        swipePanelAsesora.setEnabled(false);

        pagerPanelAsesoras = swipePanelAsesora.findViewById(R.id.pagerPanelAsesoras);

        pagerAdapter_panelAsesora = new MH_PagerAdapter_PanelAsesora(getFragmentManager());
        pagerPanelAsesoras.setAdapter(pagerAdapter_panelAsesora);
        pagerPanelAsesoras.addOnPageChangeListener(mOnPageChangeListener);
        layoutDotPanelAsesoras = swipePanelAsesora.findViewById(R.id.layoutDotPanelAsesoras);

        tabsPanelAsesora.setupWithViewPager(pagerPanelAsesoras);
        createTabIcons();

        localBroadcastReceiver = new LocalBroadcastReceiver();


        createProgress();
        checkDetailPanel();

        return v;
    }

    private void gotoZoomImage(String urlImage){
        Intent intent = new Intent(getActivity(), ImageZoomActivity.class);
        intent.putExtra(ImageZoomActivity.URL_IMAGE, urlImage);
        startActivity(intent);
    }

    private void checkDetailPanel(){
        new Http(getActivity()).getPanelAsesora(TAG, BROACAST_ASESORA_TYPE_DATA);
    }

    private void updateView(){
        tvCamp.setText("CAMPAÑA ".concat(panelAsesora.getCampana()));

        appBarLayout.setVisibility(View.VISIBLE);
        tabsPanelAsesora.setVisibility(View.VISIBLE);
        profile_image.setVisibility(View.VISIBLE);
        tvNameAsesora.setText(panelAsesora.getNombre_asesora());
        tvSaldoAsesora.setText("Saldo: $".concat(panelAsesora.getSaldo()));
        tvCupoAsesora.setText("Cupo crédito: ".concat(panelAsesora.getCupo_credito()));

        if(panelAsesora.getCantidad_mensajes()!=null)
            fabMessages.setTitle(panelAsesora.getCantidad_mensajes()+" Mensajes");


        fabMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MenuActivity) getActivity()).gotoMessages();
            }
        });
        //verifica cual vista esta disponible para actualizarla

        updateTracking();
        updateFaltante_y_Conf();
    }



    /**
     * Eventos SwipeRefreshLayout
     */
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener
            = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh() {

        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG,"onPageSelected Page: "+position);
            switch (position){
                case MH_PagerAdapter_PanelAsesora.PAGE_TRACKING:
                    updateTracking();
                    break;
                case MH_PagerAdapter_PanelAsesora.PAGE_FALTANTES_Y_CONF:
                    updateFaltante_y_Conf();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateTracking(){
        if(panelAsesora!=null) {
            pagerAdapter_panelAsesora.getTrackingFragment().setData(panelAsesora.getTracking());
        }
    }

    private void updateFaltante_y_Conf(){
        if(panelAsesora!=null) {
            pagerAdapter_panelAsesora.getFaltantesAsesoraFragment().setData(panelAsesora.getFaltantes());
        }
    }

    private void createTabIcons() {

        //////////
        TextView tab1 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tab1.setText(getResources().getString(R.string.edo_pedido));

        Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_track_changes_white_24dp);
        mDrawable1.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tab1.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable1, null, null);
        tabsPanelAsesora.getTabAt(0).setCustomView(tab1);

        //////////
        TextView tab2 = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_item, null);
        tab2.setText(getResources().getString(R.string.faltantes));

        Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_info_outline_white_24dp);
        mDrawable2.setColorFilter(new
                PorterDuffColorFilter(getResources().getColor(R.color.azulDupree), PorterDuff.Mode.MULTIPLY));

        tab2.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable2, null, null);
        tabsPanelAsesora.getTabAt(1).setCustomView(tab2);

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

    private static long lastTime=0;
    private static String intentRepeat="";
    private BroadcastReceiver localBroadcastReceiver;
    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }

            Log.e(TAG,"Calendar.getInstance().getTimeInMillis()= "+String.valueOf(Calendar.getInstance().getTimeInMillis()));
            Log.e(TAG,"lastTime= "+String.valueOf(lastTime));
            //si en un tiempo menor a un segundo, llega el mismo resultado, se desecha
            //esto garantiza que se pueda ejecutar la misma accion manualmente, mas evita repeticiones del broadcast que hacen algunos telefonos
            if( (Calendar.getInstance().getTimeInMillis()-lastTime<1000) && intentRepeat.equals(intent.getExtras().toString())) {// si no ha pasado medio segundo puede ser un rebote
                Log.e(TAG,"intentRepeat()");
                return;
            }

            lastTime=Calendar.getInstance().getTimeInMillis();
            intentRepeat=intent.getExtras().toString();


            if (intent.getAction().equals(TAG)) {
                switch (intent.getStringExtra(TAG)) {
                    //Datos personales
                    case BROACAST_ASESORA_TYPE_DATA:
                        Log.i(TAG, "BROACAST_GRTE_TYPE_LIST_CAMP");
                        String jsonPanel = intent.getStringExtra(ListString.BROACAST_DATA);
                        if(jsonPanel!=null) {
                            panelAsesora = new Gson().fromJson(jsonPanel, PanelAsesora.class);
                            updateView();
                        }
                        break;
                    case BROACAST_PANEL_IMG_PROFILE_URL:
                        Log.i(TAG, "BROACAST_PANEL_IMG_PROFILE_URL");
                        dismissProgressDialog();
                        String url = intent.getStringExtra(ListString.BROACAST_DATA);
                        if(url!=null) {
                            saveImageProfile(url);
                            imageNewProfile(url);
                        }

                        break;
                    case BROACAST_PANEL_ERROR:
                        dismissProgressDialog();
                        break;
                }
            }
        }
    }
    private void saveImageProfile(String url){
        Log.i(TAG, "imageProfile: "+url);
        perfil.setImagen_perfil(url);
        mPreferences.setJSON_TypePerfil(getActivity(), perfil);
    }

    private void imageProfile(String url){
        Log.i(TAG, "imageProfile: "+url);
        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));
        //img.clearMemoryCache();
        //img.clearDiskCache();
        img.displayImage(url, profile_image);
    }

    //limpia la cache y obliga a bajarla nuevamente
    private void imageNewProfile(String url){
        //profile_image.setImageResource(0);

        Log.i(TAG, "imageNewProfile: "+url);



        img = ImageLoader.getInstance();
        img.init(PinchZoomImageView.configurarImageLoader(getActivity()));
        //img.displayImage("", profile_image);
        MemoryCacheUtils.removeFromCache(url, img.getMemoryCache());
        DiskCacheUtils.removeFromCache(url, img.getDiskCache());
        //img.clearMemoryCache();
        //img.clearDiskCache();
        img.displayImage(url, profile_image);
    }



    private ProgressDialog pDialog;
    private void createProgress(){
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//K0LM6F
    }

    public void showProgressDialog(){
        pDialog.setMessage(getResources().getString(R.string.msg_espere));
        pDialog.show();
    }

    public void dismissProgressDialog(){
        if(pDialog!=null)
            pDialog.dismiss();
    }


    public static final int CONST_PERMISSION_IMAGE=123;
    @AfterPermissionGranted(CONST_PERMISSION_IMAGE)
    private void permissionImage() {
        //private void connect(int mode, String roomId, String nameCall, String numberCall, String idDevice) {
        String[] perms = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            //hacer
            Log.e(TAG,"----------------------------------");
            takeImage(imageSelected);
        } else {
            EasyPermissions.requestPermissions(this, "Necesita habilitar permisos", CONST_PERMISSION_IMAGE, perms);
        }
    }

    /**
     * Respuestas a permisos
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    ImageLoader img;
    String imageSelected="";
    String pathImage=null;
    private void takeImage(final String objectFragm){
        PickSetup pickSetup = new PickSetup();

        //pickSetup.setHeight(1200);
        //pickSetup.setWidth(960);
        //pickSetup.
        PickImageDialog.build(pickSetup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //fileBitmap=null;
                        if(r.getBitmap()!=null) {
                            Log.e(TAG, "pathImage: "+r.getPath());
                            pathImage = r.getPath();
                            switch (objectFragm){
                                case BROACAST_INSCRIP_IMG_PROFILE:
                                    //profile_image.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                                    new fileFromBitmap(r.getBitmap(), "documents", IMG_CED_PROFILE, getActivity().getApplicationContext()).execute();
                                    break;
                            }
                        } else {
                            msgToast("No se logro cargar la imagen");
                        }
                    }
                }).show(getChildFragmentManager());
    }

    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;//potencias de 2 (1= mitad , 4 , carta parte, 8 octaba parte)
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }


    private class fileFromBitmap extends AsyncTask<Void, Integer, String> {
        File file;
        Context context;
        Bitmap bitmap;
        int indexFile=0;
        String name;
        public fileFromBitmap(Bitmap bitmap, String name, int indexFile, Context context) {
            this.bitmap = bitmap;
            this.name= name;
            this.indexFile= indexFile;
            this.context= context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            //bitmap = resize(bitmap, 1406, 2500);
            bitmap = resize(bitmap, 400, 300);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            //si la quieres en discofile = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            String jsonPerfil = mPreferences.getJSON_TypePerfil(getActivity());
            if(jsonPerfil!=null){
                Perfil perfil = new Gson().fromJson(jsonPerfil, Perfil.class);
                file = new File(getActivity().getCacheDir() + "/" +perfil.getValor()+".jpg");//en cache
                //file = new File(Environment.getExternalStorageDirectory() + File.separator + Calendar.getInstance().getTimeInMillis() + perfil.getValor() + ".jpg");
            } else {
                //file = new File(Environment.getExternalStorageDirectory() + File.separator + Calendar.getInstance().getTimeInMillis() + perfil.getValor() + ".jpg");
                file = new File(getActivity().getCacheDir() + "/dupree_" + name + "_temporary_profile_file" + String.valueOf(indexFile) + ".jpg");//en cache
            }

            //file = new File(getActivity().getCacheDir() + "dupree_" + name + "_temporary_profile_file" + String.valueOf(indexFile) + ".jpg");//en cache
            try {
                FileOutputStream fo = new FileOutputStream(file, false);//false indica que se sobreescribe si existe
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            saveFile(file, indexFile);
        }

        private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
            if (maxHeight > 0 && maxWidth > 0) {
                int width = image.getWidth();
                int height = image.getHeight();
                float ratioBitmap = (float) width / (float) height;
                float ratioMax = (float) maxWidth / (float) maxHeight;

                int finalWidth = maxWidth;
                int finalHeight = maxHeight;
                if (ratioMax > 1) {
                    finalWidth = (int) ((float)maxHeight * ratioBitmap);
                } else {
                    finalHeight = (int) ((float)maxWidth / ratioBitmap);
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, false);
                return image;
            } else {
                return image;
            }
        }
    }

    File file;
    private void saveFile(File file, int indexFile){
        Log.e(TAG,"File: "+String.valueOf(indexFile)+", "+file.getAbsolutePath());
        switch (indexFile){
            case IMG_CED_PROFILE:
                this.file=file;
                showProgressDialog();
                sendImageMultiPart(this.file, TAG, BROACAST_PANEL_IMG_PROFILE_URL);
                break;
        }
        //this.file.add(indexFile,file);
    }

    private void sendImageMultiPart(File file, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_Profile(
                file, tagFragment, objectFragment, BROACAST_PANEL_ERROR
        );
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
