package com.dupreincaperu.dupree.mh_dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dupreincaperu.dupree.R;
import com.dupreincaperu.dupree.mh_fragments_main.RegisterAsesoraFragment;
import com.dupreincaperu.dupree.mh_http.Http;
import com.dupreincaperu.dupree.mh_utilities.MyDialoges;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by cloudemotion on 6/8/17.
 */

public class MH_Dialogs_Volante extends DialogFragment{

    private final String TAG = "MH_Dialogs_Volante";

    public MH_Dialogs_Volante() {
    }

    String pathImage=null;

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

    public static MH_Dialogs_Volante newInstance() {
        Bundle args = new Bundle();

        MH_Dialogs_Volante fragment = new MH_Dialogs_Volante();
        fragment.setArguments(args);
        return fragment;

    }

    private String asesora;
    RegisterAsesoraFragment registerAsesoraFragment;
    public void loadData(String asesora, RegisterAsesoraFragment registerAsesoraFragment){
        this.asesora=asesora;
        this.registerAsesoraFragment=registerAsesoraFragment;
    }

    int style = DialogFragment.STYLE_NO_TITLE;
    int theme = R.style.MyDialogTransparent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(style, theme);
    }

    TextView tvAsesoraVolante;
    Button btnCancelarVolante, btnEnviarVolante;
    ImageView imgVolante;

    //Bitmap fileBitmap;
    private File fileList;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        final View view = getActivity().getLayoutInflater().inflate(R.layout.mh_dialogo_volante, null);

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

        tvAsesoraVolante = (TextView) view.findViewById(R.id.tvAsesoraVolante);
        tvAsesoraVolante.setText(asesora);
        imgVolante = (ImageView) view.findViewById(R.id.imgVolante);
        imgVolante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permissionImage();
            }
        });
        //camera=new Camera(MH_Dialogs_Volante.this);
        //camera.setImageViewPic(imgVolante);


        btnEnviarVolante = (Button) view.findViewById(R.id.btnEnviarVolante);
        btnCancelarVolante = (Button) view.findViewById(R.id.btnCancelarVolante);

        btnCancelarVolante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnEnviarVolante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateTermins()){
                    //showDialogWait();
                    registerAsesoraFragment.showProgressDialog();///barra de progreso
                    //sendImageMultiPart(pathImage, RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE);
                    //sendImageMultiPart(fileBitmap, RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE);
                    //sendImageMultiPart(fileList.get(0), RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE);
                    sendImageMultiPart(fileList, RegisterAsesoraFragment.TAG, RegisterAsesoraFragment.BROACAST_REG_TYPE_TERMINS_VOLANTE);
                }
            }
        });

        //fileList = new ArrayList<>();
        //file = new File[](1);
        return dialog;
    }

    public Boolean ValidateTermins() {
        //if(pathImage==null) {
        if(fileList ==null) {
            msgToast("Debe seleccionar un volante");
            return false;
        }
        return  true;
    }


    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
        Log.e(TAG, "pathImage: "+pathImage);
        convertPathToImage(pathImage);
    }

    public void convertPathToImage(String pathImage){
        /*
        File imgFile = new  File(pathImage);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgVolante.setImageBitmap(myBitmap);
        } else {
            msgToast("Ocurrio un problema al leer la imagen");
        }
        */
        imgVolante.setImageBitmap(resizeBitmap(pathImage, 256, 256));//para mostrar en pantalla con poca resolusion 960, 1200 en xamarin para enviar, 256x256 para mostrar
    }
    private void sendImageMultiPart(String filePath, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_file(
                filePath, tagFragment, objectFragment
        );
    }

    private void sendImageMultiPart(Bitmap fileBitmap, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_bitmap(
                fileBitmap, tagFragment, objectFragment
        );
    }

    private void sendImageMultiPart(File file, String tagFragment, String objectFragment){
        new Http(getActivity()).uploadImage_File(
                file, tagFragment, objectFragment, RegisterAsesoraFragment.BROACAST_REG_ERROR
        );
    }

    private void msgToast(String msg){
        Log.e("onError", msg);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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


            // bitmap = resize2(bitmap, 960.0f, 1200.0f);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

            //si la quieres en discofile = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            file = new File(getActivity().getCacheDir() + "dupree_"+name+"_temporary_file"+String.valueOf(indexFile)+".jpg");//en cache
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                //fo.write(resize2(bitmap, 960.0f, 1200.0f));
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

        private Bitmap resize2(Bitmap image, float width, float height) {
            float ZielHoehe = 0;
            float ZielBreite = 0;

            int Hoehe = image.getHeight();
            int Breite = image.getWidth();

            if (Hoehe > Breite) {
                ZielHoehe = height;
                float teiler = Hoehe / height;
                ZielBreite = Breite / teiler;
            } else {
                ZielBreite = width;
                float teiler = Breite / width;
                ZielHoehe = Hoehe / teiler;
            }

            return Bitmap.createScaledBitmap(image, (int)ZielBreite, (int)ZielHoehe, false);



            //return resizedImage;
        }

    }

    private void saveFile(File file, int indexFile){
        Log.e("onError", file.getAbsolutePath());
        this.fileList=file;
    }

    public void error(){
        stopDialogoWait();
    }

    private void showDialogWait(){
        MyDialoges.showProgressDialog(getActivity(), getActivity().getResources().getString(R.string.msg_espere));
    }

    private void stopDialogoWait(){
        MyDialoges.dismissProgressDialog();
    }

    public static final int CONST_PERMISSION_IMAGE=123;
    @AfterPermissionGranted(CONST_PERMISSION_IMAGE)
    private void permissionImage() {
        //private void connect(int mode, String roomId, String nameCall, String numberCall, String idDevice) {
        String[] perms = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            //hacer
            Log.e(TAG,"----------------------------------");
            takeImage();
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

    public void takeImage(){
        PickSetup pickSetup = new PickSetup();
        pickSetup.setHeight(1200);
        pickSetup.setWidth(960);

        //pickSetup.setPickTypes()

        PickImageDialog.build(pickSetup)
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //fileBitmap=null;
                        if(r.getBitmap()!=null) {
                            //probando
                            fileList =null;
                            //se recorta la imagen y convierte en fil, para enviar (se intento en http, pero rmbitmap, se cierra y all no esta disponible)
                            //new fileFromBitmap(resize(r.getBitmap(), 960, 1200), getActivity().getApplicationContext()).execute();
                            new fileFromBitmap(r.getBitmap(), "volante", 0, getActivity().getApplicationContext()).execute();

                            //Glide.with(this).load(Functions.getByteArrayFromBitmap(r.getBitmap())).asBitmap()
                            //fileBitmap = new Bitmap(r.getBitmap());
                            //fileBitmap=getResizedBitmap(r.getBitmap(), 960, 1200);
                            Log.e(TAG, "pathImage: "+r.getPath());
                            pathImage = r.getPath();
                            imgVolante.setImageBitmap(resizeBitmap(pathImage, 256, 256));
                        } else {
                            msgToast("No se logro cargar la imagen");
                        }
                    }


                }).show(getChildFragmentManager());
    }
}
